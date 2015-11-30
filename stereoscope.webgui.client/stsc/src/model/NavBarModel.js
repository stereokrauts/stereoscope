/**
 * 
 */
sk.stsc.model.NavBarModel = Backbone.Model.extend({
	
	menuList: null,
	view: null,
	
	initialize: function(options) {
		try {
			if (options.dispatcher) {
				this.dispatcher = options.dispatcher;
			} else {
				throw 'Argument "dispatcher" missing.';
			}
		} catch(e) {
			var msg = 'Illegal NavBarModel object initialization: ' + e;
			throw new Error(msg);
		}
		this.menuList = new sk.stsc.model.AppMenuListModel({ 
			dispatcher: this.dispatcher });
		this.view = new sk.stsc.view.NavBarView({ model: this });
	}
	
});