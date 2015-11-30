/**
 * PongView - The view of the pong game
 */
define(['jquery', 'underscore', 'backbone', 'easel'],
		function($, _, Backbone, createjs) {
	
	var PongView = Backbone.View.extend({
		
		model: null,
		bgCanvas: null,
		fgCanvas: null,
		bgStage: null,
		fgStage: null,
		
		
		player1Paddle: null,
		player2Paddle: null,
		ball: null,
		
		player1Score: null,
		player2Score: null,
		
		initialize: function(options) {
			try {
				if (options.model, options.domId) {
					this.model = options.model;
					this.el = $('#' + options.domId);
				} else {
					throw 'Argument "model" missing.';
				}
			} catch(e) {
				var msg = 'Illegal PongView object initialization: ' + e;
				throw new Error(msg);
			}
			
		},
		
		setCanvases: function() {
			var w = $(window).width();
			var h = $(window).height();
			var bgCanvas = this.$el.append();
			var fgCanvas = this.$el.append();
		},
		
		setStages: function() {
			
		},
		
		setScores: function() {
			this.player1Score = new Text('0', 'bold 20px Arial', '#fff');
			this.player2Score = new Text('0', 'bold 20px Arial', '#fff');
			this.player1Score.x = 0;
			this.player1Score.y = 0;
			this.player2Score.x = 0;
			this.player2Score.y = 0;
		},
		
		setTicker: function() {
			
		},
		
		render: function() {
			
		},
		
		renderStartScreen: function() {
			
		},
		
		renderGameScreen: function() {
			
		},
		
		updateGameState: function() {
			
		},
		
		updatePaddle1: function() {
			
		},
		
		updatePaddle2: function() {
			
		},
		
		updateBall: function() {
			
		}
		
		
	});
	
	return PongView;	
});