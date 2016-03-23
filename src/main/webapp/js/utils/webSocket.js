/**
 * Created by pym on 2016/3/22.
 */
define(['jquery', 'utils/constant'],
    function($, constant){
        var webSocket;
        var messageCallbacks = {};
        var onWebSocketMessage = function (event) {
            var data = JSON.parse(event.data);
            var eventType = data.eventType;

            var cs = messageCallbacks[eventType] || [];
            for (var i = 0, len = cs.length; i < len; i++) {
                var item = cs[i],
                    fn = item.fn,
                    scope = item.scope;
                fn.apply(scope, [data]);
            }
        };

        var init = function(){
            var ws = new WebSocket(constant.webSocket);
            if (ws != null) {
                ws.onerror = function (event) {
                    console.info("webSocket Error");
                };

                ws.onopen = function (event) {
                    console.info("webSocket Connected");
                };

                ws.onmessage = function (event) {
                    onWebSocketMessage(event);
                };
            }

            return ws;
        };

        var sendMessage = function (msg) {
            if (webSocket) {
                webSocket.send(JSON.stringify(msg));
            }
        };

        var addMessageCallback = function (eventType, fn, scope) {
            var cs = messageCallbacks;
            if (!cs[eventType]) {
                cs[eventType] = [];
            }

            var fns = cs[eventType];
            fns.push({
                fn: fn,
                scope: scope
            });
        };

        var removeMessageCallback= function (eventType, fn) {
            var cs = messageCallbacks;
            if (!cs[eventType]) {
                cs[eventType] = [];
            }

            var fns = cs[eventType];
            for (var i = 0, len = fns.length; i < len; i++) {
                if (fn == fns[i].fn) {
                    fns.splice(i, 1);
                    break;
                }
            }
            if (fns.length == 0) {
                delete cs[eventType];
            }
        };

        var restart= function(){
            webSocket = init();
        };

        var close= function () {
            messageCallbacks = null;
            if (webSocket) {
                webSocket.close();
                console.info("webSocket close");
            }
        };

        return {
            sendMessage: sendMessage,
            addMessageCallback: addMessageCallback,
            removeMessageCallback: removeMessageCallback,
            restart: restart,
            close: close
        }
    }
);
