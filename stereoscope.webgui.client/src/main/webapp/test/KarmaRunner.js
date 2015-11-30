var allTestFiles = [];
var TEST_REGEXP = /(spec|test)\.js$/i;

var pathToModule = function(path) {
	return path.replace(/^\/base\/js\//, '').replace(/\.js$/, '');
	//return path.replace(/^\/base\/js\//, '');
};

Object.keys(window.__karma__.files).forEach(function(file) {
	if (TEST_REGEXP.test(file)) {
		// Normalize paths to RequireJS module names.
		//allTestFiles.push(pathToModule(file));
		allTestFiles.push(file);
	}
});

require.config({
	// Karma serves files under /base, which is the basePath from your config file
	baseUrl: '/base/js',

	paths: {
		Squire: 'lib/Squire',
		jquery: 'lib/jquery-2.1.1',
		underscore: 'lib/underscore',
		backbone: 'lib/backbone',
		"backbone-super": 'lib/backbone-super',
		sockjs: 'lib/sockjs-0.3.4',
	    fastclick: 'lib/fastclick',
	    easel: 'lib/easeljs-0.8.0.combined',
	    tween: 'lib/tweenjs-0.6.0.min',
	    preload: 'lib/preloadjs-0.6.0.min',
	    modernizr: 'lib/modernizr',
	    foundation: 'lib/foundation/foundation',
	    "foundation.abide": 'lib/foundation/foundation.abide',
	    "foundation.accordion": 'lib/foundation/foundation.accordion',
	    "foundation.alert": 'lib/foundation/foundation.alert',
	    "foundation.clearing": 'lib/foundation/foundation.clearing',
	    "foundation.dropdown": 'lib/foundation/foundation.dropdown',
	    "foundation.equalizer": 'lib/foundation/foundation.equalizer',
	    "foundation.interchange": 'lib/foundation/foundation.interchange',
	    "foundation.joyride": 'lib/foundation/foundation.joyride',
	    "foundation.magellan": 'lib/foundation/foundation.magellan',
	    "foundation.offcanvas": 'lib/foundation/foundation.offcanvas',
	    "foundation.orbit": 'lib/foundation/foundation.orbit',
	    "foundation.reveal": 'lib/foundation/foundation.reveal',
	    "foundation.slider": 'lib/foundation/foundation.slider',
	    "foundation.tab": 'lib/foundation/foundation.tab',
	    "foundation.tooltip": 'lib/foundation/foundation.tooltip',
	    "foundation.topbar": 'lib/foundation/foundation.topbar'
	},
	
	shim: {
		easel: { exports: 'createjs' },
		
		tween: {
			deps: ['easel'],
			exports: 'createjs.Tween'
		},
		preload: { 
			deps: ['easel'],
			exports: 'createjs.LoadQueue' 
		},
		
		foundation: 			[ 'jquery', 'fastclick' ],
		"foundation.abide": 	[ 'foundation' ],
		"foundation.accordion": [ 'foundation' ],
		"foundation.alert": 	[ 'foundation' ],
		"foundation.clearing": 	[ 'foundation' ],
		"foundation.dropdown": 	[ 'foundation' ],
		"foundation.equalizer": [ 'foundation' ],
		"foundation.interchange": [ 'foundation' ],
		"foundation.joyride": 	[ 'foundation' ],
		"foundation.magellan": 	[ 'foundation' ],
		"foundation.offcanvas": [ 'foundation' ],
		"foundation.orbit": 	[ 'foundation' ],
		"foundation.reveal": 	[ 'foundation' ],
		"foundation.slider": 	[ 'foundation' ],
		"foundation.tab": 		[ 'foundation' ],
		"foundation.tooltip": 	[ 'foundation', 'modernizr' ],
		"foundation.topbar": 	[ 'foundation' ]
	},

	//	dynamically load all test files
	deps: allTestFiles,

	//	we have to kickoff jasmine, as it is asynchronous
	callback: window.__karma__.start
});
