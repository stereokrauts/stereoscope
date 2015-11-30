define(['underscore', 'backbone', 'model.layout/LayoutPageModel'],
		function(_, Backbone, LayoutPageModel) {

	xdescribe('A Layout Page Model', function() {
		var layoutPageModel, dispatcher, canvasStack;
		//var page = "\"page0\": {"
		var page = {
				name: "inputs",
				color: "gray",	
				controls: [
				           {
				        	   type: "basicFader",
				        	   name: "basicFader input1",
				        	   oscMessage: "/stereoscope/input/1/level",
				        	   dataType: "float",
				        	   initValue: 0.0,
				        	   coords: {"x":20,"y":50},
				        	   width: 30,
				        	   height: 200,
				        	   color: "red",
				        	   visibility: true,
				        	   orient: "horizontal",
				        	   mode: "normal"
				           }
				           ]
		};

		beforeEach(function() {
			dispatcher = _.extend({}, Backbone.Events);
			canvasStack = new CanvasStackView({
				el: $('<div/>').attr('id', 'canvasHost'),
				dimensions: statics.layout['IPAD_LANDSCAPE']
			});
			params = {
					pageData: page,
					globalPageList: new LayoutPagesCollection(),
					pageLevel: 1,
					pageId: 'page0',
					canvasDom: canvasStack.stack
			};

			layoutPageModel = new LayoutPageModel({
				dispatcher: dispatcher,
				params: params			
			});
		});

		afterEach(function() {
			dispatcher, canvasStack, params, layoutPageModel = null;
		});

		it('should be able to create its test objects', function() {
			expect(dispatcher).toBeDefined();
			expect(layoutPageModel).toBeDefined();
		});

		/*
		it('should fail if not properly initialized', function() {
			expect(function() { new LayoutPageModel(); }).toThrow(new Error('Illegal LayoutPageModel object initialization: TypeError: options is undefined'));
			expect(function() { new LayoutPageModel({nastyArg: 'blublub'}); }).toThrow(new Error('Illegal LayoutPageModel object initialization: Argument "dispatcher" and/or "params" missing.'));
		});
		*/

		describe('has default values including', function() {

		});

		it('should set its collection according to the page type (page or control host)', function() {

		});
	});
});