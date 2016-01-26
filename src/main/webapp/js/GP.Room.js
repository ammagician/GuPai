/**
 * Created by lanfeng on 2015/10/7.
 */
GP.globalFn.ns("GP");

GP.Room = function(){
    this.el = $(".roomContent");
    this.toolBar = $(".toolBar");
    this.roomId = "";

    this._initMessageCallback();
    this.resize();
};

GP.Room.prototype = {
    _initMessageCallback: function(){
       var ws = getWebSocket();
       ws.addMessageCallback("sitSeat", this._onMessage, this);
       ws.addMessageCallback("leaveSeat", this._onMessage, this);
    },

    _onMessage: function(data){
        var eventType = data.eventType;
        switch(eventType){
            case "sitSeat" :
                this._onSitSeatMessage(data.data);
                break;
            case "leaveSeat" :
                this._onLeaveSeatMessage(data.data);
                break;
        }
    },

    _onSitSeatMessage: function(data){
        var deskId = data.deskId,
            p = data.position;
        this.setSeatStatus(deskId, p, false);
    },

    _onLeaveSeatMessage: function(data){
        var deskId = data.deskId,
            p = data.position;
        this.setSeatStatus(deskId, p, true);
    },

    resize: function(){
        clearTimeout(this.resizeRoomTimeout);
        this.resizeRoomTimeout = setTimeout(function(){
            $(".roomContent").css("padding-left", GP.globalFn.calPadding() + "px");
        }, 100);
    },

    getDeskList : function(roomId, roomName){
        this.roomId = roomId;
        var roomName = roomName;
        var ctr = this;
        var msg = {roomId: roomId};
        var conn = new GP.Util.Connection();
        conn.add("url", "Game/DeskGetDeskList.do").add({async:true, type: "GET", data:{data:$.toJSON(msg)}});
        conn.addListener("onSuccess", {onSuccess : function (conn, res) {
            var desks = res.data;
            if(res.code == "-1"){
                ctr._login();
            }else{
                ctr.toolBar.find(".navRoom").removeClass("none").attr("roomId", ctr.roomId).html(roomName);
                desks.sort(function(a, b){
                    var ai = parseInt(a.name.substring(5, a.name.length));
                    var bi = parseInt(b.name.substring(5, b.name.length));
                    return ai - bi;
                });
                ctr.createDesks(desks);
            }
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
        roomContent.empty().show();
        roomContent.append($(html));
        roomContent.unbind("click");
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

        this.el.css("padding-left", GP.globalFn.calPadding() + "px");

        $(window).unbind("resize", this.resize);
        $(window).bind("resize", this.resize);
    },

    _createDesk: function(desk){
        var id = desk.id,
            name = desk.name,
            available = desk.available,
            seats = desk.seats,
            status = {};
        for(var i= 0; i<4; i++){
            var s = seats[i];
            status[s.position.toLowerCase()] = {
                e:s.available? "1" : "0",
                on: s.available? "" : "seat-on"
            };
        }
        var str = "<div class='tc deskItem fl w100 h160 m15' deskId='" + id + "'>" +
            "<div class='deskIcon w100 h100 pr'>" +
                "<div class='pa pointer seat seat-n "+status["north"].on+"' pos='NORTH' seatEmpty='" +status["north"].e+ "'></div>" +
                "<div class='pa pointer seat seat-e "+status["east"].on+"' pos='EAST' seatEmpty='" +status["east"].e+ "'></div>" +
                "<div class='pa pointer seat seat-s "+status["south"].on+"' pos='SOUTH' seatEmpty='" +status["south"].e+ "'></div>" +
                "<div class='pa pointer seat seat-w "+status["west"].on+"' pos='WEST' seatEmpty='" +status["west"].e+ "'></div>" +
            "</div>" +
            "<div class='deskName w100 h30 lh150'>" + name + "</div>" +
            //"<div class='deskMember w100 h30 lh150'>" + (available? "未满员": "满员") + "</div>" +
            "</div>";
        return str;
    },

    sitSeat: function(deskId, deskName, position, seat){
        var ctr = this;
        var msg = {
            roomId: this.roomId,
            deskId: deskId,
            position: position
        };
        var s = seat;
        var room = this;
        var conn = new GP.Util.Connection();
        conn.add("url", "Game/DeskSitSeat.do").add({async:true, data:{data:$.toJSON(msg)}});
        conn.addListener("onSuccess", {onSuccess : function (conn, res) {
            var flag = res.data;
            if(res.code == "0"){
                if(flag){
                    $(".toolBar").hide();
                    $(".content").hide();
                    ctr.desk = new GP.PlayGround(msg);
                }else{
                    room.setSeatStatus(s, false);
                }
            }
        }});
        conn.connect();
    },

    setSeatStatus: function(deskId, position, empty){
        var el = this.el;
        var desk = el.find(".deskItem[deskId=" +deskId+ "]");
        if(desk){
            var seat = desk.find(".seat[pos=" + position + "]");
            if(empty){
                seat.removeClass("seat-on").attr("seatEmpty", "1");
            }else{
                seat.addClass("seat-on").attr("seatEmpty", "0");
            }
        }
    },

    close: function(){
        var ws = getWebSocket();
        ws.removeMessageCallback("sitSeat", this._onMessage);
        ws.removeMessageCallback("leaveSeat", this._onMessage);
        if(this.desk){
            this.desk.close();
        }
        this.el.empty().hide();
    }
};