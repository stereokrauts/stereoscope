/**
 * FakeSockJSModelSpec
 * Spec for the FakeSockJSModel
 */
define(['underscore', 'backbone', 'lib/Squire'],
		function(_, Backbone, Squire) {
	
	// first we fake interfering dependencies of the object under test
	var injector = new Squire();
	
	var fakeFakeServer = Backbone.Model.extend({
		
		defaults: {
			msgFromClient: "",
			msgToClient: ""
		},
		
	});

	injector.mock('model/FakeServerModel', fakeFakeServer)
	.require([ 'model/FakeSockJSModel' ], function(FakeSockJSModel) {

		describe('A Fake SockJS Model', function() {

			var socket = null;

			beforeEach(function() {
				socket = new FakeSockJSModel();
			});

			afterEach(function() {
				socket = null;
			});

			it('should be able to create its test objects', function() {
				expect(socket).toBeDefined();
			});

			it('should have a fake-server instance on startup', function() {
				expect(this.server).not.toBeNull();
			});

			it('should instantly connect to the fake server', function() {
				expect(socket.readyState).toBeTruthy();
			});

			it('should have an "onopen" callback', function() {
				expect(socket.onopen()).toBeTruthy();
			});

			it('should have an "onclose" callback', function() {
				expect(socket.onclose()).toBeTruthy();
			});

			it('should have an "onmessage" callback', function() {
				expect(socket.onmessage()).toBeTruthy();
			});

			it('should send messages to the "server"', function() {
				spyOn(socket, 'processServerRequest');
				socket.send('some data');
				expect(socket.processServerRequest).toHaveBeenCalledWith('some data');
			});

			it('should transform a request string into a message object', function() {
				var request = '{"type":"string","oscStr":"/stereoscope/system/request/frontend/layoutList","val":"request"}';
				var msg = socket.getMsgObject(request);
				expect(msg.val).toMatch('request');
			});

			


		});
	});

	
});