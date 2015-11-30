/**
 * This is the bootstrap of the Stereoscope webclient.
 * It just sets up paths and library dependencies
 * and starts the app. 
 * 
 * @author jansen
 * 
 */
require.config({
	
	paths: {
		//require plugins
		//async: 'lib/requireplugins/async',
        //font: 'lib/requireplugins/font',
        //goog: 'lib/requireplugins/goog',
        //image: 'lib/requireplugins/image',
        //json: 'lib/requireplugins/json',
        //noext: 'lib/requireplugins/noext',
        //mdown: 'lib/requireplugins/mdown',
        //propertyParser : 'lib/requireplugins/propertyParser',
		
		modernizr: 'lib/modernizr',
		jquery: 'lib/jquery-2.1.1',
		"jquery-cookie": 'lib/jquery.cookie',
	    underscore: 'lib/underscore',
	    backbone: 'lib/backbone',
	    "backbone-super": 'lib/backbone-super',
	    sockjs: 'lib/sockjs-0.3.4',
	    fastclick: 'lib/fastclick',
	    easel: 'lib/easeljs-0.8.0.combined',
	    tween: 'lib/tweenjs-0.6.0.min',
	    preload: 'lib/preloadjs-0.6.0.min',
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
	
	/**
	 * The shim config is used to set up scripts
	 * that are not compatible with the AMD structure.
	 */
	wrapShim: true,
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
		
		modernizr: { exports: 'Modernizr' },
		
		"jquery-cookie": [ 'jquery' ],
		
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
	}

});

require(['loader'], {});
