/**
 * 
 */
sk.stsc.view.NavBarView = Backbone.View.extend({
	
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
		this.mixerMenuButton = new sk.stsc.view.AppMenuMixerButtonView();
		this.render();
	},

	render: function(id, secName) {
		var parent = S(id);
		this.mixerMenuButton.render();
		this.model.menuList.view.el = $('#appMenuList');
		this.model.menuList.view.render();
	},
	
	changeTitle: function(titleTxt) {
		var txt = titleTxt.replace('_', ' ');
		$('#sectionTitle').html('<h1 class="title">' + txt + '</h1>');
	},
	
});