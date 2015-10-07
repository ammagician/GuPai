/**
 * Created by lanfeng on 2015/10/7.
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

var calPadding = function(){
    var width = $("body").width();
    var col = parseInt(width/132),
        padding = parseInt((width - col * 132)/2);
    return padding;
};