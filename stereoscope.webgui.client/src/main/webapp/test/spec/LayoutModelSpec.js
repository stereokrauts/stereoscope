/**
 * The spec for the LayoutModel class.
 * 
 *  @author jansen
 */
define(['underscore', 'backbone', 'model.layout/LayoutModel'],
		function(_, Backbone, LayoutModel) {

	xdescribe('A Layout Model', function() {
		var dispatcher, layoutModel, objTree = null;
		var layout = "{"
			+ "	\"setup\": {"
			+ "		\"version\": 1.2,"
			+ "		\"mode\": \"IPAD_LANDSCAPE\","
			+ "		\"description\": \"A simple layout for spec/tests.\""	
			+ "	},"
			+ "	\"mixer\": {"
			+ "		\"mixerModel\": \"Yamaha 01v96\","
			+ "		\"channelCount\": 40,"
			+ "		\"auxCount\": 8,"
			+ "		\"busCount\": 8,"
			+ "		\"matrixCount\": 0"
			+ "	},"
			+ "	\"pages\": {"
			+ "		\"page1\": {"
			+ "			\"name\": \"inputs\","
			+ "			\"controls\": ["
			+ "				{"
			+ "					\"type\": \"basicFader\","
			+ "					\"name\": \"basicFader input1\","
			+ "					\"oscMessage\": \"/stereoscope/input/1/level\","
			+ "					\"dataType\": \"float\","
			+ "					\"initValue\": 0.0,"
			+ "					\"coords\": {\"x\":20,\"y\":50},"
			+ "					\"width\": 30,"
			+ "					\"height\": 200,"
			+ "					\"color\": \"red\","
			+ "					\"visibility\": true,"
			+ "					\"orient\": \"horizontal\","
			+ "					\"mode\": \"normal\""
			+ "				}"
			+ "			]"
			+ "		}"
			+ "	}"
			+ "}";

		/*
	sk.stsc.resources.json = new Array;
	var queue = new createjs.LoadQueue();
	queue.on('complete', handleComplete, this);
	queue.loadFile({id: 'defaultLayout', src: '/stsc/resources/geometic_cornersvg.svg'});
	function handleComplete() {
		console.log(queue.getResult('defaultLayout'));
		sk.stsc.resources.json['defaultLayout'] = queue.getResult('defaultLayout');
	};
		 */


		beforeEach(function() {
			dispatcher = _.extend({}, Backbone.Events);
			layoutModel = new LayoutModel({dispatcher: dispatcher});
			objTree = JSON.parse(layout);
		});

		afterEach(function() {
			dispatcher, layoutModel, objTree = null;
		});

		it('should be able to create its test objects', function() {
			expect(dispatcher).toBeDefined();
			expect(layoutModel).toBeDefined();
		});

		/*
		it('should fail if not properly initialized', function() {
			expect(function() { new LayoutModel(); }).toThrow(new Error('Illegal LayoutModel object initialization: TypeError: options is undefined'));
			expect(function() { new LayoutModel({nastyArg: 'blublub'}); }).toThrow(new Error('Illegal LayoutModel object initialization: Argument "dispatcher" missing.'));
		});
		*/

		describe('has lots of event handling and', function() {
			it('should request a layout if socket is open', function() {

			});

			it('should load a fallback layout if socket is closed (demo mode)', function() {

			});

			it('should process a layout that gets delivered from the bus', function() {

			});

			it('should process a page switch event', function() {

			});

		});

		it('should completely reset all layout references', function() {

		});


		/*
	it('should load a page list from a layout object', function() {
		layoutModel.setPageTree(objTree.pages);
		expect(layoutModel.get('pageList').length).toEqual(1);
	});

		 */
		/*
	it('should be able to load a default layout (for testing and fallback usage)', function() {
		layoutModel.setDefaultLayout();
		expect(layoutModel.get('setup.description')).toContain('A fallback layout for testing and standalone usage.');
	});
		 */

		/*
	it('should load layouts that are delivered from the dispatcher', function() {
		spyOn(layoutModel, 'handleLayoutResponse');
		dispatcher.trigger(static.osc.LAYOUT_RESPONSE, objTree);
		expect(layoutModel.handleLayoutResponse).toHaveBeenCalled();
	});

	it('should reset the layout structure', function() {
		layoutModel.setPageTree(objTree.pages);
		expect(layoutModel.get('pageList').length).not.toEqual(0);
		layoutModel.resetLayout();
		expect(layoutModel.get('pageList').length).toEqual(0);
	});
		 */
	});
});