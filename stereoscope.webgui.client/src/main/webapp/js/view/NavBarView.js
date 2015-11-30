/**
 * This view renders the top bar that shows
 * the title of the current section. 
 */
define([
  'jquery',
  'underscore',
  'backbone',
  'view/AppMenuMixerButtonView'
], function($, _, Backbone, AppMenuMixerButtonView) {

	var NavBarView = Backbone.View.extend({

		model: null,
		navBar: null,
		mixerMenuButton: null,

		initialize: function(options) {
			try {
				if (options.model) {
					this.model = options.model;
				} else {
					throw 'Argument "model" missing.';
				}
			} catch(e) {
				var msg = 'Illegal NavBarView object initialization: ' + e;
				throw new Error(msg);
			}
			this.mixerMenuButton = new AppMenuMixerButtonView();
			this.render();
		},

		render: function(id, secName) {
			var parent = $(id);
			this.mixerMenuButton.render();
			this.model.menuList.view.el = $('#appMenuList');
			this.model.menuList.view.render();
		},

		changeTitle: function(titleTxt) {
			var txt = titleTxt.replace('_', ' ');
			$('#sectionTitle').html('<h1 class="title">' + txt + '</h1>');
		},

	});

	return NavBarView;
});