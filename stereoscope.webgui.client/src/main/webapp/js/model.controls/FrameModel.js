/**
 * A frame model with an optional label for grouping elements.
 */
define([
  'jquery',
  'underscore',
  'backbone',
  'model.controls/ControlModel',
  'view.controls/FrameView'
], function($, _, Backbone, ControlModel, FrameView) {

	var FrameModel = ControlModel.extend({

		view: null,

		defaults: {
			hasLabel: false,
			hasBackground: false,
			hasEdge: false,
			bgColor: false,
			textSize: 20,
			font: 'Arial'
		},

		initialize: function(options) {
			this._super(options);

			this.setDefaults(options.params);
			this.setCustomFeatures(this.get('type'));

		},

		setDefaults: function(params) {
			this.set({
				'hasLabel': params.label,
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
			if (controlType === 'frame') {
				this.view = new FrameView({
					model: this
				});
			} else {
				throw new Error('Unsupported frame type.');
			}
		},


	});

	return FrameModel;
});