/**
 * This model cares about the socket connection
 * to the server.
 */
define([
        'underscore',
        'backbone',
        'view/SocketView',
        'sockjs',
        'model.fakeserver/FakeSockJSModel'
        ], function(_, Backbone, SocketView, SockJS, FakeSockJS) { 

	var SocketModel = Backbone.Model.extend({

		dispatcher: null,
		socket: null,
		socketPath: null,
		mq: null,

		reconInterval: null,

		defaults: {
			messageHandler: null,
			demoMode: false
		},

		initialize: function(options) {
			try {
				if (options.dispatcher && options.socketPath && options.mq) {
					this.dispatcher = options.dispatcher;
					this.socketPath = this.removePrecedingSlash(options.socketPath);
					this.mq = options.mq;
				} else {
					throw 'Argument "dispatcher" and/or "socketPath" and/or mq missing.';
				}
			} catch(e) {
				var msg = 'Illegal SocketModel object initialization: ' + e;
				this.abort(msg);
			}
			var self = this;
			_.bindAll(this, 'sendMessage', 'onMessage', 'onOpen', 'onClose');
			this.dispatcher.bind('sendMessage', function(e) { self.sendMessage(e); });

			this.view = new SocketView({ model: this });
			this.setServerConnection();

		},

		setServerConnection: function() {
			if (this.get('demoMode')) {
				this.socket = new FakeSockJS({ mq: this.mq });
			} else {
				this.socket = new SockJS(this.getSocketUrl(), null);
			}
			if (this.socket) {
				this.socket.onopen = this.onOpen;
				this.socket.onclose = this.onClose;
				this.socket.onmessage = this.onMessage;
			}
		},

		reconnect: function() {
			this.socket.close();
			setTimeout(function() {
				// do nothing for a while to save cpu power
			}, 3000);

			this.socket = new SockJS(this.getSocketUrl(), null);
			console.log(this.socket);
			this.socket.onopen = this.onOpen;
			this.socket.onclose = this.onClose;
		},

		onOpen: function() {
			//clearInterval(this.reconInterval);
			console.log('Socket connection initialized @ ' + this.getSocketUrl());
			console.log('Socket open');
			this.dispatcher.trigger('socketOpen', true);
		},

		onClose: function() {
			console.log('closing Socket');
			this.dispatcher.trigger('socketOpen', false);

			if (!this.demoMode) {
				this.reconnect();
			}
			/*
		this.reconInterval = setInterval(function(ctx) {
			console.log('Trying to connect to server...');
			ctx.reconnect();
		}, 2000, this);
			 */
			/*
		setTimeout(function(ctx) {
			console.log('Trying to connect to server...');
			ctx.reconnect();
		}, 1000, this);
			 */

		},

		onMessage: function(event) {
			console.log('Received: ' + event.data);
			// eval is very fast and very strict
			var oscObject = eval('(' + event.data + ')');
			this.dispatcher.trigger(oscObject.oscStr, oscObject);
		},

		sendMessage: function(msg){
			if (this.socket.readyState == WebSocket.OPEN) {
				if (msg && typeof msg === "object") {
					var msgString = this.stringify(msg);
					console.log("sending: " + msgString);
					this.socket.send(msgString);
				} else {
					console.log('Sending of message failed. ' +
					'Message body was null.');
				}
			} else {
				console.log("Socket is not open.");
			}
		},

		getSocketUrl: function() {
			var urlProtocol = window.location.protocol;
			var urlHost = window.location.host;
			var urlPath = window.location.pathname;
			var url = urlProtocol + '//' + urlHost + '/' + this.socketPath;
			if (!urlPath || urlPath == '/') {
				console.log('Base URL is ' + urlProtocol + '//' + urlHost);
				return url;
			} else {
				url = urlProtocol + '//' + urlHost + '/' + urlPath;
				console.log('Wrong URL: ' + 
						url + '. Should be "http://[stereoscopehost]:8090".');
				return false;
			}
		},

		removePrecedingSlash: function(string) {
			var firstChar = string.charAt(0);
			if (firstChar === '/') {
				return (string.substring(1, string.length));
			} else {
				return string;
			}
		},

		stringify: function(jsObject) {
			return JSON.stringify(jsObject);
		},

		abort: function(msg) {
			throw new Error(msg);
		}

	});

	return SocketModel;

});