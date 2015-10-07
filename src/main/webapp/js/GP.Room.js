/**
 * Created by lanfeng on 2015/10/7.
 */
ns("GP");

GP.Room = function(){
    this.el = $(".roomContent");
    this.toolBar = $(".toolBar");
};

GP.Room.prototype = {
    resize: function(){
        clearTimeout(this.resizeRoomTimeout);
        this.resizeRoomTimeout = setTimeout(function(){
            $(".roomContent").css("padding-left", calPadding() + "px");
        }, 100);
    },

    getDeskList : function(roomId){
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
            var desk = desks[i],
                id = desk.id,
                name = desk.name,
                available = desk.available;
            var str = "<div class='pointer tc deskItem fl w100 h160 m15' deskId='" + id + "'>" +
                "<div class='deskIcon w100 h100'></div>" +
                "<div class='deskName w100 h30 lh150'>" + name + "</div>" +
                "<div class='deskMember w100 h30 lh150'>" + (available? "未满员": "满员") + "</div>" +
                "</div>";
            html += str;
        }

        var roomContent = this.el;
        roomContent.removeClass("none");
        roomContent.empty();
        roomContent.append($(html));
        roomContent.bind("click", function(e){
            var target = $(e.target);
            if(target.hasClass("deskIcon")){
                var id = target.parent().attr("deskId");
                var name = target.parent().find(".deskName").html();
                console.info(id);
            }
        });

        this.el.css("padding-left", calPadding() + "px");

        $(window).unbind("resize", this.resize);
        $(window).bind("resize", this.resize);
    }
}