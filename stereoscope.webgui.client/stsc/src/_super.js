/**
 * This is a slightly modified gist by Max Brunsfeld:
 * https://gist.github.com/maxbrunsfeld/1542120
 * 
 * It provides a convenient way of calling "super".  
 */
;(function(Backbone) {

	// The super method takes two parameters: a method name
	// and an array of arguments to pass to the overridden method.
	//This is to optimize for the common case of passing 'arguments'.
	function _super(methodName, args) {

		//Keep track of how far up the prototype chain we have traversed,
		//in order to handle nested calls to _super.
		this._superCallObjects || (this._superCallObjects = {});
		var currentObject = this._superCallObjects[methodName] || this,
		parentObject = findSuper(methodName, currentObject);
		this._superCallObjects[methodName] = parentObject;

		//var result = parentObject[methodName].apply(this, args || []);
		//2013-03-17: modified to return attributes too (not just methods)
		var result = parentObject[methodName]; // Attribute. Maybe change the variable name.
		if (_.isFunction(result)) {
			result = result.apply(this, _.rest(arguments));
		}
		
		delete this._superCallObjects[methodName];
		return result;
	}

	//Find the next object up the prototype chain that has a
	//different implementation of the method.
	function findSuper(methodName, childObject) {
		var object = childObject;
		while (object[methodName] === childObject[methodName]) {
			object = object.constructor.__super__;
		}
		return object;
	}

	_.each(["Model", "Collection", "View", "Router"], function(klass) {
		Backbone[klass].prototype._super = _super;
	});

})(Backbone);