describe('A Layout Model', function() {
	var layoutModel, dispatcher, layoutResponse;
	var oscLayoutResponse = '/stereoscope/system/response/frontend/layout';
	
	beforeEach(function() {
		dispatcher = _.extend({}, Backbone.Events);
		layoutModel = new sk.stsc.model.LayoutModel({dispatcher: dispatcher});
		layoutResponse = layoutResponse = {
				'type': 'object',
				'oscStr': oscLayoutResponse,
				'val': {
					'setup': {
						'version': 0.1,
						'mode': 'ipad',
						'orientation': 'vertical'
					},
					'mixer': {
						'mixerModel': 'Yamaha 01v96',
						'channelCount': 4,
						'auxCount': 4,
						'busCount': 4,
						'matrixCount': 4
					},
					'pages': {
						'inputs': [
							{
								type: 'fader',
								name: 'fader input1',
								label: '-Inf dB',
								width: 40,
								height: 200,
								coords: [
								         x = 50,
								         y = 50
								         ],
								colorScheme: 'green',
								visibility: true,
								oscMessage: '/stereoscope/input/1/level',
								oscLabelMessage: '/stereoscope/input/1/levelLabel'
							},
							{
								'type': 'fader',
								'name': 'fader input2',
								'label': '-Inf dB',
								'width': 40,
								'height': 200,
								coords: [
								         x = 150,
								         y = 50
								         ],
								'colorScheme': 'green',
								'visibility': true,
								'oscMessage': '/stereoscope/input/2/level',
								'oscLabelMessage': '/stereoscope/input/2/levelLabel'
						
							},
							{
								'type': 'fader',
								'name': 'fader input3',
								'label': '-Inf dB',
								'width': 40,
								'height': 200,
								coords: [
								         x = 250,
								         y = 50
								         ],
								'colorScheme': 'green',
								'visibility': true,
								'oscMessage': '/stereoscope/input/3/level',
								'oscLabelMessage': '/stereoscope/input/3/levelLabel'
							}
						],
						'auxiliaries': [
							{
								type: 'fader',
								name: 'fader aux1',
								label: '-Inf dB',
								width: 40,
								height: 200,
								coords: [
								         x = 50,
								         y = 50
								         ],
								colorScheme: 'green',
								visibility: true,
								oscMessage: '/stereoscope/input/1/toAux/1/level',
								oscLabelMessage: '/stereoscope/input/1/toAux/1/levelLabel'
							},
							{
								'type': 'fader',
								'name': 'fader aux2',
								'label': '-Inf dB',
								'width': 40,
								'height': 200,
								coords: [
								         x = 150,
								         y = 50
								         ],
								'colorScheme': 'green',
								'visibility': true,
								'oscMessage': '/stereoscope/input/2/toAux/1/level',
								'oscLabelMessage': '/stereoscope/input/2/toAux/1/levelLabel'
						
							}
						],
						'channel strip': [
							{
								'type': 'fader',
								'name': 'fader input1',
								'label': '-Inf dB',
								'width': 40,
								'height': 200,
								coords: [
								         x = 50,
								         y = 50
								         ],
								'colorScheme': 'green',
								'visibility': true,
								'oscMessage': '/stereoscope/input/1/level',
								'oscLabelMessage': '/stereoscope/input/1/levelLabel'
							}
						]
					}
				}
		};
	});
	
	afterEach(function() {
		layoutModel, dispatcher, layoutResponse = null;
	});

	it('should be able to create its test objects', function() {
		expect(dispatcher).toBeDefined();
		expect(layoutModel).toBeDefined();
	});
	
	it('should fail if not properly initialized', function() {
		expect(function() { new sk.stsc.model.LayoutModel(); }).toThrow(new Error('Illegal LayoutModel object initialization: TypeError: options is undefined'));
		expect(function() { new sk.stsc.model.LayoutModel({nastyArg: 'blublub'}); }).toThrow(new Error('Illegal LayoutModel object initialization: Argument "dispatcher" missing.'));
	});
	
	describe('has default values including', function() {
		it('an osc layout response address of type string', function() {
			expect(layoutModel.get('oscLayoutResponse')).toMatch(oscLayoutResponse);
		});
		
		it('a collection of layout pages', function() {
			expect(layoutModel.get('pages')).toBeDefined();
			expect(layoutModel.get('pages').length).toEqual(0);
		});
	});
	
	it('should call a handler when receiving layout response event', function() {
		spyOn(layoutModel, 'handleLayoutResponse');
		dispatcher.trigger(oscLayoutResponse, layoutResponse);
		expect(layoutModel.handleLayoutResponse).toHaveBeenCalledWith(layoutResponse);
	});
	
	it('should populate the page collection with page models', function() {
		layoutModel.setLayoutPages(layoutResponse.val);
		expect(layoutModel.get('pages').length).toEqual(3);
	});
	
	it('should populate page models with controls', function() {
		
	});
	
	
});