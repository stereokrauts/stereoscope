/**
 * This model emulates the behaviour
 * of the Webgui server module.
 * It is needed for demo/standalone mode when no
 * real server is available.
 */
define([ 'underscore', 'backbone', 'commons', 'model.fakeserver/FakeMixerModel', 'preload' ],
		function(_, $, commons, FakeMixerModel) {
	
	var FakeServerModel = Backbone.Model.extend({
		
		mq: null,
		stdLayout: null,
		auxLayout: null,
		customLayout: null,
		mixer: null,
		mixerParameters: {},
		layoutLoaded: false,
		
		defaults: {
			msgToClient: 'nop-nop-nop',
			currentInput: 0,
			currentAux: 0,
			currentGEQ: 0
		},

		initialize: function(options) {
			try {
				if (options.mq) {
					this.mq = options.mq;
					this.mixer = new FakeMixerModel();
				} else {
					throw 'Argument mq missing.';
				}
			} catch(e) {
				var msg = 'Illegal FakeServerModel object initialization: ' + e;
				this.abort(msg);
			}
			
			this.loadLayout();
		},

		loadLayout: function() {
			var self = this;
			var mediaQuery = this.mq.getCurrentMedia();
			var stdLayout = 'layout-std-' + mediaQuery + '.json';
			var json = '';
			var queue = new createjs.LoadQueue(true, 'assets/');
			queue.on('complete', handleComplete, this);
			queue.loadFile({ id: 'stdLayout', src: 'layouts/layout-std-test.json', type: createjs.AbstractLoader.TEXT });
			                    //{id: 'customLayout', src: 'custom-layout.json'}
			                    //]);

			function handleComplete() {
				//console.log(queue.getResult('stdLayout'));
				//self.stdLayout = queue.getResult('stdLayout').replace(/(\r\n|\n|\r)/gm, '');
				self.stdLayout = queue.getResult('stdLayout');
				self.layoutLoaded = true;
				//self.stdLayoutResponse();
				//this.customLayout = queue.getResult('customLayout');
			}
			
			
		},

		send: function(msg) {
			this.set('msgToClient', msg);
		},

		receive: function(msg) {
			console.log('FakeSrv: Received msg: ' + msg);
			this.processRequest(msg);
		},
		
		processRequest: function(data) {
			var msg = this.getMsgObject(data);
			
			if (msg.oscStr === commons.osc.LAYOUT_LIST_REQUEST) {
				this.layoutListResponse();
			} else if (msg.oscStr === commons.osc.LAYOUT_REQUEST) {
				
			} else if (msg.oscStr === commons.osc.LAYOUT_STD_REQUEST) {
				this.stdLayoutResponse();
			} else if (msg.oscStr === commons.osc.LAYOUT_AUX_REQUEST) {
				
			} else if (msg.oscStr === commons.osc.SYSTEM_MIXERPROP_REQUEST) {
				this.mixerPropertiesResponse();
			} else if (msg.oscStr === commons.osc.SYSTEM_RESYNC_ALL ||
					commons.osc.SYSTEM_RESYNC_STATEFUL ||
					commons.osc.SYSTEM_RESYNC_SELECTED_AUX ||
					commons.osc.SYSTEM_RESYNC_SCENE_CHANGE ||
					commons.osc.SYSTEM_RESYNC_CHN_LEVELS ||
					commons.osc.SYSTEM_RESYNC_CHN_NAMES ||
					commons.osc.SYSTEM_RESYNC_CHN_ON ||
					commons.osc.SYSTEM_RESYNC_AUX_SEND_LEVELS ||
					commons.osc.SYSTEM_RESYNC_AUX_SEND_ON ||
					commons.osc.SYSTEM_RESYNC_GEQ_BAND_LEVELS ||
					commons.osc.SYSTEM_RESYNC_DELAYTIMES ||
					commons.osc.SYSTEM_RESYNC_CHANNELSTRIP ||
					commons.osc.SYSTEM_RESYNC_OUTPUTS) {
				this.resyncResponse();
			}
		},
		
		layoutListResponse: function() {
			var response = '{"type":"string", ' +
			'"oscStr":"/stereoscope/system/response/frontend/layoutList", ' +
			'"val":"standard-layout.std.json§dummy1.aux.json§dummy2.aux.json§dummy3.aux.json§"}';
			this.send(response);
		},
		
		stdLayoutResponse: function() {
			if (this.layoutLoaded) {
				var response = '{"type":"string", ' +
				'"oscStr":"/stereoscope/system/response/frontend/layout", ' +
				'"val": ' +  this.stdLayout + '}';
				this.send(response);
			} else {
				console.log('Layout not fully loaded.');
			}
		},
		
		mixerPropertiesResponse: function() {
			var response = '{"type":"boolean","oscStr":"/stereoscope/system/response/mixerProperties","val":true}';
			this.send(response);
		},
		
		resyncResponse: function() {
			/*this.resyncInputResponse();
			this.resyncSelectedAux();
			this.resyncSelectedGeq();
			this.resyncDelayTimes();
			this.resyncChannelStrip();
			this.resyncOutputs();*/
		},

		getMsgObject: function(data) {
			return eval('(' + data + ')');
		}
	});

	return FakeServerModel;
	
});