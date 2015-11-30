sk.stsc.view.FaderView = Backbone.View.extend({

	model: null,
	stage: null,
	faderYOffset: null,
	//faderKnobMoved: null,

	initialize: function(options) {
		try {
			if (options.dispatcher && options.oscAddress && options.stage) {
				this.model = new sk.stsc.model.FaderModel(
						{ dispatcher: options.dispatcher, 
							oscAddress: options.oscAddress});
				//this.setFaderAssembly(options.canvasId);
				this.stage = options.stage;
			} else {
				throw 'Argument "dispatcher" and/or "oscAddress" and/or "canvasId" missing.';
			}
		} catch(e) {
			var msg = 'Illegal Fader object initialization: ' + e;
			throw new Error(msg);
		}
		this.setFaderAssembly();
		this.on('change:model.get("faderKnobYPosition")', this.updateFader);
		_(this).bindAll('handleMouseDown', 'handleMouseMove');
		
		
	},
	
	//setFaderAssembly: function(canvasId) {
	setFaderAssembly: function() {
		//console.log(canvasId);
		//this.stage = new createjs.Stage(canvasId);
		this.container = new createjs.Container();
		this.container.name = 'faderContainer';
		this.stage.addChild(this.container);
		this.faderBackground = new createjs.Shape();
		this.faderKnob = new createjs.Shape();
		this.dbLabel = new createjs.Text();
	},
	
	render: function() {
		steal.dev.log('Rendering a fader.');
		
		this.setContainerCoords();
		this.drawFaderBackground();
		this.drawFaderKnob();
		this.drawDbLabel(this.model.get('color'));
		
		this.stage.update();
		return this;
	},
	
	setContainerCoords: function() {
		this.container.x = this.model.get('coords').x;
		this.container.y = this.model.get('coords').y;
	},

	drawFaderBackground: function() {
		this.faderBackground.graphics.beginStroke(
				this.model.get('color')).drawRoundRect(
						0, 0,
						this.model.get('width'),
						this.model.get('height'), 10);
		this.faderBackground.mouseEnabled = false;
		this.container.addChild(this.faderBackground);
	},
	
	drawFaderKnob: function() {
		this.model.setFaderKnobRange();
		steal.dev.log('oscValue: ' + this.model.get('oscValue'));
		if (!this.model.get('oscValue')) {
			this.model.set({faderKnobYPosition: this.model.get('faderKnobRange') });
		}
		steal.dev.log('faderKnobYPosition in model: ' + this.model.get('faderKnobYPosition'));
		this.faderKnob.graphics.beginFill(
				this.model.get('color')).drawRoundRect(
						0, this.model.get('faderKnobYPosition'), 
						this.model.get('width'),
						this.model.get('faderKnobHeight'), 10);
		this.faderKnob.addEventListener('mousedown', this.handleMouseDown);
		this.faderKnob.addEventListener('mouseout', this.handleMouseUp);
		this.container.addChild(this.faderKnob);
	},
	
	drawDbLabel: function(color) {
		var fontHeight = Math.floor(this.model.get('width') / 3.5);
		this.dbLabel.text = '-Inf. dB';
		this.dbLabel.color = '#ff0000';
		this.dbLabel.font = fontHeight + 'px Arial'; // like css syntax
		this.dbLabel.textAlign = 'center';
		this.dbLabel.textBaseline = 'top';
		this.dbLabel.x = this.model.get('width') / 2;
		this.dbLabel.y = this.model.get('faderKnobYPosition') + Math.floor(this.model.get('faderKnobHeight') / 3);
		this.dbLabel.mouseEnabled = false;
		this.container.addChild(this.dbLabel);
	},
	
	updateDbLabelText: function(labelText) {
		this.dbLabel.text = labelText;
		this.stage.update();
	},
	
	updateFader: function() {
		this.faderKnob.y = this.model.get('faderKnobYPosition');
		this.stage.update();
	},
	
	handleMouseDown: function(event) {
		this.faderYOffset = event.target.y - event.stageY;
		event.addEventListener('mousemove', this.handleMouseMove);
	},
	
	handleMouseUp: function(event) {
		this.faderKnob.unregisterEventListener('mousedown');
	},
	
	handleMouseMove: function(event) {
		deltaY = event.stageY + this.faderYOffset;
		faderYRange = -this.model.get('height') + this.model.get('faderKnobHeight'); //is this right? probably too wide
		if (deltaY >= faderYRange && deltaY <= 0) {
			event.target.y = deltaY;
			this.dbLabel.y = deltaY + this.model.get('height') - 17;
			this.model.set({'valueFromFader': 
				- (event.target.y / this.model.get('faderKnobRange'))});
			this.stage.update();
		}
	}

});