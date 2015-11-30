/**
 * This script sets up everything and starts the app.
 * First we have to check if all parts of the app
 * have finished loading. Using onload wouldn't work
 * because different browsers have different definitions
 * of done.
 * 
 * @author jansen
 * 
 */

(function() {

	$(document).ready(function() {
		
		/**
		 * Initialize foundation framework:
		 * Set behaviour for off-canvas menus.
		 * Should be recalled after content based on
		 * foundation stuff are rendered to the main div.
		 */
		window.initFoundation = function() {
			$(document).foundation({
				offcanvas : {
					// Sets method in which offcanvas opens.
					// [ move | overlap_single | overlap ]
					open_method: 'move', 
					// Should the menu close when a menu link is clicked?
					// [ true | false ]
					close_on_click: true
				}
			});
		};
		
		initFoundation();
		
		/**
		 * Convenience attribute as drop-in replacement
		 * for jquery "$" selector. Uses native
		 * "querySelectorAll()" when available and
		 * can be up to 20% faster compared to jquery.
		 */
		window.S = Foundation.utils.S;

		//sk.stsc.resources.svg = new Array;
		sk.stsc.resources.img = new Array;
		sk.stsc.resources.audio = new Array;
		sk.stsc.resources.json = new Array;
		var queue = new createjs.LoadQueue(true, '/stsc/resources/');
		queue.on('complete', handleComplete, this);
		
		queue.loadManifest([
		        {id: 'defaultLayout', src: 'default-layout.json'},
                {id: 'stereoscopeInv', src: 'stereoscope-inverted.png'},
                //{id: 'appbarLines', src: 'appbar.lines.horizontal.4.png'},
                {id: 'connected-icon-32x32-white', src: 'connected-icon-32x32-white.png'},
                {id: 'disconnected-icon-32x32-white', src: 'disconnected-icon-32x32-white.png'},
                //{id: 'disconnected-icon-64x64-blue', src: 'disconnected-icon-64x64-blue.png'},
                {id: 'appbarMixerMenu', src: 'menu-icon-mixer_32x32_white.png'},
                {id: 'touchosc_64x64', src: 'touchosc_64x64.png'}
                //{id: 'stereopongIntro', src: 'stereopong.mp3'}
				
		]);

		function handleComplete() {
			sk.stsc.resources.json['defaultLayout'] = queue.getResult('defaultLayout');
			sk.stsc.resources.img['stereoscopeInv'] = queue.getResult('stereoscopeInv');
			//sk.stsc.resources.img['appbarLines'] = queue.getResult('appbarLines');
			sk.stsc.resources.img['connected-icon'] = queue.getResult('connected-icon-32x32-white');
			sk.stsc.resources.img['disconnected-icon'] = queue.getResult('disconnected-icon-32x32-white');
			//sk.stsc.resources.img['disconnected-icon-big-blue'] = queue.getResult('disconnected-icon-64x64-blue');
			sk.stsc.resources.img['appbarMixerMenu'] = queue.getResult('appbarMixerMenu');
			sk.stsc.resources.img['touchosc_64x64'] = queue.getResult('touchosc_64x64');
			//sk.stsc.resources.audio['stereopongIntro'] = queue.getResult('stereopongIntro');
			//createjs.Sound.play(queue.getResult('stereopongIntro'));
			
			// start everything when assets are loaded
			//sk.log = log4javascript.getDefaultLogger();

			var dispatcher = _.extend({}, Backbone.Events);
			var websocket = new sk.stsc.model.SocketModel({
				socketPath: 'stereoscope', dispatcher: dispatcher});
			
			var layout = new sk.stsc.model.LayoutModel({dispatcher: dispatcher});
			var app = new sk.stsc.model.AppModel({dispatcher: dispatcher, layout: layout});
		}
		
		
	});

}());

			
