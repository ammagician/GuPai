/**
 * Created by pym on 2016/1/12.
 */
ns("GP");
GP.WebSocket = function(){
    this.init();
    this.messageCallbacks = {};
};

GP.WebSocket.prototype = {
    init: function(){
        var webSocket = new WebSocket(constant.webSocket);
        var ws = this;
        if(webSocket != null){
            ws.webSocket = webSocket;

            webSocket.onerror = function(event) {
                console.info("webSocket Error");
            };

            webSocket.onopen = function(event) {
                console.info("webSocket Connected");
            };

            webSocket.onmessage = function(event) {
                ws.onWebSocketMessage(event);
            };
        }
    },

    onWebSocketMessage: function(event){
        var data = $.parseJSON(event.data);
        var eventType = data.eventType;

        var cs = this.messageCallbacks[eventType] || [];
        for(var i= 0,len=cs.length; i<len; i++){
            var item = cs[i],
                fn = item.fn,
                scope = item.scope;
            fn.apply(scope, [data]);
        }
    },

    sendMessage: function(msg){
        if(this.webSocket){
            this.webSocket.send($.toJSON(msg));
        }
    },

    addMessageCallback: function(eventType, fn, scope){
        var cs = this.messageCallbacks;
        if(!cs[eventType]){
            cs[eventType] = [];
        }

        var fns = cs[eventType];
        fns.push({
            fn: fn,
            scope: scope
        });
    },

    removeMessageCallback: function(eventType, fn){
        var cs = this.messageCallbacks;
        if(!cs[eventType]){
            cs[eventType] = [];
        }

        var fns = cs[eventType];
        for(var i= 0,len=fns.length; i<len; i++){
            if(fn == fns[i].fn){
                fns.splice(i, 1);
                --i;
                --len;
                break;
            }
        }
        if(fns.length == 0){
            delete cs[eventType];
        }
    },

    close: function(){
        this.messageCallbacks = null;
        if(this.webSocket){
            this.webSocket.close();
        }
    }
}