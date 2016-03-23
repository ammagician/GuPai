/**
 * Created by lanfeng on 2016/3/22.
 */
define(["jquery"], function($){
    return {
        ns : function(str){
            str = str || "GP";
            var arr = str.split(".");
            var parent = GP;
            var index = arr[0] == "GP"? 1 : 0;
            for(var len=arr.length; index<len; index++){
                var a = arr[index];
                if(!parent[a]){
                    parent[a] = {};
                }
                parent = parent[a];
            }
        },

        loadScript : function(scripts, callback){
            var loadedCount = 0;
            var count = scripts.length;

            for(var i= 0,len=scripts.length; i<len; i++) {
                var script = document.createElement('script');
                script.setAttribute('type', 'text/javascript');
                script.setAttribute('src', scripts[i]);
                var head = document.getElementsByTagName('head')[0];
                head.appendChild(script);
                script.onload = function () {
                    ++loadedCount;
                    if (loadedCount >= count && callback) {
                        callback();
                    }
                }
            }
        },

        calPadding : function(){
            var width = $("body").width();
            var col = parseInt(width/132),
                padding = parseInt((width - col * 132)/2);
            return padding;
        },

        initCircleMap: function(position){
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
        },

        getID: (function(){
            var index = 0;
            return function(){
                var id = "GP_ID_" + index;
                index++;
                return id;
            }
        })()
    }
});
