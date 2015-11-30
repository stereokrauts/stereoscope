define(['underscore', 'backbone', 'model.controls/ControlModel'],
		function(_, Backbone, ControlModel) {

	xdescribe('A ControlModel', function() {

		var dispatcher = null;
		var stages = null;
		var controlModel = null;
		var params = {
				name: 'Horst Hrubesch',
				type: 'testControl',
				oscAddress: '/test/control/horst',
				width: 23,
				height: 42,
				coords: { x: 08, y: 15 },
				visibility: true,
				color: 'green',
				orient: 'horizontal'
		};
		
		beforeEach(function() {
			dispatcher = _.extend({}, Backbone.Events);
			stages = { testval: 23 };
			controlModel = new ControlModel({
				dispatcher: dispatcher,
				params: params,
				stages: stages
			});
		});
		
		afterEach(function() {
			oscAddress = dispatcher = controlModel = null;
		});
		
		
		it('should be able to create its test objects', function() {
			expect(dispatcher).toBeDefined();
			expect(controlModel).toBeDefined();
		});

		/*
		it('should fail if not properly initialized', function() {
			expect(function() { new ControlModel(); }).toThrow(new Error());
			expect(function() { new ControlModel({oscAddress: oscAddress}); }).toThrow(new Error());
		});
		*/

		describe('has default values including', function() {

			it('coords which is an array', function() {
				var coords = controlModel.get('coords');
				expect(coords.length).toEqual(2);
				//expect(coords[y]).toBeDefined();
			});

			it('a default visibility of "true"', function() {
				expect(controlModel.get('visibility')).toBeTruthy();
			});
		});

		describe('has a createMessageObject function which', function() {
			controlModel.set({'oscValue': 'test', 'channel': 1});

			it('can have a value of type string', function() {
				var expectedMessage = { type: 'string',
						oscStr: '/stereoscope/test/controlModel',
						chn: 1,
						val: 'test' };
				var returnedMessage = controlModel.createMessageObject('string', 'test');
				expect(returnedMessage).toEqual(expectedMessage);
			});

			it('can have a value of type float', function() {
				var expectedMessage = { type: 'float',
						oscStr: '/stereoscope/test/controlModel',
						chn: 1,
						val: 0.2342 };
				var returnedMessage = controlModel.createMessageObject('float', 0.2342);
				expect(returnedMessage).toEqual(expectedMessage);
			});

			it('can have a value of type boolean', function() {
				var expectedMessage = { type: 'boolean',
						oscStr: '/stereoscope/test/controlModel',
						chn: 1,
						val: false };
				var returnedMessage = controlModel.createMessageObject('boolean', false);
				expect(returnedMessage).toEqual(expectedMessage);
			});

			it('returns "false" if one or more properties are missing', function() {
				var returnedMessage = controlModel.createMessageObject('boolean');
				expect(returnedMessage).toBeFalsy();
			});
		});

		describe('has an abort function which', function() {

			it('halts the app', function() {
				expect(function() { controlModel.abort('stop'); }).toThrow(new Error('stop'));
			});
		});

		describe('has a sendMessage function which', function() {
			controlModel.set({channel: 1});
			controlModel.set({valueFromView: 0.2342});
			controlModel.set({oscValueType: 'float'});

			it('sets the "oscValue" if "valueFromView" has changed', function() {
				var oscValue = controlModel.get('oscValue');
				expect(oscValue).toEqual(0.2342);
			});

			it('sends the message to the dispatcher where it gets distributed to subscribers', function() {
				var expectedMessage = { type: 'float',
						oscStr: '/stereoscope/test/controlModel',
						chn: 1,
						val: 0.2342 };
				var messageFromDispatcher = null;
				dispatcher.bind('sendMessage', function(msg) {messageFromDispatcher = msg;});
				controlModel.sendMessage();
				expect(messageFromDispatcher).toEqual(expectedMessage);
			});

		});

	});
});