/**
 * A frame view with an optional label for grouping elements.
 */
define([
  'jquery',
  'underscore',
  'backbone',
  'view.controls/ControlView',
  'easel'
], function($, _, Backbone, ControlView) {

	var FrameView = ControlView.extend({

		label: null,
		bgColor: '#353535',

		initialize: function(options) {
			this._super(options);

			this.setupLabel();
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
			if (this.model.get('hasLabel')) {
				this.drawLabel();
			}
		},

		drawBackground: function() {
			var rgba = 'rgba(160, 160, 160, 0.1)';

			var background = new createjs.Shape();
			background.graphics.beginFill(this.bgColor)
			.drawRoundRect(1, 1, this.model.get('width') - 2, this.model.get('height') - 2, 5);
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


			var frameBg = new createjs.Shape();
			frameBg.graphics.beginFill(this.bgColor)
			.drawRoundRect(x + 1, y + 1, width - 2, height - 2, 3);


			this.container.background.addChild(shinyShadow);
			this.container.background.addChild(darkShadow);
			this.container.background.addChild(frameBg);
		},

		drawLabel: function() {

			var textWidth = this.label.getBounds().width;
			var textHeight = this.label.getBounds().height;

			this.label.x = this.model.get('coords').x + 30;
			//+ textWidth/2;
			this.label.y = this.model.get('coords').y
			- textHeight/2;

			var bgX = this.label.x - 8;
			var bgY = this.label.y - 6;
			var bgW = textWidth + 16;
			var bgH = textHeight + 16;
			var labelBack = new createjs.Shape();
			labelBack.graphics.beginFill('rgba(0, 0, 0, 1')
			.drawRoundRect(bgX - 1, bgY - 1, bgW + 1, bgH + 2, 3)
			.beginFill(this.bgColor)
			.drawRoundRect(bgX, bgY, bgW, bgH, 2)
			.beginFill(this.bgColor)
			.drawRect(bgX - 1, this.model.get('coords').y + 1, bgW + 2, bgH/2 + 3);

			this.stages.foreground.addChild(labelBack);
			this.stages.foreground.addChild(this.label);

		},

		addElementsToStages: function() {
			this.addContainerToStages();
		},

		render: function() {
			this.stages.background.update();
			this.stages.foreground.update();
			return this;
		},

		printProperties: function() {
			console.log('LabelStaticView related properties of this object:');
			console.log('x: ' + this.x);
			console.log('y: ' + this.y);
			console.log('width:' + this.width);
			console.log('heigth:' + this.height);
			console.log('color: ' + this.color);
		}

	});

	return FrameView;
});