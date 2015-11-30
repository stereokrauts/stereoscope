/**
 * A basic label view like in TouchOSC. 
 */
define([
  'jquery',
  'underscore',
  'backbone',
  'commons',
  'view.controls/ControlView'
], function($, _, Backbone, commons, ControlView) {

	var LabelBasicView = ControlView.extend({

		label: null,

		initialize: function(options) {
			this._super(options);

			this.setupLabel();
			this.model.on('change:valueFromMixer', this.handleFaderUpdate, this);
		},

		setupLabel: function() {
			this.setupContainer();
			this.addContainerToStages();
			this.rotation = this.setRotation(this.model.get('orient'));
			this.rotateContainer(this.rotation);
			this.setContainerCoords(
					this.model.get('coords').x,
					this.model.get('coords').y
			);

			this.label = new createjs.Text(this.model.get('valueFromMixer'),
					'normal ' + this.model.get('textSize') + 'px Arial',
					this.model.get('color'));

			if (this.model.get('hasEdge')) {
				this.drawEdge();
			}
			if (this.model.get('hasBackground')) {
				this.drawBackground();
			}

			this.drawLabel();
		},

		drawBackground: function() {
			var rgb, rgba;
			if (this.model.get('bgColor')) {
				console.log(this.model.get('bgColor'));
				rgb = commons.color.convertHexToRgb(this.model.get('bgColor'));
				rgba = 'rgba(' + rgb.r + ',' + rgb.g + ',' + rgb.b + ',0.3)';
			} else {
				rgb = commons.color.convertHexToRgb(this.model.get('color'));
				rgba = 'rgba(' + rgb.r + ',' + rgb.g + ',' + rgb.b + ',0.3)';
			}

			var background = new createjs.Shape();
			background.graphics.beginFill(rgba)
			.drawRoundRect(1, 1, this.model.get('width') - 1, this.model.get('height') - 1, 5);
			this.container.background.addChild(background);
		},

		drawEdge: function() {
			var width = this.model.get('width');
			var height = this.model.get('height');

			var innerLightGray = 'rgba(160, 160, 160, 1)';
			var shinyShadow = new createjs.Shape();
			shinyShadow.graphics.beginFill(innerLightGray)
			.drawRoundRect(x, y, width, height, 5);

			var darkShadow = new createjs.Shape();
			darkShadow.graphics.beginFill('rgba(0, 0, 0, 1')
			.drawRoundRect(x, y, width, height - 2, 4);

			var labelBg = new createjs.Shape();
			labelBg.graphics.beginFill('#3c3c3c')
			.drawRoundRect(x + 1, y + 1, width - 2, height - 2, 3);

			this.container.background.addChild(shinyShadow);
			this.container.background.addChild(darkShadow);
			this.container.background.addChild(labelBg);
		},

		drawLabel: function() {
			var textWidth = this.label.getBounds().width;
			var textHeight = this.label.getBounds().height;

			this.label.x = this.model.get('coords').x
			+ this.model.get('width')/2
			- textWidth/2;
			this.label.y = this.model.get('coords').y 
			+ this.model.get('height')/2
			- textHeight/2;

			this.stages.background.addChild(this.label);
		},

		addElementsToStages: function() {
			this.addContainerToStages();
		},

		handleLabelUpdate: function(text) {
			this.label.text = text;
		},

		render: function() {
			this.stages.background.update();
			return this;
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

	return LabelBasicView;
});