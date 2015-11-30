/**
 * This view renders the main app menu
 * (right side off-canvas menu).
 */
define([
  'jquery',
  'underscore',
  'backbone',
  'commons',
  'view/AppMenuItemView'
], function($, _, Backbone, commons, AppMenuItemView) {

	var AppMenuListView = Backbone.View.extend({

		model: null,
		dispatcher: null,

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
			_.each(commons.menu, function(item) {
				var view = new AppMenuItemView({
					dispatcher: this.model.dispatcher,
					name: item
				});
				$(this.el).append(view.render().el);
			}, this);

			return this;
		}

	});

	return AppMenuListView;
});
