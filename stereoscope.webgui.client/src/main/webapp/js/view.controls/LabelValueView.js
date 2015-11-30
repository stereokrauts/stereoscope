/**
 * The standard label view.
 * It receives values from the server
 * and can have a background and/or frame.
 * There can be different colors for text and background.
 */
define([
  'jquery',
  'underscore',
  'backbone',
  'view.controls/ControlView'
], function($, _, Backbone, ControlView) {

	var LabelValueView = ControlView.extend({

		label: null,

		initialize: function(options) {
			this._super(options);

			this.setupLabel();
			this.model.on('change:valueFromMixer', this.handleLabelUpdate, this);
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

			if (this.model.get('hasBackground') === true) {
				this.drawBackground();
			}
			this.drawLabel();
		},

		drawBackground: function() {
			x = 0;
			y = 0;

			var width = this.model.get('width');
			var height = this.model.get('height');

			var labelInnerShadow = new createjs.Shape();
			labelInnerShadow.graphics.beginFill('rgba(0, 0, 0, 1')
			.drawRoundRect(x, y, width, height - 1, 4);

			//var innerLightGray = 'rgba(160, 160, 160, 1)';
			var innerLightGray = 'rgba(110, 110, 110, 1)';
			var labelInnerShadow2 = new createjs.Shape();
			labelInnerShadow.graphics.beginFill(innerLightGray)
			.drawRoundRect(x + 1, y + 5, width - 2, height - 4, 4);

			specialgreen = 'rgba(40, 48, 40, 1)';
			specialgreenHtml = '#09b500';
			var labelBg = new createjs.Shape();
			labelBg.graphics.beginLinearGradientFill(
					//['rgba(70, 70, 70, 1)', 'rgba(32, 32, 32, 1)'],
					['rgba(50, 50, 50, 1)', 'rgba(40, 40, 40, 1)'],
					//['rgba(50, 50, 50, 1)', specialgreen],
					[0, 0.5], x + 1, y + 1, x + 1, y + height - 18)
					.drawRoundRect(x + 1, y + 1, width - 2, height - 1, 4);

			this.container.background.addChild(labelInnerShadow);
			this.container.background.addChild(labelInnerShadow2);
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


		},

		addElementsToStages: function() {
			this.addContainerToStages();
			this.stages.foreground.addChild(this.label);
		},

		handleLabelUpdate: function() {
			this.label.text = this.model.get('valueFromMixer');
		},

		render: function() {
			this.stages.background.update();
			this.stages.foreground.update();
			return this;
		}
	});

	return LabelValueView;
});