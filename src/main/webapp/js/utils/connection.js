/**
 * Created by pym on 2016/3/22.
 */
define(['jquery', 'utils/debug', "app/login"],
    function($, debug, login){
        var Connection = function(){
            this.listeners = {
                onSuccess : [], //@interface onSuccess (Connection conn, data)
                onError : [] //@interface onError (Connection conn, errorMessage)
            };

            this.baseProp = {
                dataType : 'json',
                cache : false,
                async : true,
                cObj: this
            };

            this.prop = $.extend({}, this.baseProp);

            this.fixedProp = {
                success : function (data, status) {
                    var conn = this.cObj;
                    try{
                        if (data.code === 0) {
                            var ls = conn.getListeners("onSuccess");
                            for (var i = 0; i < ls.length; i++) {
                                ls[i].onSuccess(conn, data);
                            }
                            conn.listeners.onSuccess = [];
                        } else if (data.code < 0) {
                            if (data.code == -1){
                                // login
                                login.reLogin();
                            }
                        }
                    } catch(err) {
                        debug.error(err);
                    }
                },
                error : function (request, status, error) {
                    var conn = this.cObj;
                    var ls = conn.getListeners("onError");
                    for (var i = 0; i < ls.length; i++) {
                        ls[i].onError(conn, status || error);
                    }
                    conn.listeners.onError = [];
                },
                complete : function (request) {
                    var conn = this.cObj;
                    try{
                        var ls = conn.getListeners("onComplete");
                        for (var i = 0; i < ls.length; i++) {
                            ls[i].onComplete(conn, request);
                        }
                    } catch(err) {
                        debug.error(err);
                    }

                    conn.listeners = {
                        onSuccess : [],
                        onError : []
                    };
                },
                beforeSend : function (xhr) { this.cObj.request = xhr; }
            };
        };

        Connection.prototype = {
            //@method public Connection add (String name, Object value)
            //@method public Connection add (Object o)
            add : function () {
                if (arguments.length == 1) {
                    $.extend(this.prop, arguments[0]);
                } else {
                    var param = arguments[1];
                    this.prop[arguments[0]] = param;
                }
                return this;
            },

            //@method public void connect ()
            connect : function () {
                $.extend(this.prop, this.fixedProp);
                $.ajax(this.prop);
                this.prop = $.extend({}, this.baseProp);
            },

            //@method public void abort ()
            abort : function () {
                if (this.request){
                    this.request.abort();
                }
                this.prop = $.extend({}, this.baseProp);
            },

            dispose : function(){
                this.abort();
                this.listeners = null;
                this.prop.cObj = null;
                this.baseProp.cObj = null;
            },

            //@method public Array getListeners (String type)
            getListeners : function (type) {
                if (!this.listeners[type]) {
                    this.listeners[type] = [];
                }
                return this.listeners[type];
            },

            //@method public void addListener (String type, Object listener)
            addListener : function (type, listener) {
                if (!this.listeners[type]) {
                    this.listeners[type] = [];
                }
                this.listeners[type].push(listener);
            },

            //@method public void removeListener (String type, Object listener)
            removeListener : function (type, listener) {
                var ls = this.listeners[type];

                if (!ls || ls.length == 0 ){
                    return;
                }
                for (var i = 0, len = ls.length; i < len; i++) {
                    if (listener == ls[i]) {
                        ls.splice(i, 1);
                        break;
                    }
                }
            }
        };

        return function(){
            return new Connection();
        };
    }
);
