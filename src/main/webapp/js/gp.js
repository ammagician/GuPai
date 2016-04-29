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
            require(["app/noSupport"], function(){});
        }
    }
);