define(['underscore', 'backbone', 'model.controls/FaderModel'],
		function(_, Backbone, FaderModel) {

	xdescribe('A Fader Model', function() {
		var dispatcher, faderModel;
		var params = {
				name: 'Horst Hrubesch',
				type: 'testControl',
				oscAddress: '/test/control/horst',
				width: 23,
				height: 42,
				coords: { x: 08, y: 15 },
				visibility: true,
				color: 'green',
				orient: 'horizontal',
				mode: 'normal'
		};

		beforeEach(function() {
			dispatcher = _.extend({}, Backbone.Events);
			faderModel = new FaderModel({dispatcher: dispatcher, params: params});
		});

		afterEach(function() {
			dispatcher, faderModel = null;
		});

		it('should be able to create its test objects', function() {
			expect(dispatcher).toBeDefined();
			expect(faderModel).toBeDefined();
		});

		describe('has default values including', function() {
			it('a type with a value of "fader"', function() {
				expect(faderModel.get('type')).toMatch('fader');
			});

			it('a oscMinValue of 0.0', function() {
				expect(faderModel.get('oscMinValue')).toEqual(0.0);
			});

			it('a oscMaxValue of 1.0', function() {
				expect(faderModel.get('oscMaxValue')).toEqual(1.0);
			});

			it('a faderKnobHeight of 25', function() {
				expect(faderModel.get('faderKnobHeight')).toEqual(25);
			});
		});

		it('should compute the faderknob value from a given float between 0 and 1', function() {
			faderModel.set({height: 140}); //fader height
			var fkVal1 = faderModel.getFaderKnobPosition(0.666);
			var fkVal2 = faderModel.getFaderKnobPosition(0.99);
			var fkVal3 = faderModel.getFaderKnobPosition(0.001);
			expect(fkVal1).toEqual(-77);
			expect(fkVal2).toEqual(-114);
			expect(fkVal3).toEqual(-0);
		});

		it('should compute the faderknob range', function() {
			faderModel.set({height: 140}); //fader height
			faderModel.setFaderKnobRange();
			expect(faderModel.get('faderKnobRange')).toEqual(115);
		});

		describe('can handle incomming messages which', function() {
			var dispatcher, faderModel, message, spy;

			beforeEach(function() {
				dispatcher = _.extend({}, Backbone.Events);
				faderModel = new FaderModel({dispatcher: dispatcher, oscAddress: oscAddress});
				message = { type: 'float',
						oscStr: '/stereoscope/test/faderModel',
						chn: 1,
						val: 0.2342 };
			});

			afterEach(function() {
				dispatcher, faderModel, message = null;
			});

			it('should invoke a callback', function() {
				// Obacht: callbacks have to be called within 
				// an annonymous function to avoid reference madness
				spyOn(faderModel, 'handleMessage');
				dispatcher.trigger(oscAddress, message);
				expect(faderModel.handleMessage).toHaveBeenCalled();
			});

			it('should set a new osc value', function() {
				dispatcher.trigger(oscAddress, message);
				expect(faderModel.get('oscValue')).toEqual(0.2342);
			});

			it('should set the faderknob position', function() {
				faderModel.set({height: 150});
				dispatcher.trigger(oscAddress, message);
				expect(faderModel.get('faderKnobYPosition')).toEqual(-29);
			});
		});

	});
});