sk.stsc.model.FaderModel = sk.stsc.model.ControlModel.extend({
	
	view: null,
	
	defaults: {
		oscValueType: 'float',
		oscMinValue: 0.0,
		oscMaxValue: 1.0,
		floatVal: 0.0,
		labelOscMsg: '',
		levelLabel: '-Inf',
		mode: 'normal'
		
	},
	
	initialize: function(options) {
		this._super('initialize', options);
		if (options.params.mode) {
			this.set('mode', options.params.mode);
		}
		this.setCustomFeatures(this.get('type'), options.params);
		_.bindAll(this, 'handleLevelLabelMsg');
	},
	
	getFaderKnobPosition: function(floatVal) {
		return(Math.round((this.get('height') - this.get('faderKnobHeight')) * (- (floatVal))));
	},
	
	setCustomFeatures: function(controlType, params) {
		
		if (controlType === 'basicFader') {
			this.view = new sk.stsc.view.FaderBasicView({
				model: this
			});
		} else if (controlType === 'volumeFader') {
			this.setLevelLabelOscMsg();
			this.bindLevelLabelMsg();
			this.view = new sk.stsc.view.FaderVolumeView({
				model: this
			});
			var self = this;
			this.set('labelOscMsg', params.labelOscMessage);
			this.dispatcher.bind(this.get('labelOscMsg'), 
					function(e) { self.handleLevelLabelMsg(e); });
		} else if (controlType === 'basicRotary') {
			this.view = new sk.stsc.view.RotaryBasicView({
				model: this
			});
		} else if (controlType === 'stdRotary') {
			this.view = new sk.stsc.view.RotaryStdView({
				model: this
			});
		} else {
			throw new Error('Unsupported fader type.');
		}
	},
	
	setLevelLabelOscMsg: function() {
		var parts = this.get('oscAddress').split('/');
		if (parts[2] === 'input') {
			this.set('levelLabelOscMsg', '/stereoscope/input/' + parts[3] + '/levelLabel');
		}
	},
	
	bindLevelLabelMsg: function() {
		var self = this;
		this.dispatcher.bind(this.get('levelLabelOscMsg'), 
				function(e) { self.handleLevelLabelMsg(e); });
	},
	
	handleLevelLabelMsg: function(msg) {
		this.set('levelLabel', msg.val);
	}
	
	
	
});
