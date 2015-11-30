define(['underscore', 'backbone', 'commons', 'Squire'],
		function(_, Backbone, commons, Squire) {

	// first we fake interfering dependencies of the object under test
	var injector = new Squire();

	injector.mock('view/AppView', {})
	.require(['model/AppModel'], function(AppModel) {

		xdescribe('An App Model', function() {
			var dispatcher = null;
			var appModel = null;
			var layout = "{"
				+ "	setup: {"
				+ "		version: 0.1,"
				+ "		mode: 'IPAD_LANDSCAPE'"
				+ "	},"
				+ "	mixer: {"
				+ "		mixerModel: 'Yamaha 01v96',"
				+ "		channelCount: 4,"
				+ "		auxCount: 4,"
				+ "		busCount: 4,"
				+ "		matrixCount: 4"
				+ "	},"
				+ "	pages: {"
				+ "		page01: {},"
				+ "		page02: {},"
				+ "		page03: {}"
				+ "	}"
				+ "}";

			beforeEach(function() {
				dispatcher = _.extend({}, Backbone.Events);
				appModel = new AppModel({dispatcher: dispatcher, layout: layout});
			});

			afterEach(function() {
				dispatcher, appModel = null;
			});

			it('should be able to create its test objects', function() {
				expect(dispatcher).toBeDefined();
				expect(appModel).toBeDefined();
			});

			/*
			it('should fail if not properly initialized', function() {
				expect(function() { new AppModel(); }).toThrow(new Error());
				expect(function() { new AppModel({nastyArg: 'blublub'}); }).toThrow(new Error());
			});
			*/

			/*
	describe('has a default value which', function() {

		it('is orientation with a "landscape" string value', function() {
			expect(app.get('orientation')).toEqual('landscape');
		})
	});
			 */

			it('should handle a layout response event', function() {
				spyOn(app, 'handleLayoutResponse');
				dispatcher.trigger(commons.osc.LAYOUT_RESPONSE, layout);
				expect(app.handleLayoutResponse).toHaveBeenCalled();
			});

			it('should get its dimensions from the received layout', function() {
				var msg = { val: layout };
				dispatcher.trigger(commons.osc.LAYOUT_RESPONSE, msg);
				expect(app.get('dimensions')).toEqual([ x = 1024, y = 768 ]);
			});

			it('should fail if target device is unknown', function() {
				expect(function() { app.setDimensions('Atari 2600'); }).toThrow(new Error('target device definition not supported.'));	
			});

			// this doesn't work because it doesn't track the
			// layout layout structure anymore. Should be checks
			// if single layout components are created. 
			/*
	it('should build the page objects tree', function() {
		app.createPageTree(layout.val.pages);
		expect(app.get('pageTree')).toBeDefined();
	});
			 */


			// should be checked in the PageHostView or PageHostModel
			/*
	it('should fail if the page object is empty or undefined', function() {
		expect(function() { app.createPageTree(); }).toThrow(new Error('No pages found in layout definition.'));
	});
			 */

			it('should create the app component "menu"', function() {

			});

			it('should create the app component "content"', function() {

			});


		});
	});
});