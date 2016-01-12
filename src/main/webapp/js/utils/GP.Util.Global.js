/**
 * Created by lanfeng on 2016/1/12.
 */
var GP = {};
var ns = function(str){
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
};

var loadScript = function(scripts, callback){
    var loadedCount = 0;
    var count = scripts.length;

    for(var i= 0,len=scripts.length; i<len; i++){
        var script = document.createElement('script');
        script.setAttribute('type', 'text/javascript');
        script.setAttribute('src', scripts[i]);
        var head = document.getElementsByTagName('head')[0];
        head.appendChild(script);
        script.onload = function(){
            ++loadedCount;
            if(loadedCount >= count && callback){
                callback();
            }
        }
    }
};
