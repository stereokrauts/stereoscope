sk.stsc.model.AppMenuButtonModel = Backbone.Model.extend({
	
	defaults: {
		view: null,
		dispatcher: null
	},
	
	initialize: function(options) {
		try {
			if (options.view && options.dispatcher) {
				this.view = options.view;
				this.dispatcher = options.dispatcher;
			} else {
				throw 'Argument "dispatcher" and/or "view" missing.';
			}
		} catch(e) {
			var msg = 'Illegal AppMenuButtonModel object initialization. ' + e;
			throw new Error(msg);
		}
	}
});