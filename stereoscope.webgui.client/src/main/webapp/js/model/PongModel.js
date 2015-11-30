/**
 * PongModel: The data model behind the pong game
 */
define(['underscore', 'backbone', 'view/PongView', 'preload'],
		function(_, Backbone, PongView) {
	
	var PongModel = Backbone.Model.extend({
		
		dispatcher: null,
		view: null,
		loader: null,
		
		defaults: {
			scoreP1: 0,
			scoreP2: 0,
			
			xSpeed: 5,
			ySpeed: 5,
			cpuPaddleSpeed: 6
		},
		
		initialize: function(options) {
			try {
				if (options.dispatcher) {
					this.dispatcher = options.dispatcher;
				} else {
					throw 'Argument "dispatcher" missing.';
				}
			} catch(e) {
				var msg = 'Illegal PongModel object initialization: ' + e;
				throw new Error(msg);
			}
			
			this.view = new PongView(this);
			this.loader = new createjs.LoadQueue(true, 'assets/');
			this.loadAssets();
			this.runGame();
		},
		
		loadAssets: function() {
			
		},
		
		runGame: function() {
			
		}
	
	});
	
	return PongModel;
});