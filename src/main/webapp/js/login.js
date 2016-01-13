/**
 * Created by lanfeng on 2015/10/7.
 */
$(document).ready(function(){
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
        conn.add("url", "LoginLogin.do").add({async:true, data:{data:$.toJSON(msg)}});
        conn.addListener("onSuccess", {
            onSuccess : function (conn, res) {
                var code = res.code;
                if(code == 0){
                    window.location = "hall.html";
                }
            }
        });
        conn.connect();
    });
});