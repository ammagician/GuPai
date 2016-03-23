/**
 * Created by pym on 2016/3/22.
 */
define(["utils/constant"],
    function(constant){
        var debug = constant.debug;
        return {
            info : function(msg) {
                if(debug && window.console){
                    window.console.info(msg);
                }
            },
            log : function(msg) {
                if(debug && window.console){
                    window.console.log(msg);
                }
            },
            warn : function(msg) {
                if(debug && window.console){
                    window.console.warn(msg);
                }
            },
            error : function(msg) {
                if(debug && window.console){
                    window.console.error(msg);
                    window.console.log(msg.stack);
                }
            }
        }
    }
);