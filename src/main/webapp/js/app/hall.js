/**
 * Created by lanfeng on 2015/3/22.
 */
define(['jquery', 'utils/connection',"utils/globalFn"],
    function($, connFn, globalFn){
        var el = $(".hallContent");
        var toolBar = $(".toolBar");
        var room = null;

        var createRoom = function(id, name){
            el.hide();
            if(room){
                room.close();
                room = null;
            }

            require(["app/room"], function(roomFn){
                room = roomFn();
                room.getDeskList(id);
            });

        };

        var createRooms = function(halls){
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

            var hallContent = el;
            hallContent.empty();
            hallContent.append($(html));
            hallContent.bind("click", function(e){
                var target = $(e.target);
                if(target.hasClass("roomIcon")){
                    var id = target.parent().attr("roomId");
                    var name = target.parent().find(".roomName").html();
                    createRoom(id, name);
                }
            });
        };

        var resize = function(){
            clearTimeout(this.resizeHallTimeout);
            this.resizeHallTimeout = setTimeout(function(){
                $(".hallContent").css("padding-left", globalFn.calPadding() + "px");
            }, 100);
        };

        var getRoomList  = function(hallId){
            hallId = hallId || "402881ed5027289c01502728e6200000";
            toolBar.find(".navHall").attr("hallId", hallId);
            var msg = {hallId: hallId};
            var conn = connFn();
            conn.add("url", "Game/RoomGetRoomList.do").add({async:false, type: "GET", data:{data:JSON.stringify(msg)}});
            conn.addListener("onSuccess", {
                onSuccess : function (conn, res) {
                    var halls = res.data;
                    halls.sort(function(a, b){
                        var ai = parseInt(a.name.substring(5, a.name.length));
                        var bi = parseInt(b.name.substring(5, b.name.length));
                        return ai - bi;
                    });
                    createRooms(halls);
                }
            });
            conn.connect();
            conn.dispose();
        };

        el.css("padding-left", globalFn.calPadding() + "px");
        $(window).bind("resize", resize);

        return {
            getRoomList: getRoomList,
            currentRoom: room
        }
    }
);
