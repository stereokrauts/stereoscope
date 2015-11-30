/**
 * This control lets you choose one from different
 * stateful elements like aux, geq or others.
 * 
 */
define([
  'jquery',
  'underscore',
  'backbone',
  'commons',
  'model.controls/ControlModel',
  'view.controls/StatefulSelectBarView',
  'view.controls/StatefulSelectBoxView'
], function($, _, Backbone, commons, ControlModel,
		StatefulSelectBarView,
		StatefulSelectBoxView) {

	var StatefulSelectModel = ControlModel.extend({

		view: null,
		items: null,

		defaults: {
			oscValueType: 'boolean',
			textSize: 16,
			font: 'Arial'
		},

		initialize: function(options) {
			this._super(options);

			this.setDefaults(options.params);
			this.setCustomFeatures(this.get('type'),
					options.params.smallDevice);
		},

		setDefaults: function(params) {
			this.items = params.oscMsgList;
			this.set({ 
				'textSize': params.size });
			if (params.bgColor) {
				this.set('bgColor', this.setColor(params.bgColor));
			}
			if (!params.initValue) {
				this.set('valueFromMixer', " ");
			}
		},

		setCustomFeatures: function(controlType, isSmallDevice) {
			if (controlType === 'statefulSwitch') {
				if (isSmallDevice) {
					this.view = new StatefulSelectBoxView({
						model: this
					});
				} else {
					this.view = new StatefulSelectBarView({
						model: this,
					});
				}
			} else {
				throw new Error('Unsupported select bar type.');
			}
		},

		fireEvent: function(oscStr) {
			var request = commons.createRequestObject(oscStr, true);
			this.dispatcher.trigger('sendMessage', request);
		},
		
		getTwoDigitNumber: function(num) {
			if (num < 10) {
				return '0' + num;
			} else {
				return num;
			}
		}

	});

	return StatefulSelectModel;
});