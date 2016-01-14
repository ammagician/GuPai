/**
 * Created by lanfeng on 2015/10/7.
 */
ns("GP");

GP.Hall = function(){
    this.el = $(".hallContent");
    this.toolBar = $(".toolBar");
};

GP.Hall.prototype = {
    resize: function(){
        clearTimeout(this.resizeHallTimeout);
        this.resizeHallTimeout = setTimeout(function(){
            $(".hallContent").css("padding-left", calPadding() + "px");
        }, 100);
    },

    getRoomList : function(hallId){
        hallId = hallId || "402881ed5027289c01502728e6200000";
        this.toolBar.find(".navHall").attr("hallId", hallId);
        var ctr = this;
        var msg = {hallId: hallId};
        var conn = new GP.Util.Connection();
        conn.add("url", "Game/RoomGetRoomList.do").add({async:true, type: "GET", data:{data:$.toJSON(msg)}});
        conn.addListener("onSuccess", {
            onSuccess : function (conn, res) {
                var halls = res.data;
                halls.sort(function(a, b){
                    var ai = parseInt(a.name.substring(5, a.name.length));
                    var bi = parseInt(b.name.substring(5, b.name.length));
                    return ai - bi;
                });
                ctr.createRooms(halls);
            }
        });
        conn.connect();
    },

    createRooms: function(halls){
        halls = halls || [];
        var html = "";
        for(var i= 0,len=halls.length; i<len; i++){
            var hall = halls[i],
                id = hall.id,
                name = hall.name,
                available = hall.available;
            var str = "<div class='pointer tc roomItem fl w100 h160 m15' roomId='" + id + "'>" +
                "<div class='roomIcon w100 h100'></div>" +
                "<div class='roomName w100 h30 lh150'>" + name + "</div>" +
                //"<div class='roomMember w100 h30 lh150'>" + (available? "未满员": "满员") + "</div>" +
                "</div>";
            html += str;
        }

        var ctr = this;
        var hallContent = this.el;
        hallContent.empty();
        hallContent.append($(html));
        hallContent.bind("click", function(e){
            var target = $(e.target);
            if(target.hasClass("roomIcon")){
                var id = target.parent().attr("roomId");
                var name = target.parent().find(".roomName").html();
                ctr.createRoom(id, name);
            }
        });
    },

    createRoom: function(id, name){
        this.el.hide();
        if(this.room){
            this.room.close();
            this.room = null;
        }
        this.room = new GP.Room();
        this.room.getDeskList(id);
    }
}