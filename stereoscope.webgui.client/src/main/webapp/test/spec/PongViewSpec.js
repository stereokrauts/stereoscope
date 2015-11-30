/**
 * PongViewSpec - Specs for the pong view
 */
define(['jquery', 'underscore', 'backbone', 'view/PongView'],
		function($, _, Backbone, PongView) {
	
	describe('A PongView Spec', function() {
		var model, pong;		
		
		beforeEach(function() {
			model = {};
			pong = new PongView(model);
		});

		afterEach(function() {
			model = pong = null;
		});
		
		it('should be able to create its test objects', function() {
			expect(pong).toBeDefined();
		});
		
		it('should have added canvases to DOM on startup', function() {
			
		});
		
		it('should have created stages on startup', function() {
			
		});
		
		it('should have created all graphic shapes on startup', function() {
			
		});
		
		it('should start a ticker on game start', function() {
			
		});
		
		
		
		
	});
	
});