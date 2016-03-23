/**
 * Created by lanfeng on 2016/3/22.
 */
define(['jquery', 'utils/connection','utils/webSocket', "utils/globalFn"],
    function($, connFn, webSocket, globalFn){
        (function(){
            $("#loginDiv").hide();
            var bd = $("body");
            var t = bd.height()/2 - 100;
            var l = bd.width()/2 - 200;
            $("#loginContent").css({"left": l+"px", "top": t+"px"});

            $("#resetBtn").bind("click", function() {
                $("#userInput").val("");
                $("#pwdInput").val("");
            });

            $("#loginBtn").bind("click", function() {
                var user = $.trim($("#userInput").val());
                var pwd = $.trim($("#pwdInput").val());

                if(user == "" || pwd == ""){
                    return;
                }

                var msg = {
                    user:user,
                    pwd: pwd
                };

                var loginFlag = false;
                var conn = connFn();
                conn.add("url", "Login/LoginLogin.do").add({async:false, data:{data:JSON.stringify(msg)}});
                conn.addListener("onSuccess", {
                    onSuccess : function (conn, res) {
                        var code = res.code;
                        if(code == 0){
                            loginFlag = true;
                        }
                    }
                });
                conn.connect();

                if(loginFlag){
                    conn.dispose();
                    afterLogin();
                }
            });
        })();

        var isReLogin = false;

        var reLogin  = function(){
            isReLogin = true;
            $("#loginDiv").show();
        };

        var afterLogin  = function() {
            $("#loginDiv").hide();
            if (!isReLogin) {
                webSocket.restart();
                require(["app/hall"], function(hall){
                    hall.getRoomList("8ad28d3a522fd83a01522fe9678b0000");

                    $(".toolBar .navHall").bind("click", function (e) {
                        var id = $(this).attr("hallId");
                        if (hall.el.css("display") == "none" && id) {
                            if(hall.currentRoom){
                                hall.currentRoom.close();
                            }

                            hall.el.show();
                            hall.toolBar.find(".navRoom").addClass("none");
                        }
                    });
                });
            } else {
                isReLogin = false;
                webSocket.restart();

                $(".playground").empty().hide();
                $(".toolBar").show();
                $(".content").show();

                var roomDis = $(".toolBar").find(".navRoom").css("display");
                if (roomDis == "none") {
                    $(".content").find(".hallContent").show();
                } else {
                    $(".content").find(".roomContent").show();
                }
            }

            $(window).bind("unload", function(){
                if(webSocket){
                    webSocket.close();
                }
            });
        };

        var testLogin = function(){
            var loginFlag = false;
            var conn = connFn();
            conn.add("url", "Login/LoginTestLogin.do").add({async:false, data:{data:JSON.stringify({})}});
            conn.addListener("onSuccess", {
                onSuccess : function (conn, res) {
                    var code = res.code;
                    if(code == 0){
                        loginFlag = res.data.login;
                    }
                }
            });
            conn.connect();
            if(loginFlag){
                afterLogin();
            }else{
                $("#loginDiv").show();
            }

            return loginFlag;
        };


        return {
            reLogin: reLogin,
            testLogin: testLogin
        }
    }
);

