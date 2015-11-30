/**
 * There is no model behind this view.
 * It just renders an html list element
 * and fires an event when clicked.
 */
sk.stsc.view.AppMenuItemView = Backbone.View.extend({

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
		steal.dev.log('Event fired: ' + this.name);
	},
	
	
});