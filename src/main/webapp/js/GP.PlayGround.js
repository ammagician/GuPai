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
        //this.svg = new GP.SVG();
        this.cardAnalysis = new GP.CardAnalysis();
        this.circleMap = window.globalFn._initCircleMap(this.position);
        this.readyCardsMap = {size:0, cards: {}};

        var ws = getWebSocket();
        ws.addMessageCallback("initPlayGround", this._onMessage, this);
        ws.addMessageCallback("sitSeat", this._onMessage, this);
        ws.addMessageCallback("leaveSeat", this._onMessage, this);
        ws.addMessageCallback("login", this._onMessage, this);
        ws.addMessageCallback("readyPlay", this._onMessage, this);
        ws.addMessageCallback("cancelReady", this._onMessage, this);
        ws.addMessageCallback("distributeCards", this._onMessage, this);

        this._initLayout();
        this._bindEvent();
        this.sendInitPlayGround();
        this.sendSitSeat(false);
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
                        "<div class='fl fullSize tc thc desk-left'>" +
                            "<div class='deskInfo fl'>" +
                                "<div class='fullHeight userInfo'></div>" +
                                "<div class='fullHeight winCards'></div>" +
                            "</div>" +
                            "<div class='leftCards fl'></div>" +
                        "</div>" +
                    "</div>" +
                    "<div class='fl fullHeight pg-content-center'>" +
                        "<div class='fullWidth tc thc desk-top'>" +
                            "<div class='deskInfo'>" +
                                "<div class='fullHeight fl userInfo'></div>" +
                                "<div class='fullHeight fl winCards'></div>" +
                            "</div>" +
                            "<div class='leftCards'></div>" +
                        "</div>" +
                        "<div class='fullWidth tc thc desk-center'>" +
                        "</div>" +
                        "<div class='fullWidth tc thc desk-bottom pr'>" +
                            "<div class='playBtns bc tc'>" +
                                "<button class='fl gp-btn readyBtn'>开始</button>" +
                                "<button class='fl gp-btn playCardBtn'>出</button>" +
                                "<button class='fl gp-btn passCardBtn'>过</button>" +
                            "</div>" +
                            "<div class='leftCards'>" +
                                //"<svg xmlns='http://www.w3.org/2000/svg' version='1.1' class='fullSize'></svg>" +
                            "</div>" +
                            "<div class='deskInfo'>" +
                                "<div class='fl fullHeight userInfo'></div>" +
                                "<div class='fl fullHeight winCards'></div>" +
                            "</div>" +
                        "</div>" +
                    "</div>" +
                    "<div class='fl fullHeight pg-content-right'>" +
                        "<div class='fr fullSize tc thc desk-right'>" +
                            "<div class='leftCards fl'></div>" +
                            "<div class='deskInfo fl'>" +
                                "<div class='fullHeight userInfo'></div>" +
                                "<div class='fullHeight winCards'></div>" +
                            "</div>" +
                        "</div>" +
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
        this.readyBtn = this.cavansEl.find(".readyBtn");
        this.playCardBtn = this.cavansEl.find(".playCardBtn");
        this.passCardBtn = this.cavansEl.find(".passCardBtn");
        this.exitBtn = this.el.find(".exit-icon");

        this.deskMap = {
            top: this.desk_top,
            right: this.desk_right,
            bottom: this.desk_bottom,
            left: this.desk_left,
            center: this.desk_center
        }
    },

    _bindEvent: function(){
        this.exitBtn.bind("click", {scope: this}, this._exitGame);
        this.readyBtn.bind("click", {scope: this}, this._readyPlayBtnClick);
        this.playCardBtn.bind("click", {scope: this}, this._playCards);
        this.passCardBtn.bind("click", {scope: this}, this._passCard);
    },

    _readyPlayBtnClick: function(e){

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

    sendReadyPlay: function(){
        var msg = {
            eventType: "readyPlay",
            data: this.msg
        };
        this.sendMessage(msg);
    },

    sendCancelReady: function(){
        var msg = {
            eventType: "cancelReady",
            data: this.msg
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
            case "readyPlay" :
                this._readyPlay(data.data);
            case "cancelReady" :
                this._cancelReady(data.data);
                break;
            case "distributeCards" :
                this._distributeCards(data.data);
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
                userId = s.userId,
                pp = this.circleMap[p];
            this.deskMap[pp].attr("userId", userId).find(".userInfo").html(userId);
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
        this.deskMap[pp].attr("userId", userId).find(".userInfo").html(userId);
    },

    _removeUser: function(d){
        var p = d.position;
        var pp = this.circleMap[p];
        this.deskMap[pp].attr("userId", "").find(".userInfo").html(p);
    },

    _readyPlay: function(d){
        var p = d.position;
        var pp = this.circleMap[p];
        this.deskMap[pp].html(p);
    },

    _cancelReady: function(d){
        var p = d.position;
        var pp = this.circleMap[p];
        this.deskMap[pp].html(p);
    },

    _distributeCards :function(d){
        console.info(d);
        var bottomCards = d[this.position];
        this._drawCards(bottomCards);
    },

    _exitGame: function(e){
        var pg = e.data.scope;
        pg.close(true);
    },

    _drawCards: function(cards){
        this.cards = {};
        var leftCards = this.desk_bottom.find(".leftCards");
        for(var i= 0, len=cards.length; i<len; i++) {
            var c = cards[i];
            var card = $("<div class='desk-card iBlock tc pointer'></div>");
            card.attr("cardId", c.id);
            card.attr("cardType", c.type);
            card.attr("cardValue", c.value);
            card.text(c.name);
            leftCards.append(card);
            this.cards[c.id] = card;
        }

        this._bindCardEvent();
    },

    _bindCardEvent: function(){
        var cards = this.desk_bottom.find(".leftCards");
        cards.bind("click", {scope:this}, this._readyCard);
    },

    _readyCard: function(e){
        var c = $(e.target),
            ctr = e.data.scope,
            cardId = c.attr("cardId");
        if(c.hasClass("desk-card-ready")){
            c.removeClass("desk-card-ready");
            ctr.readyCardsMap.size -= 1;
            delete ctr.readyCardsMap.cards[cardId];
        }else{
            c.addClass("desk-card-ready");
            ctr.readyCardsMap.size += 1;
            ctr.readyCardsMap.cards[cardId] = {
                id: cardId,
                type: c.attr("cardType"),
                value: parseInt(c.attr("cardValue"))
            };
        }
    },

    _playCards: function(e){
        var btn = $(e.target),
            ctr = e.data.scope,
            rcm = ctr.readyCardsMap;
        var result = ctr.cardAnalysis.cardType(rcm);
        if(!result){
            alert("Play Card Error!");
            return;
        }

        console.info(result.value);
        console.info(result.type)
    },

    _passCard: function(e){
        alert("Pass Card!");
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
        ws.removeMessageCallback("readyPlay", this._onMessage);
        ws.removeMessageCallback("cancelReady", this._onMessage);
        ws.removeMessageCallback("distributeCards", this._onMessage);

        if(leaveSeat){
            this.sendLeaveSeat();
        }

        this.el.empty().hide();
        $(".toolBar").show();
        $(".content").show();
    }
};