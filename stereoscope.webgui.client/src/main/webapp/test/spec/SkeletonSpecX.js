xdescribe('A test object', function() {
	var dispatcher, testobj;
	
	beforeEach(function() {
		dispatcher = _.extend({}, Backbone.Events);
	});
	
	afterEach(function() {
		dispatcher, testobj = null;
	});
	
	it('should be able to create its test objects', function() {
		expect(dispatcher).toBeDefined();
		expect(testobj).toBeDefined();
	});
	
	it('should fail if not properly initialized', function() {
		expect(function() { new testobj(); }).toThrow(new Error('Illegal <OBJ> object initialization: TypeError: options is undefined'));
		expect(function() { new testobj({nastyArg: 'blublub'}); }).toThrow(new Error('Illegal <OBJ> object initialization: Argument <ARG> missing.'));
	});
	
	xdescribe('has a default value which', function() {
		it('is a blublub', function() {
			expect(blublub).toEqual('something');
		});
	});
	
});