/**
 * A basic rotary control like the one in TouchOSC.
 * Degrees/radians are inverted because y-axis coordinates
 * go down (rather than up in a normal coordinate-system).
 */
define([
  'jquery',
  'underscore',
  'backbone',
  'commons',
  'easel',
  'view.controls/ControlView'
], function($, _, Backbone, commons, easel, ControlView) {

	var RotaryBasicView = ControlView.extend({

		currentMouseYPos: null,

		// properties related to drawing
		background: new createjs.Shape(),
		outerArc: new createjs.Shape(),
		innerArc: new createjs.Shape(),
		delimiter1: new createjs.Shape(),
		delimiter2: new createjs.Shape(),
		levelNeedle: new createjs.Shape(),
		arcFill: new createjs.Shape(),
		hitArea: new createjs.Shape(),
		rotation: 0,
		outerRadius: null,
		innerRadius: null,
		centerX: null,
		centerY: null,
		nullRadian: Math.PI*3 / 4, //135°
		fullRadian: Math.PI / 4,   //45°
		range: Math.PI*3 / 2,      //360°-(135°-45°)=270°
		colorWithAlpha: null,

		initialize: function(options) {
			this._super(options);

			this.model.on('floatVal', this.updateNeedle);
			_(this).bindAll('handleMouseDown', 'handleMouseMove', 'render', 'updateNeedle', 'drawLevelNeedle');
			this.model.on('change:valueFromMixer', this.handleRotaryUpdate, this);
			this.setupRotary(options.orient);
		},

		setupRotary: function(orient) {
			this.setupContainer();
			this.addContainerToStages();
			this.rotation = this.setRotation(this.model.get('orient'));
			this.rotateContainer(this.rotation);
			this.setContainerCoords(
					this.model.get('coords').x,
					this.model.get('coords').y
			);



			if (!this.model.get('oscValue')) {
				this.model.set('oscValue', 0.0);
			}
			this.colorWithAlpha = this.getRgba(0.3);
			this.setRadii(this.model.get('width'));
			this.setCenterPoint(this.model.get('width'));
			this.container.interactive.addChild(this.hitArea,
					this.background,
					this.arcFill,
					this.outerArc,
					this.innerArc,
					this.delimiter1,
					this.delimiter2,
					this.levelNeedle
			);
			//this.rotation = this.setRotation(orient);
			//this.container.rotation = this.rotation;
			this.container.interactive.addEventListener('mousedown', this.handleMouseDown);
			this.container.interactive.addEventListener('mouseup', this.handleMouseUp);
		},

		render: function() {
			this.drawHitArea();
			this.drawBackground();
			this.drawArc(this.innerArc, this.innerRadius);
			this.drawArc(this.outerArc, this.outerRadius);
			this.drawDelimiter(this.delimiter1, this.nullRadian);
			this.drawDelimiter(this.delimiter2, this.fullRadian);
			this.drawLevelNeedle(this.nullRadian);

			this.stages.interactive.update();
			return this;
		},

		setRadii: function(width) {
			this.outerRadius = width/2;
			this.innerRadius = width/4;
		},

		setCenterPoint: function(width) {
			this.centerX = width/2;
			this.centerY = width/2;
		},

		drawArc: function(shape, radius) {
			shape.graphics.beginStroke(
					this.model.get('color')
			).arc(this.centerX, 
					this.centerY,
					radius,
					this.nullRadian,
					this.fullRadian
			);
		},

		drawBackground: function() {
			this.background.graphics.beginFill('rgba(0, 0, 0, 1)')
			.moveTo(this.centerX, this.centerY)
			.arc(this.centerX, this.centerY, this.outerRadius, this.nullRadian, this.fullRadian)
			.endFill().beginFill(commons.color.ui.layoutBg)
			.drawCircle(this.centerX, this.centerY, this.innerRadius);
		},

		drawDelimiter: function(shape, radian) {
			var startVector = this.radianToVector(this.innerRadius, radian);
			var endVector = this.radianToVector(this.outerRadius, radian);
			shape.graphics.moveTo(this.centerX + startVector.x, this.centerY + startVector.y);
			shape.graphics.beginStroke(
					this.model.get('color')
			).lineTo(this.centerX + endVector.x, this.centerY + endVector.y);
		},

		drawLevelNeedle: function(radian) {
			var vector = this.radianToVector(this.outerRadius, radian);
			this.levelNeedle.graphics.moveTo(this.centerX, this.centerY);
			this.levelNeedle.graphics.beginStroke(
					this.model.get('color')).lineTo(
							this.centerX + vector.x,
							this.centerY + vector.y);
		},

		drawArcFill: function(radian) {
			this.arcFill.graphics.beginFill(this.colorWithAlpha)
			.moveTo(this.centerX, this.centerY)
			.arc(this.centerX, this.centerY, this.outerRadius, this.nullRadian, radian)
			.endFill().beginFill(commons.color.ui.layoutBg)
			.drawCircle(this.centerX, this.centerY, this.innerRadius);

		},

		drawHitArea: function() {
			this.hitArea.graphics.beginFill(commons.color.ui.layoutBg).rect(
					0, 0,
					this.model.get('width'),
					this.model.get('height'));
		},

		addElementsToStages: function() {
			this.addContainerToStages();
		},

		updateNeedle: function(floatVal) {
			var radian;
			if (floatVal) {
				radian = this.range*floatVal + this.nullRadian;
			} else {
				radian = this.nullRadian;
			}
			console.log('radian (needle): ' + radian);
			this.levelNeedle.graphics.clear();
			this.drawLevelNeedle(radian);
			this.arcFill.graphics.clear();
			this.drawArcFill(radian);
			this.stages.interactive.update();
		},

		handleRotaryUpdate: function(ctx, evt) {
			console.log(evt);
			var newValue = 0;
		},

		handleMouseDown: function(event) {
			steal.dev.log('Mousedown in rotary detected.');
			this.currentMouseYPos = event.stageY;
			event.addEventListener('mousemove', this.handleMouseMove);
		},

		handleMouseUp: function(event) {
			steal.dev.log('Mouseup in rotary detected.');
		},

		handleMouseMove: function(event) {
			var deltaY = this.currentMouseYPos - event.stageY;
			console.log('oscValue: ' + this.model.get('oscValue'));
			var newValue = this.model.get('oscValue') + deltaY * 0.01;
			this.currentMouseYPos = event.stageY;
			steal.dev.log('mouse pos: ' + deltaY);
			if (newValue < 0) {
				newValue = 0;
			} else if (newValue > 1) {
				newValue = 1;
			}
			this.model.set('valueFromView', newValue);
			this.updateNeedle(newValue);
		},

		// not in use
		radianToAngle: function(radian) {
			return (radian * (180 / Math.PI));
		},

		radianToVector: function(radius, radian) {
			return {
				x: radius * Math.cos(radian),
				y: radius * Math.sin(radian)
			};
		},

		getRgba: function(alpha) {
			var rgb = commons.color.convertHexToRgb(this.model.get('color'));
			return 'rgba(' + rgb.r + ',' + rgb.g + ',' + rgb.b + ',' + alpha + ')';
		},

		printProperties: function() {
			console.log('rotary object properties:');
			console.log('centerX: ' + this.centerX);
			console.log('centerY: ' + this.centerY);
		}


	});

	return RotaryBasicView;
});