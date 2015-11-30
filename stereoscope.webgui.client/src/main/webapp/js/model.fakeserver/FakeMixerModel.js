/**
 * This model simulates the mixer.
 * That's basically holding values
 * for mixer properties and simulating
 * basic activity.
 */
define([ 'underscore', 'backbone', 'commons', 'model.fakeserver/FakeMixerLabelProvider' ],
		function(_, $, commons, FakeMixerLabelProvider) {
	
	var FakeMixerModel = Backbone.Model.extend({
		
		labels: null,
		mixerParameters: {},
		
		defaults: {
			mixerName: 'Demo Mixer',
			inputChannelCount: 48,
			outputAuxCount: 8,
			outputBusCount: 8,
			outputGeqCount: 8,
			outputCount: 2,
			geqBandCount: 31,
		},
		
		initialize: function() {
			this.labels = new FakeMixerLabelProvider();
			this.setAllRandomParameters();
		},
		
		setAllRandomParameters: function() {
			this.setRandomInputParameters();
			this.setRandomOutputParameters();
		},
		
		setRandomInputParameters: function() {
			var chn = this.get('inputChannelCount');
			this.setRandomMixerParameters(commons.osc.INPUT_CHANNELON, 'boolean', chn);
			this.setRandomMixerParameters(commons.osc.INPUT_LEVEL, 'number', chn);
			this.setRandomMixerParameters(commons.osc.INPUT_LEVELLABEL, 'string', chn);
			this.setRandomMixerParameters(commons.osc.INPUT_LABEL, 'string', chn);
			this.setRandomMixerParameters(commons.osc.INPUT_PAN, 'number', chn);
			
			this.setRandomInputPeqParameters();
			this.setRandomInputDynamicsParameters();
			//this.setRandomInputSendParameters();
		},
		
		setRandomInputPeqParameters: function(chn) {
			var bands = 4;
			this.setRandomMixerParameters(commons.osc.INPUT_PEQ_ON, 'number', chn);
			this.setRandomMixerParameters(commons.osc.INPUT_PEQ_ONLABEL, 'string', chn);
			this.setRandomMixerParameters(commons.osc.INPUT_PEQ_MODE, 'number', chn);
			this.setRandomMixerParameters(commons.osc.INPUT_PEQ_MODELABEL, 'string', chn);
			this.setRandomMixerParameters(commons.osc.INPUT_PEQ_Q, 'number', chn, bands);
			this.setRandomMixerParameters(commons.osc.INPUT_PEQ_F, 'number', chn, bands);
			this.setRandomMixerParameters(commons.osc.INPUT_PEQ_G, 'number', chn, bands);
			this.setRandomMixerParameters(commons.osc.INPUT_PEQ_QLABEL, 'string', chn, bands);
			this.setRandomMixerParameters(commons.osc.INPUT_PEQ_FLABEL, 'string', chn, bands);
			this.setRandomMixerParameters(commons.osc.INPUT_PEQ_GLABEL, 'string', chn, bands);
		},
		
		setRandomInputDynamicsParameters: function(chn) {
			for (var i = 1; i <= 2; i++) {
				this.setRandomMixerParameters(commons.osc.INPUT_DYNA_ON, 'number', chn, i);
				this.setRandomMixerParameters(commons.osc.INPUT_DYNA_ONLABEL, 'string', chn, i);
				this.setRandomMixerParameters(commons.osc.INPUT_DYNA_ATTACK, 'number', chn, i);
				this.setRandomMixerParameters(commons.osc.INPUT_DYNA_ATTACKLABEL, 'string', chn, i);
				this.setRandomMixerParameters(commons.osc.INPUT_DYNA_DECAY, 'number', chn, i);
				this.setRandomMixerParameters(commons.osc.INPUT_DYNA_DECAYLABEL, 'string', chn, i);
				this.setRandomMixerParameters(commons.osc.INPUT_DYNA_GAIN, 'number', chn, i);
				this.setRandomMixerParameters(commons.osc.INPUT_DYNA_GAINLABEL, 'string', chn, i);
				this.setRandomMixerParameters(commons.osc.INPUT_DYNA_HOLD, 'number', chn, i);
				this.setRandomMixerParameters(commons.osc.INPUT_DYNA_HOLDLABEL, 'string', chn, i);
				this.setRandomMixerParameters(commons.osc.INPUT_DYNA_KNEE, 'number', chn, i);
				this.setRandomMixerParameters(commons.osc.INPUT_DYNA_KNEELABEL, 'string', chn, i);
				this.setRandomMixerParameters(commons.osc.INPUT_DYNA_RANGE, 'number', chn, i);
				this.setRandomMixerParameters(commons.osc.INPUT_DYNA_RANGELABEL, 'string', chn, i);
				this.setRandomMixerParameters(commons.osc.INPUT_DYNA_RATIO, 'number', chn, i);
				this.setRandomMixerParameters(commons.osc.INPUT_DYNA_RATIOLABEL, 'string', chn, i);
				this.setRandomMixerParameters(commons.osc.INPUT_DYNA_RELEASE, 'number', chn, i);
				this.setRandomMixerParameters(commons.osc.INPUT_DYNA_RELEASELABEL, 'string', chn, i);
				this.setRandomMixerParameters(commons.osc.INPUT_DYNA_THRESHOLD, 'number', chn, i);
				this.setRandomMixerParameters(commons.osc.INPUT_DYNA_THRESHOLDLABEL, 'string', chn, i);
			}
		},
		
		setRandomOutputParameters: function() {
			this.setRandomMixerParameters(
					commons.osc.OUTPUT_MASTER_LEVEL, 'number', this.get('outputCount'));
			this.setRandomMixerParameters(
					commons.osc.OUTPUT_AUX_LEVEL, 'number', this.get('outputAuxCount'));
			this.setRandomMixerParameters(
					commons.osc.OUTPUT_BUS_LEVEL, 'number', this.get('outputBusCount'));
		},
		
		setRandomMixerParameters: function(oscPattern, valType,chNum, count) {
			for (var i = 0; i <= chNum; i++) {
				var address = commons.osc.replaceChannelNumber(oscPattern, i);
				if (count) {
					var address = commons.osc.replaceCounterNumber(address, count);
				}
				var value = this.setRandomValue(valType, oscPattern);
				this.mixerParameters[address] = value;
			}
		},
		
		setRandomValue: function(valueType, oscPattern) {
			var value = null;
			if (valueType === 'number') {
				value = Math.random();
			} else if (valueType === 'boolean') {
				value = Math.random() >= 0.5;
			} else if (valueType === 'string') {
				value = this.setRandomLabel(oscPattern);
			}
			return value;
		},
		
		setRandomLabel: function(oscPattern) {
			var value = null;
			var random = Math.random();
			var param = oscPattern.substring(oscPattern.lastIndexOf('/') + 1);
			if (param === 'levelLabel') {
				value = this.labels.getLabelLevel10Db(random);
			} else if (param ==='panLabel') {
				value = this.labels.getLabelPanning(random);
			} else if (param === 'qLabel') {
				value = this.labels.getLabelPeqQ(random);
			} else if (param === 'fLabel') {
				value = this.labels.getLabelPeqF(random);
			} else if (param === 'gLabel') {
				value = this.labels.getLabelPeqG(random);
			}
			return value;
		}
		
	});
	
	return FakeMixerModel;
	
});