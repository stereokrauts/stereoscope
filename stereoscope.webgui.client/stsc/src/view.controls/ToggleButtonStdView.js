/**
 * This is the standard toggle button view.
 */
sk.stsc.view.ToggleButtonStdView = sk.stsc.view.ControlView.extend({
	
	label: '',
	colorBg: null,
	
	initialize: function(options) {
		this._super('initialize', options);
		
		_.bindAll(this, 'handleMouseClick', 'handleButtonUpdate');
		this.setupButton();
		this.model.on('change:valueFromMixer', this.handleButtonUpdate, this);
	},
	
	setupButton: function() {
		this.setupContainer();
		//this.addContainerToStages();
		this.rotation = this.setRotation(this.model.get('orient'));
		this.rotateContainer(this.rotation);
		this.setContainerCoords(
				this.model.get('coords').x,
				this.model.get('coords').y
		);
		this.drawBackground();
		//this.drawColorBg();
		if (this.model.get('hasLabel')) {
			this.drawLabel();
		}
		this.container['interactive'].addEventListener('click', this.handleMouseClick);
	},
	
	drawBackground: function() {
		var x = 0;
		var y = 0;
		
		var width = this.model.get('width');
		var height = this.model.get('height');
		
		var frame = new createjs.Shape;
		frame.graphics.beginFill('rgba(0, 0, 0, 1)')
			.drawRoundRect(x, y, width, height, 3);
		
		var lightGray = 'rgba(160, 160, 160, 1)';
		var shinyShadow = new createjs.Shape();
		shinyShadow.graphics.beginFill(lightGray)
			.drawRoundRect(x + 1, y + 1, width - 2, height - 2, 2);
		
		var bg = new createjs.Shape();
		bg.graphics.beginLinearGradientFill(
				['rgba(100, 100, 100, 1)', 'rgba(80, 80, 80, 1)'],
				[0, 1], x + 1, y + 2, x + 1, height - 3)
			.drawRoundRect(x + 1, y + 2, width - 2, height - 3, 2);
		
		var rgb = statics.convertHexToRgb(this.model.get('color'));
		var rgba = 'rgba(' + rgb.r + ',' + rgb.g + ',' + rgb.b + ',0.5)';
		this.colorBg = new createjs.Shape();
		this.colorBg.graphics.beginFill(rgba)
			.drawRoundRect(x + 1, y + 2, width - 2, height - 3, 2);
		
		this.container['background'].addChild(frame);
		this.container['background'].addChild(shinyShadow);
		this.container['interactive'].addChild(bg);
	},
	
	drawLabel: function() {
		var textSize = this.model.get('width') / 3;
		this.label = new createjs.Text(this.model.get('name'),
				'normal ' + textSize + 'px Arial', 'white');
		
		var textWidth = this.label.getBounds().width;
		var textHeight = this.label.getBounds().height;
		
		this.label.x = this.model.get('coords').x
			+ this.model.get('width')/2
			- textWidth/2;
		this.label.y = this.model.get('coords').y 
			+ this.model.get('height')/2
			- textHeight/2;
	
		//this.container['interactive'].addChild(this.label);
	},
	
	addElementsToStages: function() {
		this.addContainerToStages();
		this.stages['interactive'].addChild(this.label);
	},
	
	handleButtonUpdate: function() {
		this.model.set('valueFromView', this.model.get('oscValue'));
		this.renderOnOffStatus();
	},
	
	handleMouseClick: function() {
		this.model.set('valueFromView', !this.model.get('oscValue'));
		this.renderOnOffStatus();
	},
	
	renderOnOffStatus: function() {
		this.model.get('oscValue') ? 
				this.container['interactive'].addChild(this.colorBg) : this.container['interactive'].removeChild(this.colorBg);
		this.stages['interactive'].update();
	},
	
	render: function() {
		this.stages['background'].update();
		this.stages['foreground'].update();
		this.stages['interactive'].update();
		return this;
	}
});