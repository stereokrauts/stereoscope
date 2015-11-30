define(['underscore', 'backbone', 'view/AppView'],
		function(_, Backbone, AppView) {

	xdescribe('An App View', function() {
		var model, view;

		beforeEach(function() {
			model = {};
			view = new AppView({model: model});
		});

		afterEach(function() {
			model, view = null;
		});

		it('should be able to create its test objects', function() {
			expect(model).toBeDefined();
			expect(view).toBeDefined();
		});

		/*
		it('should fail if not properly initialized', function() {
			expect(function() { new AppView(); }).toThrow(new Error());
			expect(function() { new AppView({nastyArg: 'blublub'}); }).toThrow(new Error());
		});
		*/

		xdescribe('has a default value which', function() {
			it('is "tagName" of value "div"', function() {
				expect(view.tagName).toEqual('div');
			});

			it('is "id" of value "app"', function() {
				expect(view.id).toEqual('app');
			});

			it('is "mainArea" that represents a dom element', function() {
				// value is not testable
				expect(view.mainArea).toBeDefined();
			});
		});

		xdescribe('can render html and', function() {
			/**
			 * this is dangerous because it tests the cached
			 * results, not the resulting dom itself. It should
			 * be equal but it's not the same. Mocking the dom
			 * can be quite a complex task and is over the shelf.
			 */
			it('should return the view object after rendering', function() {
				expect(view.render()).toEqual(view);
			});

		});

	});
});