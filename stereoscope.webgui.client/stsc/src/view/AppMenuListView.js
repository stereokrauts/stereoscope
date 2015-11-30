sk.stsc.view.AppMenuListView = Backbone.View.extend({

	model: null,
	dispatcher: null,
	//el: $('#appMenuList'),
	
	initialize: function(options) {
		try {
			if (options.model) {
				this.model = options.model;
			} else {
				throw 'Argument "model" missing.';
			}
		} catch(e) {
			var msg = 'Illegal AppMenuListView object initialization. ' + e;
			throw new Error(msg);
		}
	},
	
	render: function() {
		_.each(statics.menu, function(item) {
			var view = new sk.stsc.view.AppMenuItemView({
				dispatcher: this.model.dispatcher,
				name: item
			});
			$(this.el).append(view.render().el);
		}, this);
		
		return this;
	}
	
});
