/*!
Jasmine-Stereoscope: A set of matchers for the stereocope web-frontend.

 */
+function(jasmine) {
	
	beforeEach(function() {
		
		jasmine.Expectation.addMatchers({
			
			// these tests don't distinguish between
			// single easel objects (a container can be
			// tested to be a shape e.g.)
			toBeAnEaselStage: function() {
				return {
					compare: function(actual) {
						if (!actual.alpha) {
							return { pass: false };
						} else {
							return { pass: true};
						}
					}
				};
			},
			
			toBeAnEaselContainer: function() {
				return {
					compare: function(actual) {
						if (!actual) {
							return "Test object doesn't exist";
						} else {
							if (!actual.alpha) {
								return { pass: false };
							} else {
								return { pass: true };
							}
						}
					}
				};
			},
			
			toBeAnEaselShape: function() {
				return {
					compare: function(actual) {
						if (!actual) {
							return "Test object doesn't exist";
						} else {
							if (!actual.alpha) {
								return { pass: false };
							} else {
								return { pass: true };
							}
						}
					}
				};
			}
		});
	});
	
	afterEach(function() {
		// here goes event cleanup if any
	});
	
}(jasmine, window);
