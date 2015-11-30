define(['underscore', 'backbone', 'view.controls/FaderVolumeView'],
		function(_, Backbone, FaderVolumeView) {

	xdescribe('A Fader View', function() {
		var dispatcher, fader;
		var oscAddress = '/dev/null';
		var stage = new createjs.Stage("noCanvas");

		beforeEach(function() {
			dispatcher = _.extend({}, Backbone.Events);
			fader = new FaderView({dispatcher: dispatcher, oscAddress: oscAddress, stage: stage});
			spyOn(fader, 'setFaderAssembly');
		});

		it('should be able to create its test objects', function() {
			expect(dispatcher).toBeDefined();
			expect(fader).toBeDefined();
		});

		/*
		it('should fail if not properly initialized', function() {
			expect(function() { new FaderView(); }).toThrow(new Error('Illegal Fader object initialization: TypeError: options is undefined'));
			expect(function() { new FaderView({oscAddress: oscAddress}); }).toThrow(new Error('Illegal Fader object initialization: Argument "dispatcher" and/or "oscAddress" and/or "canvasId" missing.'));
		});
		*/

		describe('has to setup its graphic components:', function() {
			// This should be extended when the fader design gets refactored

			it('a stage object that acts as a canvas layer', function() {
				expect(fader.stage).toBeDefined();
			});

			it('a container that holds bitmap and vector graphics', function() {
				expect(fader.container).toBeDefined();
			});

			it('the container should be a child of stage', function() {
				expect(fader.stage.children).toContain(fader.container);

			});
		});

		it('should set the container coords to the values of the model coords', function() {
			fader.model.set({'coords.x': 23, 'coords.y': 42});
			fader.setContainerCoords();
			expect(fader.container.x).toEqual(23);
			expect(fader.container.y).toEqual(42);
		});

	});
});