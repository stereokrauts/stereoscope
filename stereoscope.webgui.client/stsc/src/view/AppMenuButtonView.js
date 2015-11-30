/**
 * This adds the button for the main menu to the app.
 * It is so simple that it needs no model.
 */

sk.stsc.view.AppMenuButtonView = Backbone.View.extend({

	className: 'ui-state-default ui-corner-all',
	el: $('#appMenuButton'),
	menu: $('#appMenuList'),
	
	events: {
		'click	.toggleMenu':	'toggleMenu'
	},
	
	initialize: function() {
		$(sk.stsc.resources.img['appbarLines']).addClass('toggleMenu');
		this.render();
	},
	
	render: function() {
		this.$el.addClass(this.className);
		this.$el.append(sk.stsc.resources.img['appbarLines']);
		//this.$el.html('<span class="toggleMenu">Menu</span>');
		return this;
	},
	
	toggleMenu: function() {
		steal.dev.log('button clicked');
		this.menu.toggle('slide', {direction: 'right'});
	}

});