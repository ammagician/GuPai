/**
 * Created by lanfeng on 2015/10/7.
 */
ns("GP");
GP.Login = function(){
    this.isReLogin = false;
    this.init();
    this.testLogin();
};

GP.Login.prototype = {
    init: function(){
        $("#loginDiv").hide();
        var ctr = this;
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

            var conn = new GP.Util.Connection();
            conn.add("url", "Login/LoginLogin.do").add({async:true, data:{data:$.toJSON(msg)}});
            conn.addListener("onSuccess", {
                onSuccess : function (conn, res) {
                    var code = res.code;
                    if(code == 0){
                        ctr.afterLogin();
                    }
                }
            });
            conn.connect();
        });
    },

    reLogin : function(){
        this.isReLogin = true;
        $("#loginDiv").show();
    },

    afterLogin : function() {
        $("#loginDiv").hide();
        if (!this.isReLogin) {
            window.globalFn.initWebSocket();
            var hall = new GP.Hall();
            window.app.hall = hall;
            hall.el.css("padding-left", window.globalFn.calPadding() + "px");
            $(window).bind("resize", hall.resize);

            hall.getRoomList("8ad28d3a522fd83a01522fe9678b0000");

            $(".toolBar .navHall").bind("click", function (e) {
                var id = $(this).attr("hallId");
                if (hall.el.css("display") == "none" && id) {
                    window.app.hall.room.close();
                    hall.el.show();
                    hall.toolBar.find(".navRoom").addClass("none");
                }
            });
        } else {
            this.isReLogin = false;
            window.globalFn.restartWebSocket();

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
    },

    testLogin: function(){
        var ctr = this;
        var conn = new GP.Util.Connection();
        conn.add("url", "Login/LoginTestLogin.do").add({async:true, data:{data:$.toJSON({})}});
        conn.addListener("onSuccess", {
            onSuccess : function (conn, res) {
                var code = res.code;
                if(code == 0){
                    if(res.data.login){
                        ctr.afterLogin();
                    }else{
                        $("#loginDiv").show();
                    }
                }
            }
        });
        conn.connect();
    }
};

