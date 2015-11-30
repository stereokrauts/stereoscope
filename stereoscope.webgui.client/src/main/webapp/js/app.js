/**
 * This is a kind of shell-module that sets up
 * assets and starts the router and dispatcher.
 */
define([
        'jquery',
        'underscore',
        'initFoundation',
        'modernizr',
        'model/SocketModel',
        'model/MediaQueryModel',
        'model/AppModel',
        'model.layout/LayoutModel',
        'preload',
        'foundation',
        'foundation.abide',
        'foundation.accordion',
        'foundation.alert',
        'foundation.clearing',
        'foundation.dropdown',
        'foundation.equalizer',
        'foundation.interchange',
        'foundation.joyride',
        'foundation.magellan',
        'foundation.offcanvas',
        'foundation.orbit',
        'foundation.reveal',
        'foundation.slider',
        'foundation.slider',
        'foundation.tab',
        'foundation.tooltip',
        'foundation.topbar',
        'jquery-cookie'
], function($, _, Foundation, Modernizr, SocketModel, MediaQueryModel, AppModel, LayoutModel) {
	'use strict';
	/**
	 * Initialize all app components and start the app.
	 */
	var initialize = function() {

		var errMsg = '';
		if (!Modernizr.canvas) {
			errMsg = 'Your browser doesn\'t have canvas support. Please upgrade and try again.';
			alert(errMsg);
			throw new Error(errMsg);
		} else if (!Modernizr.websockets) {
			errMsg = 'Your browser doesn\'t support websockets. Please upgrade and try again.';
			alert(errMsg);
			throw new Error(errMsg);
		}
		
		/**
		 * First we have to load all assets
		 */
		window.assets = window.assets || {};
		assets.img = [];
		var queue = new createjs.LoadQueue(true, 'assets/');
		queue.on('complete', handleComplete, this);
		queue.loadManifest([
		                    {id: 'connected-icon-32x32-white', src: 'icons/connected-icon-32x32-white.png'},
		                    {id: 'disconnected-icon-32x32-white', src: 'icons/disconnected-icon-32x32-white.png'},
		                    //{id: 'demo-mode-icon-32x32-white', src: 'icons/car-marriage-icon-32x32-white.png'},
		                    {id: 'demo-mode-icon-32x32-white', src: 'icons/demo-mode-icon-32x32-white.png'},
		                    {id: 'appbarMixerMenu', src: 'icons/menu-icon-mixer_32x32_white.png'}
		                    ]);

		/**
		 * When assets are loaded, start the app
		 */
		function handleComplete() {
			assets.img['connected-icon'] = queue.getResult('connected-icon-32x32-white');
			assets.img['disconnected-icon'] = queue.getResult('disconnected-icon-32x32-white');
			assets.img['demo-mode-icon'] = queue.getResult('demo-mode-icon-32x32-white');
			assets.img['appbar-mixer-menu'] = queue.getResult('appbarMixerMenu');

			$(document).ready(function() {
				Foundation.initFoundation();
				
				var dispatcher = _.extend({}, Backbone.Events);
				var mq = new MediaQueryModel({register: true});
				
				var layout = new LayoutModel({dispatcher: dispatcher, mq: mq});

				new SocketModel({
					socketPath: 'stereoscope',
					dispatcher: dispatcher,
					mq: mq
				});

				new AppModel({dispatcher: dispatcher, layout: layout});
			});

		}
	};

	return { init: initialize };
});
