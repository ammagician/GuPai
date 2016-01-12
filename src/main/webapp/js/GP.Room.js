/**
 * Created by lanfeng on 2015/10/7.
 */
ns("GP");

GP.Room = function(){
    this.el = $(".roomContent");
    this.toolBar = $(".toolBar");
    this.roomId = "";

    this._initMessageCallback();
};

GP.Room.prototype = {
    _initMessageCallback: function(){
       var ws = getWebSocket();
       ws.addMessageCallback("sitSeat", this._onMessage, this);
    },

    _onMessage: function(data){
        var msg = data.msg,
            deskId = msg.deskId,
            p = msg.position,
            empty = msg.empty;
        var el = this.el;
        var desk = el.find(".deskItem deskId=[" +deskId+ "]");
        if(desk){
            var seat = desk.find(".seat pos=[" +p+ "]");
            this.setSeatStatus(seat, empty);
        }
    },

    resize: function(){
        clearTimeout(this.resizeRoomTimeout);
        this.resizeRoomTimeout = setTimeout(function(){
            $(".roomContent").css("padding-left", calPadding() + "px");
        }, 100);
    },

    getDeskList : function(roomId){
        this.roomId = roomId;
        var ctr = this;
        var msg = {roomId: roomId};
        var conn = new GP.Util.Connection();
        conn.add("url", "DeskGetDeskList.do").add({async:true, type: "GET", data:{data:$.toJSON(msg)}});
        conn.addListener("onSuccess", {onSuccess : function (conn, res) {
            var desks = res.data;
            ctr.createDesks(desks);
        }});
        conn.connect();
    },

    createDesks: function(desks){
        desks = desks || [];
        var html = "";
        for(var i= 0,len=desks.length; i<len; i++){
            var desk = desks[i];
            var str = this._createDesk(desk);
            html += str;
        }

        var room = this;
        var roomContent = this.el;
        roomContent.removeClass("none");
        roomContent.empty();
        roomContent.append($(html));
        roomContent.bind("click", function(e){
            var target = $(e.target);
            if(target.hasClass("seat")){
                if(target.attr("seatEmpty") != "1"){
                    return;
                }
                var position = target.attr("pos"),
                    deskEl = target.parent().parent(),
                    deskId = deskEl.attr("deskId"),
                    deskName = deskEl.find(".deskName").html();
                room.sitSeat(deskId, deskName, position, target);
            }
        });

        this.el.css("padding-left", calPadding() + "px");

        $(window).unbind("resize", this.resize);
        $(window).bind("resize", this.resize);
    },

    _createDesk: function(desk){
        var id = desk.id,
            name = desk.name,
            available = desk.available;
        var str = "<div class='tc deskItem fl w100 h160 m15' deskId='" + id + "'>" +
            "<div class='deskIcon w100 h100 pr'>" +
                "<div class='pa pointer seat seat-n' pos='north' seatEmpty='1'></div>" +
                "<div class='pa pointer seat seat-e' pos='east' seatEmpty='1'></div>" +
                "<div class='pa pointer seat seat-s' pos='south' seatEmpty='1'></div>" +
                "<div class='pa pointer seat seat-w' pos='west' seatEmpty='1'></div>" +
            "</div>" +
            "<div class='deskName w100 h30 lh150'>" + name + "</div>" +
            //"<div class='deskMember w100 h30 lh150'>" + (available? "未满员": "满员") + "</div>" +
            "</div>";
        return str;
    },

    sitSeat: function(deskId, deskName, position, seat){
        var msg = {
            roomId: this.roomId,
            deskId: deskId,
            position: position
        };
        var s = seat;
        var room = this;
        var conn = new GP.Util.Connection();
        conn.add("url", "DeskSitSeat.do").add({async:true, data:{data:$.toJSON(msg)}});
        conn.addListener("onSuccess", {onSuccess : function (conn, res) {
            var flag = res.data;
            if(res.code == "0" && flag){
                $(".toolBar").hide();
                $(".content").hide();
                new GP.PlayGround(msg);
            }
            room.setSeatStatus(s, false);
        }});
        conn.connect();
    },

    setSeatStatus: function(seat, empty){
        if(empty){
            seat.removeClass("seat-on");
        }else{
            seat.addClass("seat-on");
        }
    }
}