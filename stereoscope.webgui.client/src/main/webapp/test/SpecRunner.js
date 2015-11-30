/**
 * SpecRunner.js
 * This is the main file of the spec suite
 * that does the following:
 *     Load dependencies (core libs, testings libs)
 *     Configure the testing environment
 *     Load the test suites
 *     Run the test engine 
 */
var jmPath = '../test/lib/jasmine-2.2.0/';

require.config({
	baseUrl: "js",
	urlArgs: "bust=" +  (new Date()).getTime(),
	
	paths: {
		jquery: 'lib/jquery-2.1.1',
	    underscore: 'lib/underscore',
	    backbone: 'lib/backbone',
	    //"backbone-super": appPath + 'lib/backbone-super',
	    easel: 'lib/easeljs-0.8.0.combined',
	    fastclick: 'lib/fastclick',
	    foundation: 'lib/foundation/foundation',
	    jasmine: jmPath + 'jasmine',
	    'jasmine-html': jmPath + 'jasmine-html',
		'jasmine-boot': jmPath + 'boot',
		'jasmine-jquery': jmPath + 'jasmine-jquery',
		//'blanket': 'lib/blanket_jasmine',
		
		//'jasmine-blanket': 'lib/blanket_jasmine'
		spec: '../test/spec/'
	},
	
	shim: {
		easel: { exports: 'createjs' },

		foundation: [ 'jquery', 'fastclick' ],
		
		'jasmine-boot': {
			deps: ['jasmine', 'jasmine-html'],
			exports: 'jasmine'
		},

		'jasmine-html': {
			deps: ['jasmine']
		},
		
		'jasmine-jquery': {
			deps: ['jasmine-boot']
		}
		/*
		'blanket': {
			deps: ['jasmine-boot'],
			exports: 'blanket'
		}
	*/
	}
});

require(['jquery', 'jasmine-boot', 'jasmine-jquery'], function($, jasmine) {
	var jasmineEnv = jasmine.getEnv();
	var specs = [];
	var sp = '../test/spec/';

	//jasmineEnv.addReporter(new jasmine.BlanketReporter());
	//jasmineEnv.updateInterval = 1000;

	/*
	jasmineEnv.specFilter = function() {
		return htmlReporter.specFilter(spec);
	};
*/
	
	specs.push(sp + 'AppMenuItemViewSpec');
	specs.push(sp + 'FakeSockJSModelSpec');
	specs.push(sp + 'MediaQueryModelSpec');
	//specs.push(sp + 'PongModelSpec');
	//specs.push(sp + 'PongViewSpec');
	specs.push(sp + 'commonsSpec');
	

	$(document).ready(function() {
		require(specs, function(spec) {
			window.onload();
		});
	});
});