if (!PG) var PG = {};
if (!GP.Util) GP.Util = {};
if (!GP.Util.Debug) GP.Util.Debug = {};

GP.Util.Debug.log = function(msg) {
	if(PG.DEBUG && window.console){
		window.console.log(msg);
	}
};
GP.Util.Debug.warn = function(msg) {
	if(PG.DEBUG && window.console){
		window.console.warn(msg);
	}
};
GP.Util.Debug.error = function(msg) {
	if(PG.DEBUG && window.console){
		window.console.error(msg);
		window.console.log(msg.stack);
	}
};