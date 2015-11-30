define(['underscore', 'backbone', 'Squire'],
		function(_, Backbone, Squire, AppmenuListModel) {

	var injector = new Squire();

	injector.mock('view/AppMenuListView', {
		testVal: 23
	})
	.require(['model/AppMenuListModel'], function(AppMenuListModel) {

		xdescribe('An AppMenuList Model', function() {
			var dispatcher, view, menuItems, menuList;

			beforeEach(function() {
				dispatcher = _.extend({}, Backbone.Events);
				view = 'brzz';
				menuItems = ['Mixer', 'Layouts', 'About'];
				menuList = new AppMenuListModel({view: view, dispatcher: dispatcher, menuItems: menuItems});
			});

			afterEach(function() {
				dispatcher, view, menuItems, menuList = null;
			});

			it('should be able to create its test objects', function() {
				expect(dispatcher).toBeDefined();
				expect(menuList).toBeDefined();
			});

			/*
			it('should fail if not properly initialized', function() {
				expect(function() { new AppMenuListModel(); }).toThrow(new Error());
				expect(function() { new AppMenuListModel({nastyArg: 'blublub'}); }).toThrow(new Error());
			});
			*/

			it('should create its own view', function() {
				expect(menuList.view.testVal).toEqual(23);
			});

		});
	});
});