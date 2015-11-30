sk.stsc.model.AppMenuListModel = Backbone.Model.extend({

	view: null,
	dispatcher: null,
	
	defaults: {
	
	},
	
	initialize: function(options) {
		try {
			if (options.dispatcher) {
				this.dispatcher = options.dispatcher;
			} else {
				throw 'Argument "view" and/or "dispatcher" missing.';
			}
		} catch(e) {
			var msg = 'Illegal AppMenuListModel object initialization: ' + e;
			throw new Error(msg);
		}
		this.view = new sk.stsc.view.AppMenuListView({ model: this });
	},
	
});