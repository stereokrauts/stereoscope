/**
 * StatefulSelectBoxView:
 * A control that comes to use on
 * devices that are to small to handle
 * a StatefulSelectBar. 
 */
define([
  'jquery',
  'underscore',
  'backbone'
], function($, _, Backbone) {

	var StatefulSelectBoxView = Backbone.View.extend({

		model: null,
		selectId: null,
		optionClass: null,
		
		events: function() {
			var _events = [];
			_events['change ' + '#' + this.selectId] = 'handleStatefulSwitch';
			return _events;
		},
		
		initialize: function(options) {
			try {
				if (options.model) {
					this.model = options.model;
				} else {
					throw 'Argument "model" missing.';
				}
			} catch(e) {
				var msg = 'Illegal StatefulSelectBoxView object initialization: ' + e;
				throw new Error(msg);
			}
			this.setupSelectBox();
			_.bindAll(this, 'handleStatefulSwitch');
			
		},
		
		setupSelectBox: function() {
			this.selectId = this.model.get('oscAddress').replace(/\//g, '-');
			this.optionClass = this.model.get('name').replace(/\s/g, '-');
			this.setEl();
			this.setStatefulBoxHtml();
		},
		
		setEl: function() {
			this.$el = $('<form/>')
				.addClass('stateful-select-box')
				.css('position', 'absolute')
				.css('bottom', '50px')
				.css('z-index', '100');
		},
		
		setStatefulBoxHtml: function() {
			var label = $('<span/>').html(this.model.get('valueFromMixer'));
			var select = $('<select/>').attr('id', this.selectId).css('width', '70px');
			this.$el.append(label, select);
			this.addSelectItems(select, this.model.items);
		},
		
		handleStatefulSwitch: function(e) {
			//console.log(e);
			this.model.fireEvent(e.currentTarget.value);
		},
		
		render: function() {
			$('#canvasHost').append(this.$el);
		},
		
		unrender: function() {
			this.$el.remove();
		},
		
		addSelectItems: function(parent, items) {
			for (var i = 1; i <= items.length; i++) {
				var cnt = this.model.getTwoDigitNumber(i);
								
				var option = $('<option/>')
					.addClass('stateful-select').addClass(this.optionClass)
					.attr('value', items[i - 1])
					.html(cnt);
				parent.append(option);
			}
		},
		
		getTwoDigitNumber: function(num) {
			if (num < 10) {
				return '0' + num;
			} else {
				return num;
			}
		},
		
		addElementsToStages: function() {
			// just here because of convention.
		}
	
	});
	
	return StatefulSelectBoxView;
});