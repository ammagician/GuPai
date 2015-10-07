ns("GP.Util");
GP.Util.Connection = function () {
	this.prop = {
		dataType : 'json',
		cache : false,
		async : true,
		cObj: this
	};
	this.fixedProp = {
		success : function (data, status) {
			var self = this.cObj;
			try{
				if (data.code === 0) {
					var ls = self.getListeners("onSuccess");
					for (var i = 0; i < ls.size(); i++) {
						ls.get(i).onSuccess(self, data);
					}
					self.listeners.onSuccess = null;
				} else if (data.code < 0) {
					if (data.code == -1){
						location.replace(data.data);
					} else {
						var ls = self.getListeners("onSystemError");
						for (var i = 0; i < ls.size(); i++) {
							ls.get(i).onSystemError(self, data);
						}
						self.listeners.onSystemError = null;
					}
				} else {
					var ls = self.getListeners("onAppError");
					for (var i = 0; i < ls.size(); i++) {
						ls.get(i).onAppError(self, data);
					}
					self.listeners.onAppError = null;
				}
			} catch(err) {
				GP.Util.Debug.error(err);
			}
		},
		error : function (request, status, error) {
			var self = this.cObj;
			var ls = self.getListeners("onError");
			for (var i = 0; i < ls.size(); i++) {
				ls.get(i).onError(self, status || error);
			}
			self.listeners.onError = null;
		},
		complete : function (request) {
			var self = this.cObj;
			try{
				var ls = self.getListeners("onComplete");
				for (var i = 0; i < ls.size(); i++) {
					ls.get(i).onComplete(self, request);
				}
			} catch(err) {
				GP.Util.Debug.error(err);
			}
			
			//dispose resource
			self.listeners = null;
			self.prop.cObj = null;
			self = null;
		},
		beforeSend : function (xhr) { this.cObj.request = xhr; }
	};
	this.listeners = {
		onSuccess : null, //@interface onSuccess (Connection conn, data)
		onSysError : null, //@interface onSysError (Connection conn, data)
		onAppError : null, //@interface onAppError (Connection conn, data)
		onError : null //@interface onError (Connection conn, errorMessage)
	};
};

GP.Util.Connection.prototype = {
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
	},
	
	//@method public void abort ()
	abort : function () { 
		if (this.request){
			this.request.abort(); 
		}	
	},
	
	//@method public void setClosure (Object closure)
	setClosure : function (closure) { this.closure = closure; },
	
	//@method public Object getClosure ()
	getClosure : function () { return this.closure; },
	
	//@method public Array getListeners (String type)
	getListeners : function (type) {
		if (!this.listeners[type]) this.listeners[type] = new GP.Util.ArrayList();
		return this.listeners[type];
	},
	
	//@method public void addListener (String type, Object listener)
	addListener : function (type, listener) {
		if (!this.listeners[type]) this.listeners[type] = new GP.Util.ArrayList();
		this.listeners[type].add(listener);
	},
	
	//@method public void removeListener (String type, Object listener)
	//@method public void removeListener (String type, int index)
	removeListener : function (type, listener) {
		if (!this.listeners[type]) return;
		if (isNaN(listener)) {
			this.listeners[type].removeObject(listener);
		} else {
			this.listeners[type].remove(listener);
		}
	}
};