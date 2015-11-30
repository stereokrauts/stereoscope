/**
 * This is the base control model.
 * All other controls are subclassed
 * from this.
 * 
 * @author jansen
 *   
 */
define([
  'jquery',
  'underscore',
  'backbone',
  'commons'
], function($, _, Backbone, commons) {

	var ControlModel = Backbone.Model.extend({

		dispatcher: null,
		stages: null,

		defaults: {
			type: null,
			name: null,
			color: '#ff0000',
			coords: { x: 0, y: 0 },
			width: 0,
			height: 0,
			visibility: true,
			channel: 0,
			oscAddress: '',
			oscValue: 0.0,
			orient: 'horizontal',
			valueFromMixer: 0,
			valueFromView: 0

		},

		initialize: function(options) {
			try {
				if (options.dispatcher && options.params && options.stages) {
					this.dispatcher = options.dispatcher;
					this.initProperties(options.params);
					this.setCoords(options.params.coords.x,
							options.params.coords.y);
					this.stages = options.stages;
				} else {
					throw 'Argument "dispatcher" and/or "params" and/or "stages" missing.';
				}
			}
			catch(e) {
				var msg = 'Illegal ControlModel object initialization: ' + e;
				throw new Error(msg);
			}
			var self = this;
			_.bindAll(this, 'sendMessage');
			this.dispatcher.bind(this.get('oscAddress'), 
					function(e) { self.handleMessage(e); });
			this.on('change:valueFromView', this.sendMessage);
		},

		initProperties: function(params) {
			this.set({
				name: params.name,
				type: params.type,
				oscAddress: params.oscMessage,
				width: params.width,
				height: params.height,
				visibility: params.visibility,
				color: this.setColor(params.color),
				orient: params.orient
			});
			if (params.initValue) {
				this.set('valueFromMixer', params.initValue);
			}
		},

		handleMessage: function(msg) {
			this.set({valueFromMixer: msg.val, oscValue: msg.val});
		},

		changeDetected: function(val) {
			console.log("change detected: " + val.toSource());
		},

		sendMessage: function() {
			if (this.get('oscValue') != this.get('valueFromView')) {
				this.set('oscValue', this.get('valueFromView'));
				var msg = this.createMessageObject(this.get('oscValueType'),
						this.get('oscValue'));
				this.dispatcher.trigger('sendMessage', msg);
			}
		},

		createMessageObject: function(type, value) {
			var json = {};
			json.type = type;
			json.oscStr = this.get('oscAddress');
			json.val = value;
			if (type && (value || value === 0) &&
					this.get('oscAddress')) {
				return json;
			} else {
				console.log('At least one message parameter is null.');
				return false;
			}

		},

		setCoords: function(x, y) { 
			this.set({'coords': {x: x, y: y} });
		},

		setColor: function(color) {
			var capColor = color.toUpperCase();
			if (commons.color[capColor]) {
				return commons.color[capColor];
			} else {
				console.log('Unknown color: ' + color);
				return '#FFFF';
			}
		},


	});

	return ControlModel;
});


