/**
 * Created by lanfeng on 2015/10/7.
 */
define(["jquery", "utils/webSocket", "utils/connection", "utils/globalFn"],
    function($, websocket, connFn, globalFn){
        var Room = function(roomId){
            this.hallDiv = $("#hallDiv");
            this.roomContent = this.hallDiv.find(".roomContent");
            this.toolBar = $("#toolBarDiv");
            this.roomId = roomId || "";

            this._initMessageCallback();
            this.resize(0);
        };

        Room.prototype = {
            _initMessageCallback: function(){
                var ws = websocket;
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

            resize: function(timeout){
                var room = this;
                clearTimeout(this.resizeRoomTimeout);
                this.resizeRoomTimeout = setTimeout(function(){
                    room.roomContent.css("padding-left", globalFn.calPadding() + "px");
                }, timeout || 100);
            },

            getDeskList : function(roomId, roomName){
                this.roomId = roomId;
                var roomName = roomName;
                var room = this;
                var msg = {roomId: roomId};
                var conn = connFn();
                conn.add("url", "Game/DeskGetDeskList.do")
                    .add({async:true, type: "GET", data:{data:JSON.stringify(msg)}});
                conn.addListener("onSuccess", {onSuccess : function (conn, res) {
                    var desks = res.data;
                    if(res.code == "-1"){
                        room._login();
                    }else{
                        room.toolBar.find(".navRoom").removeClass("none").attr("roomId", room.roomId).html(roomName);
                        desks.sort(function(a, b){
                            var ai = parseInt(a.name.substring(5, a.name.length));
                            var bi = parseInt(b.name.substring(5, b.name.length));
                            return ai - bi;
                        });
                        room.createDesks(desks);
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
                var roomContent = this.roomContent;
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

                roomContent.css("padding-left", globalFn.calPadding() + "px");

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
                var msg = {
                    roomId: this.roomId,
                    deskId: deskId,
                    position: position
                };
                var s = seat;
                var room = this;
                var conn = connFn();
                conn.add("url", "Game/DeskSitSeat.do")
                    .add({async:true, data:{data:JSON.stringify(msg)}});
                conn.addListener("onSuccess", {onSuccess : function (conn, res) {
                    var flag = res.data;
                    if(res.code == "0"){
                        if(flag){
                            room.hallDiv.hide();
                            require(["app/playGround"], function(playGroundFn){
                                room.desk = playGroundFn(msg);
                            });
                        }else{
                            room.setSeatStatus(s, false);
                        }
                    }
                }});
                conn.connect();
            },

            setSeatStatus: function(deskId, position, empty){
                var desk = this.roomContent.find(".deskItem[deskId=" +deskId+ "]");
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
                var ws = websocket;
                ws.removeMessageCallback("sitSeat", this._onMessage);
                ws.removeMessageCallback("leaveSeat", this._onMessage);
                if(this.desk){
                    this.desk.close();
                }
                this.roomContent.hide().empty();
            }
        };

        return function(id){
            return new Room(id);
        }
    }
);

