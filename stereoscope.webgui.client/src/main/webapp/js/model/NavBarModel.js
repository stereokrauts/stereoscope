/**
 * 
 */
define([
        'jquery',
        'underscore',
        'backbone',
        'view/NavBarView',
        'model/AppMenuListModel'
], function($, _, Backbone, NavBarView, AppMenuListModel) {

	var NavBarModel = Backbone.Model.extend({

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
			this.menuList = new AppMenuListModel({ 
				dispatcher: this.dispatcher });
			this.view = new NavBarView({ model: this });
		}

	});

	return NavBarModel;
});