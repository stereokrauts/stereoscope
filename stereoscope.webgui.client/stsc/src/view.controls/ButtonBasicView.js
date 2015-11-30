/**
 * 
 */
sk.stsc.view.ButtonBasicView = sk.stsc.view.ControlView.extend({
	
	// properties related to drawing
	buttonBackground: null,
	buttonEdge: null,
	hitArea: null,
	edgeCurving: 5,
	
	initialize: function(options) {
		this._super('initialize', options);
		
		_(this).bindAll('handleMouseDown', 'handleMouseUp', 'render', 'renderOnStatus', 'renderOffStatus');

		// should only render the background, not the button as whole
		this.model.on('change:oscValue', this.renderOnStatus);
		this.model.set('type', 'basicButton');
		
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
		this.container['interactive'].addEventListener('mousedown', this.handleMouseDown);
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
		this.model.get('oscValue') ? this.drawBackground() : this.container['interactive'].removeChild(this.buttonBackground);
		this.stages['interactive'].update();
		
		return this;
	},
	
	renderOnStatus: function() {
		if (this.model.get('oscValue') === true) {
			this.drawBackground();
			this.stages['interactive'].update();
			
		}
	},
	
	renderOffStatus: function() {
		this.container['interactive'].removeChild(this.buttonBackground);
		this.model.set({valueFromView: false, oscValue: false, valueFromMixer: false});
		this.stages['interactive'].update();
	},
	
	addElementsToContainer: function() {
		
		
		
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
		this.container['interactive'].addChild(this.buttonBackground);
	},
	
	drawEdges: function() {
		this.buttonEdge.graphics.beginStroke(
				this.model.get('color')).drawRoundRect(
						0, 0,
						this.model.get('width'),
						this.model.get('height'),
						this.edgeCurving);
		this.buttonEdge.mouseEnabled = false;
		this.container['interactive'].addChild(this.buttonEdge);
	},
	
	drawHitArea: function() {
		this.hitArea.graphics.beginFill(
				'#000').drawRoundRect(
						0, 0,
						this.model.get('width'),
						this.model.get('height'),
						this.edgeCurving);
		this.container['interactive'].addChild(this.hitArea);
	},
	
	handleMouseDown: function(event) {
		
		this.model.set('valueFromView', true);
		console.log("oscVal: " + this.model.get('oscValue') + " , fromView: " + this.model.get('valueFromView'));
		this.renderOnStatus();
		event.addEventListener('mouseup', this.handleMouseUp);
	},
	
	handleMouseUp: function(event) {
		this.renderOffStatus();
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