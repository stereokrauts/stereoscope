/**
 * This file serves as an entry point for the build system.
 * main.js includes path instructions that would point to
 * nowhere in the product so it won't work.
 */
define([], function() {
	
	require(['app'], function(app) {
		
		app.init();

	});
	
});