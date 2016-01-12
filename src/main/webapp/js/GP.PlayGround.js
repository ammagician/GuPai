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
        var ws = getWebSocket();
        ws.addMessageCallback("initPlayGround", this._onMessage, this);

        this._initLayout();
        this._bindEvent();
        this.sendSitSeat(true);
        this.sendInitPlayGround();
    },
    _initLayout: function(){
        this.el = $(".playground").show();
        var h = this.el.height() - 30;
        var html = "<div>" +
                "<div class='playground-toolbar fullWidth h30'>" +
                    "<div class='pointer exit-icon fr mr20'>退出</div>" +
                "</div>" +
                "<div id='pgc' class='playground-content fullWidth'></div>" +
            "</div>";
        this.el.append($(html));
        this.cavansEl = this.el.find("#pgc").height(h);
    },

    _bindEvent: function(){
        this.el.find(".exit-icon").bind("click", {scope: this}, this._exitGame);
    },

    sendSitSeat: function(empty){
        var msg = {
            eventType: "sitSeat",
            data: $.extend(true, {empty:empty}, this.msg)
        };
        this.sendMessage(msg);
    },

    sendInitPlayGround: function(){
        var msg = {
            eventType: "initPlayGround",
            deskId: this.deskId
        };
        this.sendMessage(msg);
    },

    sendMessage: function(msg){
        var ws = getWebSocket();
        if(ws){
            ws.send($.toJSON(msg));
        }
    },

    _onMessage: function(data){
        var eventType = data.eventType;
        switch(eventType){
            case "initPlayGround" : this._initPlayGround(data.data);
                break;
        }
    },

    _initPlayGround: function(data){
        console.info(data);
    },

    _exitGame: function(e){
        var pg = e.data.scope;
        pg.sendSitSeat(false);
        pg.el.hide();
        $(".toolBar").show();
        $(".content").show();
    },

    resize: function(){
        clearTimeout(this.resizeRoomTimeout);
        this.resizeRoomTimeout = setTimeout(function(){
            console.info("resize");
        }, 100);
    }
};