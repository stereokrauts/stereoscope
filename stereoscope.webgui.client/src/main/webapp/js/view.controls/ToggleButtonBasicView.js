/**
 * A basic toggle button view like in TouchOSC.
 */
define([
  'jquery',
  'underscore',
  'backbone',
  'view.controls/ControlView'
], function($, _, Backbone, ControlView) {

	var ToggleButtonBasicView = ControlView.extend({

		buttonBackground: null,
		buttonEdge: null,
		hitArea: null,
		edgeCurving: 5,

		initialize: function(options) {
			this._super(options);

			_(this).bindAll('handleMouseClick', 'render', 'renderOnOffStatus');

			// should only render the background, not the button in whole
			this.model.on('change:oscValue', this.renderOnOffStatus);
			this.model.set('type', 'basicToggleButton');

			this.setupButton();
		},

		setupButton: function() {
			this.setupContainer();
			this.addContainerToStages();
			this.setupButtonShapes();

			this.drawBackground();
			this.drawEdges();
			this.drawHitArea();

			this.addElementsToContainer();
			this.container.interactive.addEventListener('click', this.handleMouseClick);
		},

		addElementsToStages: function() {
			this.addContainerToStages();
		},

		render: function() {
			this.setContainerCoords(
					this.model.get('coords').x,
					this.model.get('coords').y);
			this.drawEdges();
			this.drawHitArea();
			this.model.get('oscValue') ? this.drawBackground() : this.container.interactive.removeChild(this.buttonBackground);
			this.stages.interactive.update();

			return this;
		},

		renderOnOffStatus: function() {
			this.model.get('oscValue') ? this.drawBackground() : this.container.interactive.removeChild(this.buttonBackground);
			this.stages.interactive.update();
		},

		addElementsToContainer: function() {
			this.container.interactive.addChild(this.hitArea);
			this.container.interactive.addChild(this.buttonEdge);

		},

		setupButtonShapes: function() {
			this.buttonBackground = new createjs.Shape();
			this.buttonEdge = new createjs.Shape();
			this.hitArea = new createjs.Shape();
		},

		drawBackground: function() {
			this.buttonBackground.graphics.beginFill(
					this.model.get('color')).drawRoundRect(
							0, 0,
							this.model.get('width'),
							this.model.get('height'),
							this.edgeCurving);
			this.buttonBackground.mouseEnabled = false;
			this.container.interactive.addChild(this.buttonBackground);
		},

		drawEdges: function() {
			this.buttonEdge.graphics.beginStroke(
					this.model.get('color')).drawRoundRect(
							0, 0,
							this.model.get('width'),
							this.model.get('height'),
							this.edgeCurving);
			this.buttonEdge.mouseEnabled = false;
		},

		drawHitArea: function() {
			this.hitArea.graphics.beginFill(
			'#000').drawRoundRect(
					0, 0,
					this.model.get('width'),
					this.model.get('height'),
					this.edgeCurving);

		},

		handleMouseClick: function(event) {
			//this.model.set('oscValue', !this.model.get('oscValue'));
			this.model.set('valueFromView', !this.model.get('oscValue'));
			console.log("oscVal: " + this.model.get('oscValue') + " , fromView: " + this.model.get('valueFromView'));
			this.renderOnOffStatus();
		},

		printProperties: function() {
			console.log('Control view related properties of this object:');
			console.log('osc: ' + this.model.get('oscAddress'));
			console.log('x: ' + this.model.get('coords').x);
			console.log('y: ' + this.model.get('coords').y);
			console.log('width:' + this.model.get('width'));
			console.log('heigth:' + this.model.get('height'));
		}	
	});

	return ToggleButtonBasicView;
});