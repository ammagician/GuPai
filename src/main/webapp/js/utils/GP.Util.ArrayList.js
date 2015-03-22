if (!GP) var GP = {};
if (!GP.Util) GP.Util = {};
GP.Util.ArrayList = function () {
	this.listStore = [];	
};
GP.Util.ArrayList.prototype = {
		//@method public void add (Object o)
		//@method public void add (Object o, int index)
		add : function (o, index) {
			this.addAll([o], index);	
		},
		
		//@method public void addAll (Array collection)
		//@method public void addAll (Array collection, int index)
		addAll : function (collection, index) {
			if (!this.isIndex(index)) {
				index = this.size();
			}	
			if (index < 0 || index > this.size()) {
				throw new Error ("IndexOutOfBounds:" + index + " is not between 0 and " + this.size());	
			}	
			if (!(collection instanceof Array)) {
				throw new Error ("InvalidArgument:" + collection);	
			}
			var i, j, size = collection.length;
			for (i = this.listStore.length - 1; i >= index; i--) {
				this.listStore[i+size] = this.listStore[i];	
			}
			for (j = 0; j < size; j++) {
				this.listStore[++i] = collection[j];
			}
		}, 
		
		//@method public void clear ()
		clear : function () {
			this.listStore = [];	
		},
		
		//@method public boolean contains (Object o)
		contains : function (o) {
			//return this.indexOf(o) > -1;
			return glb_array_indexOf(o,this.listStore, true);
		},
		

		//@method public Object get (int index)
		get : function (index) {
			if (!this.isIndex(index)) {
				throw new Error ("InvalidArgument:" + index);	
			}	
			if (index < 0 || index >= this.size()) {
				throw new Error ("IndexOutOfBounds:" + index + " not between 0 and " + (this.size() - 1));	
			}
			return this.listStore[index];
		},
	
		//@method public int indexOf (Object o)
		indexOf : function (o) {
			var o1;
			if (o && (typeof o.equals == 'function')) {
				for (var i = 0; i < this.listStore.length; i++) {
					o1 = this.listStore[i];
					if (o.equals(o1)) return i;
				}	
			} else {
				for (var i = 0; i < this.listStore.length; i++) {
					o1 = this.listStore[i];
					if (o == o1) return i;
				}		
			}
			return -1;
		},
		
		//@method public boolean isEmpty ()
		isEmpty : function () {
			return this.listStore.length == 0;	
		},
		
		//@method public int lastIndexOf (Object o)
		lastIndexOf : function (o) {
			var o1;
			if (o && (typeof o == 'function')) {
				for (var i = this.listStore.length - 1; i >= 0; i--) {
					o1 = this.listStore[i];
					if (o.equals(o1)) return i;
				}	
			} else {
				for (var i = this.listStore.length - 1; i >= 0; i--) {
					o1 = this.listStore[i];
					if (o == o1) return i;
				}		
			}
			return -1;
		},
		
		//@method public void remove (int index)
		remove : function (index) {
			if (!this.isIndex(index)) {
				throw new Error ("InvalidArgument:" + index);	
			}	
			if (index < 0 || index >= this.size()) {
				throw new Error ("IndexOutOfBounds:" + index + " is not between 0 and " + (this.size() - 1));	
			}
			if (index > this.size() / 2) {
				for (var i = index, n = this.size() - 1; i < n; i++) {
					this.listStore[i] = this.listStore[i+1];
				}
				this.listStore.pop();
			} else {
				for (var i = index; i > 0; i--) {
					this.listStore[i] = this.listStore[i-1];	
				}
				this.listStore.shift();
			}
			
		},
		
		//@method public void removeObject (Object o)
		removeObject : function (o) {
			//this.remove(this.indexOf(o));	
			this.remove(glb_array_indexOf(o,this.listStore,true));	
		},
		

		//@method public void set (Object o, int index)
		set : function (o, index) {
			if (!this.isIndex(index)) {
				throw new Error ("InvalidArgument:" + index);	
			}	
			if (index < 0 || index >= this.size()) {
				throw new Error ("IndexOutOfBounds:" + index + " is not between 0 and " + (this.size() - 1));	
			}
			this.listStore[index] = o;
		},
		
		//@method public int size ()
		size : function () {
			return this.listStore.length;
		},
		
		//@method public Array toArray ()
		toArray : function () {
			var arr = [];
			for (var i = 0; i < this.listStore.length; i++) {
				arr[i] = this.listStore[i];	
			}
			return arr;
		},
		
		//@method public boolean isIndex (Object value)
		isIndex : function (value) {
			if (isNaN(value)) return false;
			if ((value + "").indexOf(".") >= 0) return false;
			if (value < 0) return false;
			return true;
		}
};