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
        conn.add("url", "RoomGetRoomList.do").add({async:true, type: "GET", data:{data:$.toJSON(msg)}});
        conn.addListener("onSuccess", {onSuccess : function (conn, res) {
            var halls = res.data;
            ctr.createRooms(halls);
        }});
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
                "<div class='roomMember w100 h30 lh150'>" + (available? "未满员": "满员") + "</div>" +
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
                ctr.showRoom(id, name);
            }
        });
    },

    showRoom: function(id, name){
        this.toolBar.find(".navRoom").removeClass("none").attr("roomId", id).html(name);
        this.el.addClass("none");
        var room = new GP.Room();
        room.getDeskList(id);
    }
}