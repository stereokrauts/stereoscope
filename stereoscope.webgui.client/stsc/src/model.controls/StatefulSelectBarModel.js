/**
 * This control lets you choose one from different
 * stateful elements like aux, geq or others.
 * 
 */
sk.stsc.model.StatefulSelectBarModel = sk.stsc.model.ControlModel.extend({
	
	view: null,
	units: null,
	
	defaults: {
		oscValueType: 'boolean',
		textSize: 16,
		font: 'Arial'
	},
	
	initialize: function(options) {
		this._super('initialize', options);
		
		this.setDefaults(options.params);
		this.setCustomFeatures(this.get('type'));
	},

	setDefaults: function(params) {
		this.units = params.oscMsgList;
		this.set({ 
			'textSize': params.size });
		if (params.bgColor) {
			this.set('bgColor', this.setColor(params.bgColor));
		}
		if (!params.initValue) {
			this.set('valueFromMixer', " ");
		}
	},
	
	setCustomFeatures: function(controlType) {
		if (controlType === 'statefulSwitch') {
			this.view = new sk.stsc.view.StatefulSelectBarView({
				model: this,
			});
		} else {
			throw new Error('Unsupported select bar type.');
		}
	},
	
	fireEvent: function(oscStr) {
		var request = statics.createRequestObject(oscStr, true);
		this.dispatcher.trigger('sendMessage', request);
	}
	
	
});