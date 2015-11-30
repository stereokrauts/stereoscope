/**
 * 
 */
sk.stsc.model.SocketModel = Backbone.Model.extend({

	dispatcher: null,
	socket: null,
	socketPath: null,
	
	demoMode: false,
	reconInterval: null,
		
	defaults: {
		messageHandler: null,
		mocking: null
	},

	initialize: function(options) {
		try {
			if (options.dispatcher && options.socketPath) {
				this.dispatcher = options.dispatcher;
				this.socketPath = this.removePrecedingSlash(options.socketPath);
				if (options.mocking) {
					this.set('mocking', options.mocking);
				}
			} else {
				throw 'Argument "dispatcher" and/or "socketPath" missing.';
			}
		} catch(e) {
			var msg = 'Illegal SocketModel object initialization: ' + e;
			this.abort(msg);
		}
		var self = this;
		_.bindAll(this, 'sendMessage', 'onMessage', 'onOpen', 'onClose');
		this.dispatcher.bind('sendMessage', function(e) { self.sendMessage(e); });

		this.view = new sk.stsc.view.SocketView({ dispatcher: this.dispatcher });
		this.setServerConnection();
		
	},
	
	setServerConnection: function() {
		// probably bad style but prevents complex testing mocks.
		if (!this.get('mocking')) {
			this.socket = new SockJS(this.getSocketUrl(), null);
			if (this.socket) {
				this.socket.onopen = this.onOpen;
				this.socket.onclose = this.onClose;
				this.socket.onmessage = this.onMessage;
			}
		}
	},
	
	reconnect: function() {
		this.socket.close();
		setTimeout(function() {
			// do nothing for a while to save cpu power
		}, 2000);
		
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
		steal.dev.log('Received: ' + event.data);
		// eval is very fast and very strict
		var oscObject = eval('(' + event.data + ')');
		this.dispatcher.trigger(oscObject.oscStr, oscObject);
	},
	
	sendMessage: function(msg){
		if (this.socket.readyState == WebSocket.OPEN) {
			if (msg && typeof msg === "object") {
				var msgString = this.stringify(msg);
				steal.dev.log("sending: " + msgString);
				this.socket.send(msgString);
			} else {
				console.log('Sending of message failed. '
						+ 'Message body was null.');
			}
		} else {
			console.log("Socket is not open.");
		}
	},
	
	getSocketUrl: function() {
		urlProtocol = window.location.protocol;
		urlHost = window.location.host;
		urlPath = window.location.pathname;
		if (!urlPath || urlPath == '/') {
			var url = urlProtocol + '//' + urlHost + '/' + this.socketPath;
			steal.dev.log('Base URL is ' + urlProtocol + '//' + urlHost);
			return url;
		} else {
			var url = urlProtocol + '//' + urlHost + '/' + urlPath;
			var msg = 'Wrong URL: ' + url + '. Should be "http://[stereoscopehost]:8090".';
			console.log(msg);
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