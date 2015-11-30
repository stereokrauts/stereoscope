/**
 * The standard rotary view.
 * degrees/radians are inverted because y-axis coordinates
 * go down (rather than up in a normal coordinate-system).
 */
define([
  'jquery',
  'underscore',
  'backbone',
  'commons',
  'view.controls/ControlView'
], function($, _, Backbone, commons, ControlView) {

	var RotaryStdView = ControlView.extend({

		currentMouseYPos: null,

		// properties related to drawing
		colorRingBg: null,
		rotaryKnob: null,
		outerArc: null,
		innerArc: null,
		delimiter1: null,
		delimiter2: null,
		levelNeedle: null,
		levelDot: null,
		hitArea: null,
		levelDotRadius: null,
		arcFill: null,
		rotation: 0,
		outerRadius: null,
		innerRadius: null,
		knobRadius: null,
		centerX: null,
		centerY: null,
		nullRadian: Math.PI*3 / 4, //135°
		fullRadian: Math.PI / 4,   //45°
		range: Math.PI*3 / 2,      //360°-(135°-45°)=270°
		colorWithAlpha: null,

		initialize: function(options) {
			this._super(options);

			this.model.on('floatVal', this.updateNeedle);
			_.bindAll(this, 'handleMouseMove', 'handleMouseUp', 'render', 'updateNeedle'/*, 'drawLevelNeedle'*/);
			this.model.on('change:valueFromMixer', this.handleRotaryUpdate, this);
			this.setupRotary(options.orient);
		},

		setupRotary: function(orient) {
			this.setupContainer();
			this.setupShapes();
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
			this.colorWithAlpha = this.getRgba(1);
			this.setRadii(this.model.get('width'));
			this.setCenterPoint(this.model.get('width'));
			this.levelDotRadius = this.model.get('width') / 20;
			this.drawColorRingBg();
			this.drawKnob();
			this.updateNeedle(this.model.get('oscValue'));
			this.container.background.addChild(
					//this.hitArea,
					this.colorRingBg
			);

			this.container.foreground.addChild(//this.hitArea,
					//this.colorRingBg,
					this.arcFill,
					this.rotaryKnob
					//this.levelDot
			);

			this.container.interactive.addChild(
					this.hitArea,
					//this.colorRingBg,
					//this.rotaryKnob,
					//this.arcFill
					//this.outerArc,
					//this.innerArc,
					//this.delimiter1,
					//this.delimiter2,
					//this.levelNeedle
					this.levelDot
			);
			//this.rotation = this.setRotation(orient);
			//this.container.rotation = this.rotation;
			this.setHitArea();
			this.container.interactive.addEventListener('pressmove', this.handleMouseMove);
			this.container.interactive.addEventListener('pressup', this.handleMouseUp);
		},

		/**
		 * This one is tricky:
		 * In order to get an invisible hit area
		 * we use a shape without graphics and replace its
		 * hitArea object with another shape with graphics.
		 * Now the hitArea is not transparent but also not visible ;-)
		 */
		setHitArea: function() {
			var ha = new createjs.Shape();
			ha.graphics.beginFill("#000").rect(0, 0, 
					this.model.get('width'), 
					this.model.get('height'));
			this.hitArea.hitArea = ha;
		},

		setupShapes: function() {
			this.colorRingBg = new createjs.Shape();
			this.rotaryKnob = new createjs.Shape();
			this.outerArc = new createjs.Shape();
			this.innerArc = new createjs.Shape();
			this.delimiter1 = new createjs.Shape();
			this.delimiter2 = new createjs.Shape();
			this.levelNeedle = new createjs.Shape();
			this.levelDot = new createjs.Shape();
			this.arcFill = new createjs.Shape();
			this.hitArea = new createjs.Shape();
		},

		render: function() {
			//this.drawHitArea();
			//this.drawColorRingBg();
			//this.drawKnob();
			//this.drawLevelDot(this.nullRadian);
			//this.updateNeedle(this.model.get('oscValue'));
			//this.drawArc(this.innerArc, this.innerRadius);
			//this.drawArc(this.outerArc, this.outerRadius);
			//this.drawDelimiter(this.delimiter1, this.nullRadian);
			//this.drawDelimiter(this.delimiter2, this.fullRadian);
			//this.drawLevelNeedle(this.nullRadian);
			this.stages.background.update();
			this.stages.foreground.update();
			this.stages.interactive.update();
			return this;
		},

		setRadii: function(width) {
			this.outerRadius = width/2;
			this.innerRadius = width/2.5;
			this.knobRadius = width/3;
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

		drawColorRingBg: function() {
			var rgb = commons.color.convertHexToRgb(commons.color.GRAY);
			var color = 'rgba(' + rgb.r + ',' + rgb.g + ',' + rgb.b + ',1)';
			this.colorRingBg.graphics.beginFill(color)
			.moveTo(this.centerX, this.centerY)
			.arc(this.centerX, this.centerY, this.outerRadius, this.nullRadian, this.fullRadian)
			.endFill().beginFill(commons.color.ui.layoutBg)
			.drawCircle(this.centerX, this.centerY, this.innerRadius);
		},

		drawKnob: function() {
			var knobTop = new createjs.Shape();
			var knobRim = new createjs.Shape();



			this.rotaryKnob.graphics.beginFill('rgba(0, 0, 0, 1)')
			.drawCircle(this.centerX, this.centerY + 1, this.knobRadius)
			.beginLinearGradientFill(['rgba(240,240,240,1)', 'rgba(20,20,20,1)'],
					[0, 1], this.centerX, 0, this.centerX, this.model.get('height'))
					//.moveTo(this.centerX, this.centerY)
					.drawCircle(this.centerX, this.centerY, this.knobRadius)
					.beginLinearGradientFill(['rgba(130,130,130,1)', 'rgba(80,80,80,1)'],
							[0, 0.8], this.centerX, 0, this.centerX, this.model.get('height'))
							.drawCircle(this.centerX, this.centerY, this.knobRadius - 2);

		},

		drawDelimiter: function(shape, radian) {
			var startVector = this.radianToVector(this.innerRadius, radian);
			var endVector = this.radianToVector(this.outerRadius, radian);
			shape.graphics.moveTo(this.centerX + startVector.x, this.centerY + startVector.y);
			shape.graphics.beginStroke(
					this.model.get('color')
			).lineTo(this.centerX + endVector.x, this.centerY + endVector.y);
		},

		/*
	drawLevelNeedle: function(radian) {
		var vector = this.radianToVector(this.outerRadius - 10, radian);
		this.levelNeedle.graphics.moveTo(this.centerX, this.centerY);
		this.levelNeedle.graphics.beginStroke(
				this.model.get('color')).lineTo(
						this.centerX + vector.x,
						this.centerY + vector.y);
	},
		 */

		drawLevelDot: function(radian) {
			var vector = this.radianToVector(this.knobRadius - this.levelDotRadius - 4, radian);
			this.levelDot.graphics.beginFill('rgba(0, 0, 0, 1)')
			.drawCircle(
					this.centerX + vector.x,
					this.centerY + vector.y - 1, this.levelDotRadius)
					.beginLinearGradientFill(['rgba(100, 100, 100, 1)', 'rgba(255, 255, 255, 1)' ],
							[0, 1], this.centerX + vector.x, this.centerY + vector.y - this.levelDotRadius,
							this.centerX + vector.x, this.centerY + vector.y + this.levelDotRadius)
							.drawCircle(
									this.centerX + vector.x,
									this.centerY + vector.y, this.levelDotRadius);
		},

		drawColorRing: function(radian) {
			var startRadian = this.nullRadian;
			//if (this.model.get('mode') === 'centered') {
			//startRadian = Math.PI / 2;
			//}

			this.arcFill.graphics.beginFill(this.colorWithAlpha)
			.moveTo(this.centerX, this.centerY)
			.arc(this.centerX, this.centerY, this.outerRadius, startRadian, radian)
			.endFill().beginFill(commons.color.ui.layoutBg)
			.drawCircle(this.centerX, this.centerY, this.innerRadius);

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
			//this.levelNeedle.graphics.clear();
			//this.drawLevelNeedle(radian);
			this.levelDot.graphics.clear();
			this.drawLevelDot(radian);
			this.arcFill.graphics.clear();
			this.drawColorRing(radian);
			this.stages.foreground.update();
			this.stages.interactive.update();
		},

		handleRotaryUpdate: function(ctx, evt) {
			this.updateNeedle(evt);
		},

		/* Futile due to easel api change
		handleMouseDown: function(event) {
			console.log('Mousedown in rotary detected.');
			this.currentMouseYPos = event.stageY;
			event.addEventListener('mousemove', this.handleMouseMove);
		},
		*/

		handleMouseUp: function(event) {
			console.log('Mouseup in rotary detected.');
		},

		handleMouseMove: function(event) {
			var deltaY = this.currentMouseYPos - event.stageY;
			console.log('oscValue: ' + this.model.get('oscValue'));
			var newValue = this.model.get('oscValue') + deltaY * 0.01;
			this.currentMouseYPos = event.stageY;
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

	return RotaryStdView;
});