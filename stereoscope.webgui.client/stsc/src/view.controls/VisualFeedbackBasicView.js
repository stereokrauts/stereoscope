/**
 * This view flashes when a message
 * is received by the model, just
 * like an led.
 */
sk.stsc.view.VisualFeedbackBasicView = sk.stsc.view.ControlView.extend({
	
	background: null,
	flash: null,
	radius: 10,
	
	initialize: function(options) {
		this._super('initialize', options);
		
		_(this).bindAll('flash');
		this.model.on('change:oscValue', this.flash);
		this.setupVF();
	},
	
	setupVF: function() {
		this.setupContainer();
		this.addContainerToStages();
		this.rotation = this.setRotation(this.model.get('orient'));
		this.rotateContainer(this.rotation);
		this.setContainerCoords(
				this.model.get('coords').x,
				this.model.get('coords').y
		);
		
		this.radius = this.model.get('width') / 2; 
		this.background = new createjs.Shape();
		this.flash = new createjs.Shape();
		
		
	},
	
	drawBackground: function() {
		this.background.graphics.beginFill(
				'rgba(0, 0, 0, 1)')
				.drawCircle(this.radius, this.radius, this.radius);
	},
	
	drawFlash: function() {
		this.flash.graphics.beginFill(
				this.model.get('color'))
				.drawCircle(this.radius, this.radius, this.radius);
	},
	
	flash: function(ms) {
		this.container['interactive'].addChild(this.background);
		setTimeout(function() {
			this.container['interactive'].removeChild(this.background);
		}, ms, this);
	},
	
	addElementsToStages: function() {
		this.container['interactive'].addChild(this.background);
	},
	
	render: function() {
		this.drawBackground();
		this.stages['interactive'].update();
		return this;
	}
});