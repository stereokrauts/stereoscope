define(['underscore', 'backbone', 'view/AppMenuItemView'],
		function(_, Backbone, AppMenuItemView) {

	xdescribe('An AppMenuItem View', function() {
		var dispatcher, itemView;

		beforeEach(function() {
			dispatcher = _.extend({}, Backbone.Events);
			itemView = new AppMenuItemView({dispatcher: dispatcher, name: 'test'});
		});

		afterEach(function() {
			dispatcher = null;
			itemView = null;
		});

		it('should be able to create its test objects', function() {
			expect(dispatcher).toBeDefined();
			expect(itemView).toBeDefined();
		});

		/*
		it('should fail if not properly initialized', function() {
			expect(function() { new AppMenuItemView(); }).toThrow(new Error());
			expect(function() { new AppMenuItemView({nastyArg: 'blublub'}); }).toThrow(new Error());
		});
*/
		describe('has a default value which', function() {
			//it('is a blublub', function() {
			//expect(blublub).toEqual('something');
			//});
		});

	});
});