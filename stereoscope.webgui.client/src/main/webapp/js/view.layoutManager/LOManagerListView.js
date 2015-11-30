/**
 * This view renders the names of all
 * available layouts into a list.
 * 
 * @author jansen
 */
define([
  'jquery',
  'underscore',
  'backbone'
], function($, _, Backbone) {

	var LOManagerListView = Backbone.View.extend({

		model: null,

		initialize: function(options) {
			try {
				if (options.model) {
					this.model = options.model;
					console.log('LayoutListView initialized.');
				} else { 
					throw 'Argument "model" missing.';
				}
			} catch(e) {
				var msg = 'Illegal LayoutListView object initialization: ' + e;
				throw new Error(msg);
			}

			_.bindAll(this, 'render');

			//binding this.render() to an event is crackbrained but neccessary
			this.model.layoutItems.bind('reset', this.render, this);
		},

		render: function() {
			this.$el.empty();
			this.model.layoutItems.each(function(item) {
				if (this.model.get('filter')) {
					if (this.model.get('filterType') === item.get('extension')) {
						item.view.render();
						this.$el.append(item.view.$el);
					}
				} else {
					item.view.render();
					this.$el.append(item.view.$el);
				}
				item.view.delegateEvents(); // restore events
			}, this);

			return this;
		},

		setListRoot: function(id) {
			this.model.set('listRoot', id);
		}

	});

	return LOManagerListView;
});