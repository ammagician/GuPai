GP.globalFn.ns("GP.Util");
GP.Util.Debug = {
    info : function(msg) {
        if(GP.DEBUG && window.console){
            window.console.info(msg);
        }
    },
    log : function(msg) {
        if(GP.DEBUG && window.console){
            window.console.log(msg);
        }
    },
    warn : function(msg) {
        if(GP.DEBUG && window.console){
            window.console.warn(msg);
        }
    },
    error : function(msg) {
        if(GP.DEBUG && window.console){
            window.console.error(msg);
            window.console.log(msg.stack);
        }
    }
};