/**
 * A basic fader view like in TouchOSC.
 */
define([
  'jquery',
  'underscore',
  'backbone',
  'view.controls/ControlView'
], function($, _, Backbone, ControlView) {

	var FaderBasicView = ControlView.extend({

		offsetDuringMove: null,

		// properties related to drawing
		faderBackground: null,
		faderKnob: null,
		faderKnobHeight: 25,
		edgeCurving: 5,


		initialize: function(options) {
			this._super(options);

			_(this).bindAll('handleMouseDown', 'handleMouseMove');
			this.model.on('change:valueFromMixer', this.handleFaderUpdate, this);
			this.setupFader(options.orient);

		},

		setupFader: function(orient) {
			this.faderKnobHeight = this.model.get('width')*0.7;
			this.setupContainer();
			this.addContainerToStages();
			this.setupFaderShapes();
			this.rotation = this.setRotation(orient);
			console.log('rotation is: ' + this.rotation);
			this.rotateContainer(this.rotation);
		},


		getFaderKnobRange: function() {
			return this.model.get('height') - this.faderKnobHeight; 
		},

		render: function() {

			this.setContainerCoords(
					this.model.get('coords').x,
					this.model.get('coords').y);
			this.drawFaderBackground();
			this.drawFaderKnob();

			this.stages.background.update();
			this.stages.foreground.update();
			this.stages.interactive.update();
			return this;
		},

		setupFaderShapes: function() {
			this.faderBackground = new createjs.Shape();
			this.faderKnob = new createjs.Shape();
		},

		drawFaderBackground: function() {
			this.faderBackground.graphics
			.beginFill(
			'rgba(0, 0, 0, 1)').drawRoundRect(
					0, 0,
					this.model.get('width'),
					this.model.get('height'),
					this.edgeCurving
			).beginStroke(
					this.model.get('color')).drawRoundRect(
							0, 0,
							this.model.get('width'),
							this.model.get('height'),
							this.edgeCurving
					);
			this.faderBackground.mouseEnabled = false;
			this.container.background.addChild(this.faderBackground);
		},

		drawFaderKnob: function() {

			this.faderKnob.graphics.beginFill(
					this.model.get('color')).drawRoundRect(
							0, this.faderKnob.y,
							//0, 50,
							this.model.get('width'),
							this.faderKnobHeight,
							this.edgeCurving
					);
			//this.faderKnobHeight = this.faderKnob.getBounds().height;

			console.log('oscValue: ' + this.model.get('oscValue'));
			if (!this.model.get('oscValue')) {
				this.faderKnob.y = this.model.get('height') - this.faderKnobHeight;
			}
			steal.dev.log('xx faderKnobYPosition: ' + this.faderKnob.y);
			this.container.interactive.addChild(this.faderKnob);

			this.faderKnob.addEventListener('mousedown', this.handleMouseDown);
			this.faderKnob.addEventListener('mouseout', this.handleMouseUp);
		},

		addElementsToStages: function() {
			this.addContainerToStages();
		},

		handleFaderUpdate: function(ctx, evt) {
			console.log('e is: ' + evt);
			//this.faderKnob.y = this.model.get('faderKnobYPosition');
			this.faderKnob.y = - evt * (this.model.get('height') - this.faderKnobHeight);
			steal.dev.log("update fader to Y pos: " + this.faderKnob.y);
			this.stages.interactive.update();
		},

		handleMouseDown: function(event) {
			console.log("mouse down");
			this.offsetDuringMove = event.stageY;
			event.addEventListener('mousemove', this.handleMouseMove);
		},

		handleMouseUp: function(event) {
			this.faderKnob.unregisterEventListener('mousedown');
		},

		handleMouseMove: function(event) {
			var deltaY = event.stageY - this.offsetDuringMove;
			this.offsetDuringMove += deltaY;
			var newYCoord = this.faderKnob.y + deltaY;

			var minYCoord = 0;
			var maxYCoord = this.model.get('height') - this.faderKnobHeight;

			if (newYCoord >= minYCoord && newYCoord <= maxYCoord) {
				event.target.y = newYCoord;
				var newValue = (this.getFaderKnobRange() - newYCoord) / this.getFaderKnobRange();
				this.model.set('valueFromView', newValue);
				this.stages.interactive.update();
			}
		},

		printProperties: function() {
			console.log('Control view related properties of this object:');
			console.log('osc: ' + this.model.get('oscAddress'));
			console.log('x: ' + this.model.get('coords').x);
			console.log('y: ' + this.model.get('coords').y);
			console.log('width:' + this.model.get('width'));
			console.log('heigth:' + this.model.get('height'));
		},

	});

	return FaderBasicView;
});