/**
 * Here we render all layouts into a list.
 * 
 * @author jansen
 */
sk.stsc.view.LOManagerListView = Backbone.View.extend({
	
	model: null,
	
	initialize: function(options) {
		try {
			if (options.model) {
				this.model = options.model;
				steal.dev.log('LayoutListView initialized.');
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