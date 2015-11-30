/**
 * This model is more convetions than function.
 * It's just a pass-through for the dispatcher.
 * 
 * TODO: Do we really need this?
 */
define([
  'jquery',
  'underscore',
  'backbone',
  'view/AppMenuListView'
], function($, _, Backbone, AppMenuListView) {

	var AppMenuListModel = Backbone.Model.extend({

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
			this.view = new AppMenuListView({ model: this });
		},

	});

	return AppMenuListModel;
});