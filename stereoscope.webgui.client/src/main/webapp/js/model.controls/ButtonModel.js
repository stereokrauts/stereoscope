/**
 * The model for all buttons in the mixer view.
 */
define([
  'jquery',
  'underscore',
  'backbone',
  'model.controls/ControlModel',
  'view.controls/ButtonBasicView',
  'view.controls/ButtonStdView',
  'view.controls/ToggleButtonBasicView',
  'view.controls/ToggleButtonStdView'
], function($, _, Backbone, ControlModel,
		ButtonBasicView, ButtonStdView,
		ToggleButtonBasicView, ToggleButtonStdView) {

	var ButtonModel = ControlModel.extend({

		view: null,

		defaults: {
			oscValueType: 'boolean',
			oscMinValue: 0.0,
			oscMaxValue: 1.0,
			hasLabel: false
		},

		initialize: function(options) {
			this._super(options);

			options.params.label ? this.set('hasLabel', true) : this.set('hasLabel', false);
			this.setView(this.get('type'));

		},

		handleMessage: function(msg) {
			if (typeof msg.val === 'number') {
				(msg.val === 1) ? msg.val = true : msg.val = false;
			}
			steal.dev.log('received message ' + this.get('oscAddress') + ' val: ' + msg.val);
			this.set({valueFromMixer: msg.val, oscValue: msg.val});
		},

		setView: function(controlType) {
			if (controlType === 'basicButton') {
				this.view = new ButtonBasicView({
					model: this
				});
			} else if (controlType === 'stdButton') {
				this.view = new ButtonStdView({
					model: this
				});
			} else if (controlType === 'stdToggleButton') {
				this.view = new ToggleButtonStdView({
					model: this
				});
			} else if (controlType === 'basicToggleButton') {
				this.view = new ToggleButtonBasicView({
					model: this
				});
				/*
			} else if (controlType === 'subPageButton') {
				this.view = new SubPageButtonView({
					model: this
				});
				*/
			}

		}

	});

	return ButtonModel;
});