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
    },

    _initCircleMap: function(position){
        var circle = ["NORTH", "WEST", "SOUTH", "EAST"];
        var _circle_ = ["bottom", "right", "top", "left"];
        var cIndex = 0;
        var p = position;
        var circleMap = {};
        circleMap[p] = _circle_[cIndex];
        var index = circle.indexOf(p);
        for(var i=0;i<3;i++){
            index++;
            if(index == 4){
                index = 0;
            }
            ++cIndex;
            p = circle[index];
            circleMap[p] = _circle_[cIndex];
        }
        return circleMap;
    }
};
