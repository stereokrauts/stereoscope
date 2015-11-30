/**
 * The model for all labels in the mixer view.
 */
define([
  'jquery',
  'underscore',
  'backbone',
  'model.controls/ControlModel',
  'view.controls/LabelBasicView',
  'view.controls/LabelStaticView',
  'view.controls/LabelValueView'
], function($, _, Backbone, ControlModel,
		LabelBasicView, LabelStaticView, LabelValueView) {
	
	var LabelModel = ControlModel.extend({
	
	view: null,
	
	defaults: {
		oscValueType: 'string',
		hasBackground: false,
		hasEdge: false,
		bgColor: false,
		textSize: 12,
		font: 'Arial'
	},
	
	initialize: function(options) {
		this._super(options);
		
		this.setDefaults(options.params);
		this.setCustomFeatures(this.get('type'));
		
	},
	
	setDefaults: function(params) {
		this.set({
				'hasBackground': params.background,
				'hasEdge': params.edge,
				'textSize': params.size
		});
		if (params.bgColor) {
			this.set('bgColor', this.setColor(params.bgColor));
		}
		if (!params.initValue) {
			this.set('valueFromMixer', " ");
		}
	},
	
	setCustomFeatures: function(controlType) {
		if (controlType === 'staticLabel') {
			this.view = new LabelStaticView({
				model: this
			});
		} else if (controlType === 'valueLabel') {
			this.view = new LabelValueView({
				model: this
			});
		} else if (controlType === 'basicLabel') {
			this.view = new LabelBasicView({
				model: this
			});
		} else {
			throw new Error('Unsupported label type.');
		}
	},

});
	
	return LabelModel;
});