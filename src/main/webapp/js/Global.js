/**
 * Created by lanfeng on 2015/10/7.
 */
var calPadding = function(){
    var width = $("body").width();
    var col = parseInt(width/132),
        padding = parseInt((width - col * 132)/2);
    return padding;
};