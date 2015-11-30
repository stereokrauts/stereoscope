/**
 * This model holds the logic for all things Pong. 
 */
sk.stsc.model.PongModel = Backbone.Model.extend({
	
	dispatcher: null,
	view: null,
	
	defaults: {
		dimensions: statics.layout['IPAD_LANDSCAPE'],
		text1: 'STEREOPONG',
		text2: 'Move fader on ch1 to change title speed',
		text3: 'Press Mute1/F1 to start',
		text4: 'Stereokrauts are back with another mindblowing release: STEREOPONG!!! '
			+ 'It\'s a technology demo to show what\'s possible with Stereoscope. '
			+ 'Instead of controlling the mixer, the mixer is utilized a game controller. '
			+ 'Just move the fader on channel1 to change the tempo of the center animation. '
			+ 'Press channel1 mute to skip the intro and have a game of pong. '
			+ 'The idea to this evolved a long time ago when we saw a pong layout '
			+ 'for the "lemur" audiocontroller. We forgot about it until a few weeks '
			+ 'ago when we did the layout code. "We" is Tobias "Darth Fader" Heide '
			+ 'and Roland "Errorizor" Jansen. Music done by Johan "Zyron" Astrand of "Deceit". '
			+ 'Greetings to Arthur K., Tobias W., Katsuo K., Nico S., Stefan E. and all of our '
			+ 'customers. Enjoy the game... ',
		text5: 'STEREOKRAUTS - Home Of The Real Cranks'
		
	},
	
	initialize: function(options) {
		try {
			if (options.dispatcher && options.el) {
				this.dispatcher = options.dispatcher;
			} else {
				throw 'Argument "dispatcher" and/or "el" missing.';
			}
		} catch(e) {
			var msg = 'Illegal PongModel object initialization: ' + e;
			throw new Error(msg);
		}
		
		this.view = new sk.stsc.view.PongIntroView({model: this, el: options.el});
	},
	
	
	
	
});