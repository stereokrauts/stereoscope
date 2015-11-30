define(['underscore', 'backbone', 'Squire'],
		function(_, Backbone, Squire) {

	// first we fake interfering dependencies of the object under test
	var injector = new Squire();

	injector.mock('view/SocketView', {})
	.mock('sockjs', {
		lastSentMsg: null,
		readyState: true,
		onopen: null,
		onclose: null,
		onmessage: null,
		send: function(msg) { lastSentMsg = msg; }
	})
	.require(['model/SocketModel'], function(SocketModel) {

		xdescribe('A Socket Model', function() {
			
			var message = null;
			var dispatcher = null;
			var socketModel = null;
			
			beforeEach(function() {
				message = { type: 'string',
						oscStr: '/dev/null',
						val: 'halla'};
				dispatcher = _.extend({}, Backbone.Events);
				socketModel = new SocketModel({dispatcher: dispatcher, socketPath: 'stereoscope'});
			});

			afterEach(function() {
				message = dispatcher = socketModel = null;
			});

			it('should be able to create its test object', function() {
				expect(dispatcher).toBeDefined();
				expect(socketModel).toBeDefined();
			});

			/*
			it('should fail if not properly initialized', function() {
				expect(function() { new SocketModel(); }).toThrow(new Error());
				expect(function() { new SocketModel({dispatcher: dispatcher}); }).toThrow(new Error());
			});
			*/

			it('should stringify a message object', function() {
				var stringified = socketModel.stringify(message);
				expect(stringified).toMatch('{"type":"string","oscStr":"/dev/null","val":"halla"}');
			});

			it('should have a function that returns false if the current url is not the same as the socket url', function() {
				expect(socketModel.getSocketUrl()).toBeFalsy();
			});

			it('should remove a trailing slash from a string', function() {
				var str = socketModel.removePrecedingSlash('/El Pollo Diablo');
				expect(str).toMatch('El Pollo Diablo');
			});

			it('should have an abort function that halts the application', function() {
				expect(function() { socketModel.abort('stop'); }).toThrow(new Error('stop'));
			});

			describe('Socket Model :: Event Handling', function() {
				it('should call a handler if a "onopen" event happens', function() {
					spyOn(socketModel, 'onOpen');
					socketModel.socket.onopen();
					expect(socketModel.onOpen).toHaveBeenCalled();
				});

				it('should call a handler if a "onclose" event happens', function() {
					spyOn(socketModel, 'onClose');
					socketModel.socket.onclose();
					expect(socketModel.onClose).toHaveBeenCalled();
				});

				it('should call a handler if a "onmessage" event happens', function() {
					spyOn(socketModel, 'onMessage');
					socketModel.socket.onmessage();
					expect(socketModel.onMessage).toHaveBeenCalled();
				});

				it('should trigger a dispatcher-event when an "onopen" event happens', function() {
					var response = null;
					dispatcher.bind('socketOpen', function(e) {response = e;});
					socketModel.socket.onopen();
					expect(response).toBeTruthy();
				});

				it('should trigger a dispatcher-event if an "onclose" event happens', function() {
					var response = null;
					dispatcher.bind('socketOpen', function(e) {response = e;});
					socketModel.socket.onclose();
					expect(response).toBeFalsy();
				});

				it('should trigger a dispatcher-event if an "onmessage" event happens', function() {
					var msg = { data: '{oscStr: "/dev/null"}' };
					var response = null;
					dispatcher.bind('/dev/null', function(e) {response = e;});
					socketModel.socket.onmessage(msg);
					expect(response.oscStr).toMatch('/dev/null');
				});

				it('should call a handler if a "sendMessage" event arrives from dispatcher', function() {
					spyOn(socketModel, 'sendMessage');
					dispatcher.trigger('sendMessage', '/dev/null');
					expect(socketModel.sendMessage).toHaveBeenCalled();
				});

				it('should send a stringified message to server when a "sendMessage" event arrives', function() {
					spyOn(socketModel.socket, 'send');
					dispatcher.trigger('sendMessage', message);
					expect(socketModel.socket.send).toHaveBeenCalled();
				});
			});
		});
	});
});