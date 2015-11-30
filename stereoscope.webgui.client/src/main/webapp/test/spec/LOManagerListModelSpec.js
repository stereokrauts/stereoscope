define(['underscore', 'backbone', 'commons', 'model.layoutManager/LOManagerListModel'],
		function(_, Backbone, commons, LOManagerListModel) {

	xdescribe('A Layout List Model', function() {
		var dispatcher, htmlID, layoutList, layouts, layout;

		beforeEach(function() {
			dispatcher = _.extend({}, Backbone.Events);
			htmlId = 'layoutListId';
			layoutList = new LOManagerListModel({dispatcher: dispatcher, htmlId: htmlId});
			layouts = {"type":"string",
					"oscStr":"/stereoscope/system/response/frontend/layoutList",
					"chn":0,
					"cnt":-1,
					"val":"prototype_01.mixerui§prototype_02.mixerui§prototype_03.mixerui§"
			};
			layout = {
					'type': 'object',
					'oscStr': commons.osc.LAYOUT_RESPONSE,
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
						}
					}
			};
		});

		afterEach(function() {
			dispatcher = htmlId = layoutList = layouts = layout = null;
		});

		it('should be able to create its test objects', function() {
			expect(dispatcher).toBeDefined();
			expect(layoutList).toBeDefined();
			expect(layouts).toBeDefined();
			expect(layout).toBeDefined();
		});

		/*
		it('should fail if not properly initialized', function() {
			expect(function() { new LOManagerListModel(); }).toThrow(new Error('Illegal LayoutListModel object initialization: TypeError: options is undefined'));
			expect(function() { new LOManagerListModel({nastyArg: 'blublub'}); }).toThrow(new Error('Illegal LayoutListModel object initialization: Argument "dispatcher" missing.'));
		});
		*/

		describe('has a default value which', function() {

			it('is layoutItems of type collection', function() {
				expect(layoutList.get('layoutItems').length).toEqual(0);
			});

		});

		// bullshit: better test if the layout request is triggered
		/*
	it('should create a layout-list request object', function() {
		var obj = layoutList.createRequestObject(static.osc.LAYOUT_LIST_REQUEST, 'request');
		expect(obj).toBeDefined();
		expect(obj.type).toMatch('string');
		expect(obj.oscStr).toMatch(static.osc.LAYOUT_LIST_REQUEST);
		expect(obj.val).toMatch('request');
	});
		 */

		// deprecated: now in LayoutItemModel
		/*
	it('should create a layout request object', function() {
		var obj = layoutList.createRequestObject(static.osc.LAYOUT_REQUEST, 'layoutName');
		expect(obj).toBeDefined();
		expect(obj.type).toMatch('string');
		expect(obj.oscStr).toMatch(static.osc.LAYOUT_REQUEST);
		expect(obj.val).toMatch('layoutName');
	});
		 */

		describe('has event based communication that', function() {

			it('should call a handler when a "socketOpen" event arrives from dispatcher', function() {
				spyOn(layoutList, 'requestLayoutList');
				dispatcher.trigger('socketOpen', 'dummytext');
				expect(layoutList.requestLayoutList).toHaveBeenCalled();
			});

			it('should call a handler when a "oscListResponse" event arrives from dispatcher', function() {
				spyOn(layoutList, 'handleListResponse');
				dispatcher.trigger(commons.osc.LAYOUT_LIST_RESPONSE, 'dummytext');
				expect(layoutList.handleListResponse).toHaveBeenCalled();
			});

			it('should call a handler when a "oscLayoutResponse" event arrives from dispatcher', function() {
				spyOn(layoutList, 'handleLayoutResponse');
				dispatcher.trigger(commons.osc.LAYOUT_RESPONSE, 'dummytext');
				expect(layoutList.handleLayoutResponse).toHaveBeenCalled();
			});

			it('should trigger a layout-list request event', function() {
				dispatcher.bind('sendMessage', function(e) {
					expect(e).toBeDefined();
					expect(e.oscStr).toMatch(commons.osc.LAYOUT_LIST_REQUEST);
				});
				dispatcher.trigger('socketOpen', true);
			});

			xit('should trigger a layout request event', function() {
				dispatcher.bind('sendMessage', function(e) {
					expect(e).toBeDefined();
					expect(e.oscStr).toMatch(oscLayoutRequest);
				});

			});

			it('should handle the list response with a callback', function() {
				dispatcher.trigger(commons.osc.LAYOUT_LIST_RESPONSE, layouts);
				expect(layoutList.get('layoutItems').length).toEqual(3);

			});

			it('should handle the layout response with a callback', function() {
				dispatcher.trigger(commons.osc.LAYOUT_RESPONSE, layout);
				expect(layoutList.get('layout').setup.version).toEqual(0.1);
			});
		});

		it('should assemble an array of layoutItem models from a list of names', function() {

		});

		it('should override the current item collection with an array of layoutItem models', function() {

		});

	});
});