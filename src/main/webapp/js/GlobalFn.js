/**
 * Created by lanfeng on 2015/10/7.
 */
var globalFn = {
    calPadding : function(){
        var width = $("body").width();
        var col = parseInt(width/132),
            padding = parseInt((width - col * 132)/2);
        return padding;
    },

    initWebSocket : function(){
        window.getWebSocket = (function(){
            var ws = new GP.WebSocket();
            return function(){
                return ws;
            }
        })();
    },

    restartWebSocket : function(){
        var ws = getWebSocket();
        if (ws) {
            ws.close();
        }
        this.initWebSocket();
    }
};
