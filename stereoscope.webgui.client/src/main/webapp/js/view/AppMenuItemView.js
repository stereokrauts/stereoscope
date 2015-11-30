/**
 * This view renders the links in the
 * main app menu (right side off-canvas).
 * It doesn't have a model since there's
 * no data to handle.
 */
define([
  'jquery',
  'underscore',
  'backbone'
], function($, _, Backbone) {

	var AppMenuItemView = Backbone.View.extend({

		name: '',
		model: null,
		dispatcher: null,
		tagName: 'li',

		events: {
			'click	.appMenuItem':	'fireEvent'
		},

		initialize: function(options) {
			try {
				if (options.dispatcher && options.name) {
					this.dispatcher = options.dispatcher;
					this.name = options.name;
				} else {
					throw 'Argument "dispatcher" missing.';
				}
			} catch(e) {
				var msg = 'Illegal AppMenuItemView object initialization: ' + e;
				throw new Error(msg);
			}
			this.render();
		},

		render: function() {
			var linkTxt = this.name.replace('_', ' '); 
			this.$el.attr('id', 'menu' + this.name);
			this.$el.html('<a href="#" class="appMenuItem">' + linkTxt + '</a>');
			return this;
		},

		fireEvent: function() {
			this.dispatcher.trigger('swapContent', this.name);
			console.log('Event fired: ' + this.name);
		},

	});

	return AppMenuItemView;
});