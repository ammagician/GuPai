/**
 * Created by lanfeng on 2016/1/12.
 */
ns("GP");

GP.PlayGround = function(msg){
    this.msg = msg || {};
    this.roomId = msg.roomId || "";
    this.deskId = msg.deskId || "";
    this.position = msg.position || "";
    this.init();
};

GP.PlayGround.prototype = {
    init: function(){
        this.circleMap = window.globalFn._initCircleMap(this.position);

        var ws = getWebSocket();
        ws.addMessageCallback("initPlayGround", this._onMessage, this);
        ws.addMessageCallback("sitSeat", this._onMessage, this);
        ws.addMessageCallback("leaveSeat", this._onMessage, this);
        ws.addMessageCallback("login", this._onMessage, this);

        this._initLayout();
        this._bindEvent();
        this.sendSitSeat(false);
        this.sendInitPlayGround();
    },
    _initLayout: function(){
        this.el = $(".playground").show().empty();
        var h = this.el.height() - 30;
        var html = "<div>" +
                "<div class='playground-toolbar fullWidth h30'>" +
                    "<div class='pointer exit-icon fr mr20'>退出</div>" +
                "</div>" +
                "<div id='pgc' class='pr playground-content fullWidth'>" +
                    "<div class='fl fullHeight pg-content-left'>" +
                        "<div class='fl fullSize tc thc desk-left'>left</div>" +
                    "</div>" +
                    "<div class='fl fullHeight pg-content-center'>" +
                        "<div class='fullWidth tc thc desk-top'>top</div>" +
                        "<div class='fullWidth tc thc desk-center'>center</div>" +
                        "<div class='fullWidth tc thc desk-bottom'>bottom</div>" +
                    "</div>" +
                    "<div class='fl fullHeight pg-content-right'>" +
                        "<div class='fr fullSize tc thc desk-right'>right</div>" +
                    "</div>" +
                    "<div class='cb'></div>" +
                "</div>" +
            "</div>";
        this.el.append($(html));
        this.cavansEl = this.el.find("#pgc").height(h);
        this.desk_top = this.cavansEl.find(".desk-top");
        this.desk_right = this.cavansEl.find(".desk-right");
        this.desk_bottom = this.cavansEl.find(".desk-bottom");
        this.desk_left = this.cavansEl.find(".desk-left");
        this.desk_center = this.cavansEl.find(".desk-center");

        this.deskMap = {
            top: this.desk_top,
            right: this.desk_right,
            bottom: this.desk_bottom,
            left: this.desk_left,
            center: this.desk_center
        }
    },

    _bindEvent: function(){
        this.el.find(".exit-icon").bind("click", {scope: this}, this._exitGame);
    },

    sendSitSeat: function(){
        var msg = {
            eventType: "sitSeat",
            data: this.msg
        };
        this.sendMessage(msg);
    },
    sendLeaveSeat: function(){
        var msg = {
            eventType: "leaveSeat",
            data: this.msg
        };
        this.sendMessage(msg);
    },

    sendInitPlayGround: function(){
        var msg = {
            eventType: "initPlayGround",
            data: {roomId: this.roomId, deskId: this.deskId}
        };
        this.sendMessage(msg);
    },

    sendMessage: function(msg){
        var ws = getWebSocket();
        if(ws && ws.webSocket){
            ws.webSocket.send($.toJSON(msg));
        }
    },

    _onMessage: function(data){
        var eventType = data.eventType;
        switch(eventType){
            case "initPlayGround" :
                this._initPlayGround(data.data);
                break;
            case "sitSeat" :
                this._sitSeat(data.data);
                break;
            case "leaveSeat" :
                this._leaveSeat(data.data);
                break;
            case "login" :
                this._login();
                break;
        }
    },

    _login: function(){
        this.close();
        window.app.loginObj.reLogin();
    },

    _initPlayGround: function(data){
        console.info(data);
        var ss = data.desk.seats;
        for(var i= 0; i<4; i++){
            var s = ss[i],
                p = s.position,
                userId = s.userId || p,
                pp = this.circleMap[p];
            this.deskMap[pp].attr("userId", userId).html(userId);
        }
    },

    _sitSeat: function(data){
        var deskId = data.deskId;
        if(deskId != this.deskId){
            return;
        }

        if(data.userId){
            this._addUser(data);
        }
    },

    _leaveSeat: function(data){
        var deskId = data.deskId;
        if(deskId != this.deskId){
            return;
        }
        this._removeUser(data);
    },

    _addUser: function(d){
        var p = d.position,
            userId = d.userId;
        var pp = this.circleMap[p];
        this.deskMap[pp].attr("userId", userId).html(userId);
    },

    _removeUser: function(d){
        var p = d.position;
        var pp = this.circleMap[p];
        this.deskMap[pp].attr("userId", "").html(p);
    },

    _exitGame: function(e){
        var pg = e.data.scope;
        pg.close(true);
    },

    resize: function(){
        clearTimeout(this.resizeRoomTimeout);
        this.resizeRoomTimeout = setTimeout(function(){
            console.info("resize");
        }, 100);
    },

    close: function(leaveSeat){
        var ws = getWebSocket();
        ws.removeMessageCallback("initPlayGround", this._onMessage);
        ws.removeMessageCallback("sitSeat", this._onMessage);
        ws.removeMessageCallback("leaveSeat", this._onMessage);

        if(leaveSeat){
            this.sendLeaveSeat();
        }

        this.el.empty().hide();
        $(".toolBar").show();
        $(".content").show();
    }
};