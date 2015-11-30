/**
 * This view renders the button for the mixer menu.
 * It doesn't have a model since there is no data to handle.
 */
define([
  'jquery',
  'underscore',
  'backbone'
], function($, _, Backbone) {

	var AppMenuMixerButtonView = Backbone.View.extend({

		el: $('#menuMixerButton'),

		initialize: function(options) {
			this.icon = assets.img['appbar-mixer-menu'];
			$(this.icon).css('margin-left', '5px')
			.css('margin-top', '-3px');
		},

		render: function(sockStat) {
			this.$el.empty().append(this.icon);

		}

	});

	return AppMenuMixerButtonView;
});