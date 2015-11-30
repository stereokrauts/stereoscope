/**
 * The standard statefulSelectBar view.
 * 
 * This control lets you choose one from different
 * stateful elements like aux, geq or others.
 * Unlike all other control views this is done in HTML
 * so stage objects are not in use here.
 */
define([
  'jquery',
  'underscore',
  'backbone'
], function($, _, Backbone) {

	var StatefulSelectBarView = Backbone.View.extend({

		model: null,
		anchorClass: null,

		events: {
			'click .stateful-select': 'handleStatefulSwitch'
		},


		initialize: function(options) {
			try {
				if (options.model) {
					this.model = options.model;
				} else {
					throw 'Argument "model" missing.';
				}
			} catch(e) {
				var msg = 'Illegal StatefulSelectBarView object initialization: ' + e;
				throw new Error(msg);
			}
			
			_.bindAll(this, 'handleStatefulSwitch');
			this.setupStatefulBar();

		},

		setupStatefulBar: function() {
			this.anchorClass = this.model.get('name').replace(/\s/g, '-');
			this.$el = $('<dl/>').addClass('stateful-select-bar');
			this.setSelectBarHtml();
		},

		handleStatefulSwitch: function(e) {
			console.log(e);
			var active = $(e.target.parentElement);
			var oscStr = e.currentTarget.attributes.value.nodeValue;
			$('.stateful-select').each(function(index) {
				$(this.parentElement).removeClass('active');
			});
			active.addClass('active');
			this.model.fireEvent(oscStr);
		},

		render: function() {
			$('#canvasHost').append(this.$el);
			this.setYPosition();
		},

		unrender: function() {
			this.$el.remove();
		},

		setSelectBarHtml: function() {
			var label = $('<dt/>').html(this.model.get('valueFromMixer'));
			this.$el.append(label);
			this.addItems(this.model.items);
		},

		addItems: function(items) {
			for (var i = 1; i <= items.length; i++) {
				var cnt = this.model.getTwoDigitNumber(i);
				var itemTag = $('<dd/>');
				var anchor = $('<a/>')
					.addClass('stateful-select', this.anchorClass)
					.attr('value', items[i - 1])
					.attr('href', '#').html(cnt);
					
				itemTag.append(anchor);
				if (i === 1) {
					itemTag.addClass('active');
				}
				this.$el.append(itemTag);
			}
		},

		setYPosition: function() {
			if (this.$el.height() >= 32) {
				var height = this.$el.height() / 3;
				this.$el.css('bottom', height);
			}
		},

		addElementsToStages: function() {
			// just here because of convention.
		}

	});

	return StatefulSelectBarView;
});