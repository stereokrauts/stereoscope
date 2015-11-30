/**
 * This view renders a single 
 * list element with a layout name.
 */
define([
  'jquery',
  'underscore',
  'backbone'
], function($, _, Backbone) {

	var LOManagerItemView = Backbone.View.extend({

		model: null,
		tagName: 'li',
		className: 'layoutItem',
		parent: null,


		events: {
			'click .layoutLoadButton': 'setConfirmBox'
		},

		initialize: function(options) {
			try {
				if (options.model) {
					this.model = options.model;
				} else {
					throw 'Argument "model" and/or "parentId" missing.';
				}
			} catch(e) {
				var msg = 'Illegal LayoutItemView object initialization: ' + e;
				throw new Error(msg);
			}

			this.listenTo(this.model, 'active', this.render);
		},

		render: function(filter) {
			this.addLayoutClass();
			this.$el.html(this.addInnerHtml(
					this.model.get('name'),
					this.model.get('active')));
			return this;
		},

		setConfirmBox: function() {
			var result = confirm('Really load ' + this.model.get('fileName') + ' ?');
			if (result) {
				this.model.setArmed();
				return result;
			} else {
				this.model.deactivate();
				return result;
			}
		},

		addInnerHtml: function(itemName, active) {
			var html = this.addItem(itemName);
			if (active) {
				html += this.addActiveButton(); 
			} else {
				html += this.addLoadButton();
			}
			//html += this.addExportButton();
			return html;
		},

		addItem: function(itemName) {
			var html = '<div>'; 
			html += '<p class="layoutItemName">' + itemName + '</p>';
			html += '<p class="layoutDescription">Description: n.a.</p>';
			html += '<p class="layoutAuthor">Author: n.a.</p>';
			html += '</div>';
			return html;
		},

		addLoadButton: function(itemName) {
			return '<button class="layoutLoadButton">Load</button>';
		},

		addActiveButton: function() {
			return '<button class="layoutActiveButton">Active</button>';
		},

		addExportButton: function() {
			return '<button class="layoutExportButton">Export</button>';
		},

		addLayoutClass: function() {
			if (this.model.get('extension') === 'json' ||
				this.model.get('extension') === 'std' ||
					this.model.get('extension') === 'aux') {
				this.$el.addClass('stscLayout');
			} else if (this.model.get('extension') === 'touchosc') {
				this.$el.addClass('toscLayout');
			} else {
				return;
			}
		}

	});

	return LOManagerItemView;
});