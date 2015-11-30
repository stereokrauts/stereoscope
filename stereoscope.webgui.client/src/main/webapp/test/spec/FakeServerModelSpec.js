/**
 * FakeServerModelSpec
 * Spec for the FakeServerModel
 */
define(['underscore', 'backbone', 'model/FakeServerModel'],
		function(_, Backbone, FakeServerModel) {
	
	describe('A Fake Server Model', function() {
		
		var server = null;
		
		beforeEach(function() {
			server = new FakeServerModel();
		});
		
		afterEach(function() {
			server = null;
		});
		
		it('should initialize its test object', function() {
			expect(server).toBeDefined();
		});
		
		it('should contain faked mixer properties', function() {
			expect(server.get('mixerName')).toBeDefined();
			expect(server.get('inputChannelCount')).toBeDefined();
			expect(server.get('outputAuxCount')).toBeDefined();
			expect(server.get('outputBusCount')).toBeDefined();
			expect(server.get('outputGeqCount')).toBeDefined();
			expect(server.get('outputCount')).toBeDefined();
			expect(server.get('geqBandCount')).toBeDefined();
		});
		
		it('should keep track of stateful pages', function() {
			expect(server.get('currentInput')).toBeDefined();
			expect(server.get('currentAux')).toBeDefined();
			expect(server.get('currentGeq')).toBeDefined();
		});
		
		it('should have loaded a default layout on startup', function() {
			
		});
		
		it('should have loaded an aux layout on startup', function() {
			
		});
		
		it('should have loaded a custom layout on startup', function() {
			expect(server.customLayout).not.toBeNull();
		});
	
		describe('A request from client', function() {

			beforeEach(function() {
				spyOn(server, 'send');
			});

			it('should trigger a layout-list-response', function() {
				var request = '{"type":"string","oscStr":"/stereoscope/system/request/frontend/layoutList","val":"request"}';
				var response = '{"type":"string", ' +
				'"oscStr":"/stereoscope/system/response/frontend/layoutList", ' +
				'"val":"standard-layout.std.json§aux-demo.aux.json§custom-demo.aux.json§"}';
				socket.processServerRequest(request);
				expect(socket.send).toHaveBeenCalledWith(response);

			});

			it('should trigger a std-layout-response', function() {
				var request = '{"type":"string","oscStr":"/stereoscope/system/request/frontend/layout/std","val":"large"}';
			});

			it('should trigger a aux-layout-response', function() {
				var request = '{"type":"string","oscStr":"/stereoscope/system/request/frontend/layout/aux","val":"large"}';
			});

			it('should trigger a custom layout-response', function() {
				var request = '{"type":"string","oscStr":"/stereoscope/system/request/frontend/layout/custom","val":"large"}';
				socket.processServerRequest(request);
				expect(socket.send).toHaveBeenCalledWith(server.customLayout);
			});

			it('should trigger a mixerproperties-response', function() {
				var request = '{"type":"string","oscStr":"/stereoscope/system/request/mixerProperties","val":true}';
				var response = '{"type":"boolean","oscStr":"/stereoscope/system/response/mixerProperties","val":true}';
				socket.processServerRequest(request);
				expect(socket.send).toHaveBeenCalledWith(response);
			});

			it('should trigger resync-responses', function() {
				var request = '{"type":"string","oscStr":"/stereoscope/system/resync/channelLevels","val":true}';
			});
			
			it('should update stateful aux page on aux change', function() {
				
			});
			
			it('should update stateful geq page when geq changes', function() {
				
			});
			
			it('should update channel strip page on ch change', function() {
				
			});
			

		});
	
	
	})
	
});