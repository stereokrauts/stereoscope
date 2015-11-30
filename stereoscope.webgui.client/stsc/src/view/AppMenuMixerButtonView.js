/**
 * 
 */
sk.stsc.view.AppMenuMixerButtonView = Backbone.View.extend({
	
	el: $('#menuMixerButton'),
	
	initialize: function(options) {
			this.icon = sk.stsc.resources.img['appbarMixerMenu'];
			$(this.icon).css('margin-left', '5px')
				.css('margin-top', '-3px');
	},
	
	render: function(sockStat) {
		this.$el.empty().append(this.icon);
	
	}
	
});