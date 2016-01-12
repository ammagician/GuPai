/**
 * Created by lanfeng on 2016/1/12.
 */
ns("GP");

GP.PlayGround = function(deskId){
    this.deskId = deskId || "";
    this.init();
};

GP.PlayGround.prototype = {
    init: function(){
        this._initLayout();
        this._bindEvent();

        this._initWebSocket();
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

    _initWebSocket: function(){
        var webSocket = new WebSocket(constant.webSocket);
        var pg = this;
        if(webSocket != null){
            this.webSocket = webSocket;

            webSocket.onerror = function(event) {
                console.info("webSocket Error");
            };

            webSocket.onopen = function(event) {
                console.info("webSocket Connected");
            };

            webSocket.onmessage = function(event) {
                pg.onWebSocketMessage(event);
            };
        }
    },

    onWebSocketMessage: function(event){
        var data = $.parseJSON(event.data);
        var eventType = data.eventType;
        switch(eventType){
            case "initPlayGround" : this._initPlayGround(data.data);
                break;
        }
    },

    sendMessage: function(msg){
        if(this.webSocket){
            this.webSocket.send($.toJSON(msg));
        }
    },

    initPlayGround: function(){
        var msg = {
            eventType: "initPlayGround",
            deskId: this.deskId
        }
        this.sendMessage(msg);
    },

    _initPlayGround: function(data){
        console.info(data);
    },

    _exitGame: function(e){
        var pg = e.data.scope;
        pg.initPlayGround();
        return;
        if(pg.webSocket){
            pg.webSocket.close();
            pg.webSocket= null;
        }
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

}