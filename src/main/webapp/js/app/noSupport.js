/**
 * Created by pym on 2016/4/29.
 */
define(["jquery"], function($){
    var bd = $("body"),
        w = bd.width(),
        h = bd.height();
    var div = $("<div id='support' class='tc'>你的浏览器版本太低，请下载最新的版本。</div>");
    div.height(h);
    div.width(w);
    div.css({"line-height": h + "px", "color": "#fff"});
    $("body").empty().append(div);

    return null;
});