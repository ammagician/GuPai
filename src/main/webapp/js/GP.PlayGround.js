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
        this.tour = null;

        var ws = getWebSocket();
        ws.addMessageCallback("initPlayGround", this._onMessage, this);
        ws.addMessageCallback("sitSeat", this._onMessage, this);
        ws.addMessageCallback("leaveSeat", this._onMessage, this);
        ws.addMessageCallback("login", this._onMessage, this);
        ws.addMessageCallback("readyPlay", this._onMessage, this);
        ws.addMessageCallback("cancelReady", this._onMessage, this);
        ws.addMessageCallback("distributeCards", this._onMessage, this);
        ws.addMessageCallback("playCard", this._onMessage, this);
        ws.addMessageCallback("tourEnd", this._onMessage, this);

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
                            "<div class='desk-center-top tc fullWidth h33p'></div>" +
                            "<div class='desk-center-middle fullWidth h33p'>" +
                                "<div class='desk-center-left tc fullHeight w50p'></div>" +
                                "<div class='desk-center-right tc fullHeight w50p'></div>" +
                            "</div>" +
                            "<div class='desk-center-bottom fullWidth h33p tc'></div>" +
                        "</div>" +
                        "<div class='fullWidth tc thc desk-bottom pr'>" +
                            "<div class='playBtns bc tc'>" +
                                "<button class='gp-btn readyBtn' ready='0'>开始</button>" +
                                "<button class='none gp-btn playCardBtn'>出</button>" +
                                "<button class='none gp-btn passCardBtn'>垫</button>" +
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
        this.playCardBtn.bind("click", {scope: this}, this._playCancelBtnClick);
        this.passCardBtn.bind("click", {scope: this}, this._playCancelBtnClick);
    },

    _readyPlayBtnClick: function(e){
        var t = $(e.target),
            scope = e.data.scope,
            ready = t.attr("ready") == "0";
        scope.sendReadyPlay(ready);
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

    sendReadyPlay: function(ready){
        var msg = {
            eventType: ready? "readyPlay" : "cancelReady",
            data: {roomId: this.roomId, deskId: this.deskId}
        };
        this.sendMessage(msg);
    },

    sendPlayCard: function(data, pass){
        var d = $.extend({}, this.msg);
        d.cardsInfo = data;
        d.pass = pass;
        d.startPosition = this.tour.startPosition;
        var msg = {
            eventType: "playCard",
            data: d
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
                this._readyPlay(true);
                break;
            case "cancelReady" :
                this._readyPlay(false);
                break;
            case "distributeCards" :
                this._distributeCards(data.data);
                break;
            case "playCard" :
                this._playCard(data.data);
                break;
            case "tourEnd" :
                this._tourEnd(data.data);
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

    _readyPlay: function(ready){
        if(ready){
            this.readyBtn.attr("ready", "1").text("取消");
        }else{
            this.readyBtn.attr("ready", "0").text("开始");
        }
    },

    _distributeCards :function(d){
        var bottomCards = d[this.position];
        this._drawCards(bottomCards);

        this.readyBtn.addClass("none");
        this.passCardBtn.removeClass("none");
        this.playCardBtn.removeClass("none");

        if(this.position == "EAST"){
            this.playTurn = true;
            this.tour = {
                startPosition : this.position
            }
        }
    },

    _exitGame: function(e){
        var pg = e.data.scope;
        pg.close(true);
    },

    _drawCards: function(cards){
        this.cards = {};
        var leftCards = this.desk_bottom.find(".leftCards");
        var images = window.constant.cardImages;
        for(var i= 0, len=cards.length; i<len; i++) {
            var c = cards[i];
            var card = $("<div class='desk-card iBlock tc pointer'></div>");
            card.attr("cardId", c.id);
            card.attr("cardType", c.type);
            card.attr("cardValue", c.value);
            card.text(c.name);
            var image = images[c.id];
            card.css("background", "url(" + image + ") 0px 0px no-repeat");
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

    _playCancelBtnClick: function(e){
        var btn = $(e.target),
            ctr = e.data.scope,
            rcm = ctr.readyCardsMap;
        if(!ctr.playTurn){
            return;
        }

        var result = ctr.cardAnalysis.cardType(rcm);
        if(!result){
            new GP.UI.Tip({
                duration: 500,
                msg: "Play card error!"
            });
            return;
        }
        var pass = btn.hasClass(".passCardBtn");
        ctr.sendPlayCard(result, pass);

        console.info(result.value);
        console.info(result.comType);
        console.info(result.cardType);
    },

    _playCard: function(msg){
        this.playTurn = false;
        var position = msg.position,
            pass = msg.pass,
            startPosition = msg.startPosition,
            cardIds = msg.cardsInfo.cardIds,
            p = this.circleMap[position];
        if(position == this.position){
            for(var i= 0,len=cardIds.length; i<len; i++){
                var id = cardIds[i];
                this.cards[id].remove();
                delete this.cards[id];
            }
            this.readyCardsMap.size = 0;
            this.readyCardsMap.cards = {};
        }else{
            var cs = this.deskMap[p].find(".leftCards").find(".desk-card");
            for(var i=0; i<cardIds.length; i++){
                cs[i].remove();
            }
        }

        var rp = ".desk-center-" + p,
            el = this.deskMap.center.find(rp).empty();
        el.css("line-height", el.height() + "px");
        var images = window.constant.cardImages;
        for(var i= 0,len=cardIds.length; i<len; i++){
            var id = cardIds[i];
            var card = $("<div class='desk-card m0 iBlock tc'></div>");
            card.attr("cardId", id);
            card.text(pass? "" : id);
            if(!pass){
                var image = images[id];
                card.css("background", "url(" + image + ") 0px 0px no-repeat");
            }
            el.append(card);
        }

        if(this.tour){
            this.tour = {};
        }

        this.tour.startPosition = startPosition;

        var pc = window.constant.positionCircle;
        if(pc[position] == this.position && startPosition != this.position){
            this.playTurn = true;
        }
    },

    _tourEnd: function(data){
        var winPosition = data.winPosition;
        if(winPosition == this.position){
            this.playTurn = true;
        }

        //重置paiju
        this.tour = {};
        this.desk_center.find(".desk-center-top").empty();
        this.desk_center.find(".desk-center-left").empty();
        this.desk_center.find(".desk-center-right").empty();
        this.desk_center.find(".desk-center-bottom").empty();

        var cardIds = data.winCards,
            p = this.circleMap[winPosition],
            winCards = this.deskMap[p].find(".winCards");

        winCards.css("line-height", winCards.height() + "px");
        var layCss = (p == "left" || p == "right")? "desk-card-lay" : "";
        var images = window.constant.cardImages;
        for(var i= 0,len=cardIds.length; i<len; i++){
            var id = cardIds[i];
            var image = images[id + layCss? "_L": ""];
            var card = $("<div class='desk-card m0 iBlock tc "+ layCss +"'></div>");
            card.css("background", "url(" + image + ") 0px 0px no-repeat");
            card.attr("cardId", id);
            card.text(id);
            winCards.append(card);
        }
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
        ws.removeMessageCallback("playCard", this._onMessage);
        ws.removeMessageCallback("tourEnd", this._onMessage);

        if(leaveSeat){
            this.sendLeaveSeat();
        }

        this.el.empty().hide();
        $(".toolBar").show();
        $(".content").show();
    }
};