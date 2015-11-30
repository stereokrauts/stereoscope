define(['underscore', 'backbone', 'easel', 'view.controls/FaderBasicView'],
		function(_, Backbone, createjs, FaderBasicView) {

	xdescribe('A Basic Fader View', function() {
		var fader, dispatcher;
		var oscAddress = '/dev/null';
		var stage = new createjs.Stage("noCanvas");

		beforeEach(function() {
			dispatcher = _.extend({}, Backbone.Events);
			fader = new FaderBasicView({dispatcher: dispatcher, oscAddress: oscAddress, stage: stage});
		});

		afterEach(function() {
			fader, dispatcher = null;
		});

		it('should be able to create its test object', function() {
			expect(fader).toBeDefined();
		});

		/*
		it('should fail if not properly initialized', function() {
			expect(function() { new FaderBasicView(); }).toThrow(new Error('Illegal Basic Fader View object initialization: TypeError: options is undefined'));
			expect(function() { new FaderBasicView({nastyArg: 'blublub'}); }).toThrow(new Error('Illegal Basic Fader View object initialization: Argument "dispatcher" and/or "oscAddress" and/or "stage" missing.'));
		});
		*/

		it('has a stage which is an abstraction to an html canvas', function() {
			expect(fader.stage).toBeAnEaselStage();
		});

		it('has a container object that can hold geometrical shapes', function() {
			expect(fader.container).toBeAnEaselContainer();
		});

		it('has a background shape', function() {
			expect(fader.faderBackground).toBeAnEaselShape();
		});

		it('has a knob shape', function() {
			expect(fader.faderKnob).toBeAnEaselShape();
		});

	});
});