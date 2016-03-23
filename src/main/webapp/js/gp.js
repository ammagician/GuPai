/**
 * Created by pym on 2016/3/22.
 */

require.config({
    baseUrl: "./js",
    paths: {
        app: 'app',
        ui: 'ui',
        utils: 'utils',
        libs: 'libs',
        jquery: "libs/jquery-1.9.1.min"
    }
});

require(['jquery', 'app/login'],
    function($, login) {
        if(window.WebSocket){
            login.testLogin();
        }else{
            var bd = $("body"),
                w = bd.width(),
                h = bd.height();
            var div = $("<div id='support' class='tc'>你的浏览器版本太低，请下载最新的版本。</div>");
            div.height(h);
            div.width(w);
            div.css({"line-height": h + "px", "color": "#fff"});
            $("body").empty().append(div);
        }
    }
);