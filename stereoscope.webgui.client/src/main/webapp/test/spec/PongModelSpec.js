define(['jquery', 'underscore', 'backbone', 'model/PongModel'],
		function($, _, Backbone, PongModel) {

	describe('A PongModel Spec', function() {
		var dispatcher, pong;

		beforeEach(function() {
			dispatcher = _.extend({}, Backbone.Events);
			pong = new PongModel(dispatcher);
		});

		afterEach(function() {
			dispatcher = pong = null;
		});

		it('should be able to create its test objects', function() {
			expect(dispatcher).toBeDefined();
			expect(pong).toBeDefined();
		});
		
		it('should load assets for the game', function() {
			
		});
		
		it('should start the game', function() {
			
		});

		xit('should fail if not properly initialized', function() {
			expect(function() { new testobj(); }).toThrow(new Error('Illegal <OBJ> object initialization: TypeError: options is undefined'));
			expect(function() { new testobj({nastyArg: 'blublub'}); }).toThrow(new Error('Illegal <OBJ> object initialization: Argument <ARG> missing.'));
		});

		xdescribe('has a default value which', function() {
			it('is a blublub', function() {
				expect(blublub).toEqual('something');
			});
		});

	});
});