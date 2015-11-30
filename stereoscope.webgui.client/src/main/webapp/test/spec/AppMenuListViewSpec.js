define(['underscore', 'backbone', 'view/AppMenuListView'],
		function(_, Backbone, AppMenuListView) {

	xdescribe('An AppMenuList View', function() {
		var menuListModel;
		var menuListView;

		beforeEach(function() {
			menuListModel = {
					
			};
			menuListView = new AppMenuListView({model: menuListModel});
		});

		afterEach(function() {
			menuListModel, menuListView = null;
		});

		it('should be able to create its test objects', function() {
			expect(menuListModel).toBeDefined();
			expect(menuListView).toBeDefined();
		});

		/*
		it('should fail if not properly initialized', function() {
			expect(function() { new AppMenuListView(); }).toThrow(new Error());
			expect(function() { new AppMenuListView({nastyArg: 'blublub'}); }).toThrow(new Error());
		});
		*/

	});
});