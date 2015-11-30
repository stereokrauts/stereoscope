sk.stsc.model.FrameModel = sk.stsc.model.ControlModel.extend({
	
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
		this._super('initialize', options);
		
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
			this.view = new sk.stsc.view.FrameView({
				model: this
			});
		} else {
			throw new Error('Unsupported frame type.');
		}
	},

	
});