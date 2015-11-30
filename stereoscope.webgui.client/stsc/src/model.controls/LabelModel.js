sk.stsc.model.LabelModel = sk.stsc.model.ControlModel.extend({
	
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
		this._super('initialize', options);
		
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
			this.view = new sk.stsc.view.LabelStaticView({
				model: this
			});
		} else if (controlType === 'valueLabel') {
			this.view = new sk.stsc.view.LabelValueView({
				model: this
			});
		} else if (controlType === 'basicLabel') {
			this.view = new sk.stsc.view.LabelBasicView({
				model: this
			});
		} else {
			throw new Error('Unsupported label type.');
		}
	},

	
});