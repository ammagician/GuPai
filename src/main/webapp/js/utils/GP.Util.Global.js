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
