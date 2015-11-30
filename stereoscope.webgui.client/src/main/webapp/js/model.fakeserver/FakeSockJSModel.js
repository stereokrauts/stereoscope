/**
 * This model fakes the sockJS api
 * to be used in demo/standalone mode.
 * 
 */
define([ 'underscore', 'backbone', 'commons', 'model.fakeserver/FakeServerModel' ],
		function(_, Backbone, commons, FakeServerModel) {
	'use strict';
	
	var FakeSockJSModel = Backbone.Model.extend({
		
		readyState: 0,
		server: null,
		
		initialize: function(options) {
			try {
				if (options.mq) {
					this.server = new FakeServerModel({ mq: options.mq });
				} else {
					throw 'Argument mq missing.';
				}
			} catch(e) {
				var msg = 'Illegal FakeSockJSModel object initialization: ' + e;
				throw new Error(msg);
			}
			this.connect();
		},
		
		connect: function() {
			if (this.server) {
				this.server.on('change:msgToClient', this.receive, this);
				this.readyState = 1;
				this.fireOnOpen(1000);
			}
		},
		
		onopen: function() {
			return true;
		},
		
		onclose: function() {
			return true;
		},
		
		onmessage: function() {
			return true;
		},
		
		send: function(data) {
			if (this.readyState) {
				this.server.receive(data);
			}
		},
		
		receive: function(e) {
			e.data = e.changed.msgToClient;
			this.onmessage(e);
		},
		
		fireOnOpen: function(delay) {
			setTimeout(function(ctx) {
				ctx.onopen();
			}, delay, this);
		}
		
	});
	
	return FakeSockJSModel;
	
});