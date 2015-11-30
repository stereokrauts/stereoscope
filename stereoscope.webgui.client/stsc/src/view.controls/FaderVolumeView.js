/**
 * This is the fancy fader for volume changes
 * like in inputs or aux.
 * It doesn't include extra stuff like labels etc. 
 */
sk.stsc.view.FaderVolumeView = sk.stsc.view.ControlView.extend({
	
	offsetDuringMove: null,
	
	faderKnob: null,
	faderKnobHeight: 34,
	faderKnobRange: 0,
	faderGlobalZero: null,
	
	colorBar: null,
	colorBarBottom: 0,
	colorBarHeight: 0,
	
	slot: null,
	slotX: null,
	slotY: null,
	slotWidth: null,
	slotHeight: null,
	slotEdge: null,
	
	dbLabel: null,
	dbLabelLastX: null,
	
	
	initialize: function(options) {
		this._super('initialize', options);
		
		var self = this;
		_.bindAll(this, 'handleMouseDown', 'handleMouseUp', 'handleMouseMove', 'handleTweenIn', 'handleTweenOut', 'updateFaderKnob');
		this.model.on('change:valueFromMixer', this.handleFaderUpdate, this);
		this.model.on('change:levelLabel', this.handleLevelLabelUpdate, this);
		
		this.setupFader(options.orient);
	},
	
	
	setupFader: function() {
		this.setupContainer();
		this.addContainerToStages();
		this.rotation = this.setRotation(this.model.get('orient'));
		this.rotateContainer(this.rotation);
		this.setContainerCoords(
				this.model.get('coords').x,
				this.model.get('coords').y
				);
		this.faderKnob = this.container['interactive'];
		this.slot = this.container['background'];
		this.colorBar = new createjs.Shape();
		this.dbLabel = new createjs.Text(this.model.get('levelLabel'), 'normal 10px Arial', 'white');
		this.setVectorValues();
		this.drawBackground();
		this.drawForeground();
		this.drawFaderKnob();
		this.drawDbLabel();
	},
	
	setVectorValues: function() {
		this.faderKnobHeight = this.model.get('width')*0.7;
		this.faderKnobRange = this.model.get('height') - this.faderKnobHeight;
		//if (this.model.get('mode') === 'centered') {
		this.faderGlobalZero = this.model.get('coords').y + this.faderKnobRange;
		this.slotWidth = this.model.get('width')/4;
		this.slotHeight = this.faderKnobRange;
		this.slotX = this.model.get('width')/2 - this.slotWidth/2;
		this.slotY = this.faderKnobHeight/2;
		this.slotEdge = this.slotWidth/4;
		if (this.model.get('mode') === 'centered') {
			this.colorBarBottom = this.faderKnobRange/2 + this.faderKnobHeight/2;
		} else {
			this.colorBarBottom = this.faderKnobRange + this.faderKnobHeight/2 - 4;
		}
	},
	
	drawBackground: function() {
		var lightGray = 'rgba(70, 70, 70, 1)';
		var greenBg = 'rgba(9, 180, 0, 1)';
		
		var shinyFrame = new createjs.Shape();
		shinyFrame.graphics.beginFill(lightGray)
			.drawRoundRect(this.slotX + 1, this.slotY + 1, this.slotWidth, this.slotHeight, this.slotEdge);
		
		var frame = new createjs.Shape();
		frame.graphics.beginFill('rgba(0, 0, 0, 1')
			.drawRoundRect(this.slotX, this.slotY, this.slotWidth, this.slotHeight, this.slotEdge);
		
		
		//var gradientDark = 'rgba(24, 24, 24, 1)';
		var gradientDark = 'rgba(12, 12, 12, 1)';
		//var gradientBright = 'rgba(39, 39, 39, 1)';
		var gradientBright = 'rgba(24, 24, 24, 1)';
		var innerGradient = new createjs.Shape();
		innerGradient.graphics.beginLinearGradientFill(
				[gradientDark, gradientBright],
				[0.4, 1], this.slotX + 2, this.slotY + 2, this.slotX + this.slotWidth, this.slotY + 2)
				.drawRoundRect(this.slotX + 2, this.slotY + 2, this.slotWidth - 4, this.slotHeight - 4, this.slotEdge);
		
		this.slot.addChild(shinyFrame);
		this.slot.addChild(frame);
		this.slot.addChild(innerGradient);
	},
	
	drawForeground: function() {
		this.colorBar.x = this.model.get('coords').x;
		this.colorBar.y = this.model.get('coords').y;
		
	},
	
	
	drawColorBar: function() {
		//var greenBg = 'rgba(9, 180, 0, 1)';
		this.colorBar.graphics.clear();
		if (this.colorBarHeight >= 0) {
			var y = this.colorBarBottom - this.colorBarHeight;
			console.log('y value for colorbar: ' + y);
			this.colorBar.graphics.beginFill(this.model.get('color'))
			.drawRoundRect(this.slotX + 3, y, this.slotWidth - 6, this.colorBarHeight, this.slotEdge);
		} else {
			this.colorBar.graphics.beginFill(this.model.get('color'))
			.drawRoundRect(this.slotX + 3, this.colorBarBottom, this.slotWidth - 6, -this.colorBarHeight, this.slotEdge);
		}
	},

	drawFaderKnob: function() {
		var x = 0;
		var y = 0;
		var width = this.model.get('width');
		var height = this.faderKnobHeight;
		
		var gradientBright = 'rgba(24, 24, 24, 1)';
		var greenBg = 'rgba(9, 180, 0, .1)';
		
		var gray1 = 'rgba(125, 125, 125, 1)';
		var gray2 = 'rgba(67, 67, 67, 1)';
		
		
		var outerFrame = new createjs.Shape();
		outerFrame.graphics.beginLinearGradientFill(
				[gradientBright, 'rgba(0,0,0,1)'],
				[0, 1], x + 1, y + 1, x - 2, y + height - 2)
				.drawRoundRect(x + 1, y + 1, width - 2, height - 2, 6);
		
		var innerFrame = new createjs.Shape();
		innerFrame.graphics.beginLinearGradientFill(
				[gray1, gray2],
				[0, 1], x + 2, y + 2, x, y + height - 4)
				.drawRoundRect(x + 2, y + 2, width - 4, height - 4, 5);
		
		var sfgrad1 = 'rgba(84, 84, 84, 1)';
		
		var surface = new createjs.Shape();
		surface.graphics.beginLinearGradientFill(
				[sfgrad1, gray1],
				[0, 0.7], x + 4, y + 4, x + 4, y + height - 8)
				.drawRoundRect(x + 4, y + 4, width - 8, height - 8, 5);
		
		var labelInnerShadow = new createjs.Shape();
		labelInnerShadow.graphics.beginFill('rgba(0, 0, 0, 1')
			.drawRoundRect(x + 7, y + 8, width - 14, height - 16, 4);
		
		var innerLightGray = 'rgba(160, 160, 160, 1)';
		var labelInnerShadow2 = new createjs.Shape();
		labelInnerShadow.graphics.beginFill(innerLightGray)
			.drawRoundRect(x + 7, y + 10, width - 14, height - 18, 4);
		
		specialgreen = 'rgba(40, 48, 40, 1)';
		specialgreenHtml = '#09b500';
		var labelBg = new createjs.Shape();
		labelBg.graphics.beginLinearGradientFill(
				//['rgba(70, 70, 70, 1)', 'rgba(32, 32, 32, 1)'],
				['rgba(50, 50, 50, 1)', 'rgba(40, 40, 40, 1)'],
				//['rgba(50, 50, 50, 1)', specialgreen],
				[0, 0.5], x + 7, y + 9, x + 7, y + height - 18)
			.drawRoundRect(x + 7, y + 9, width - 14, height - 18, 4);
		
		this.faderKnob.addChild(outerFrame);
		this.faderKnob.addChild(innerFrame);
		this.faderKnob.addChild(surface);
		this.faderKnob.addChild(labelInnerShadow);
		this.faderKnob.addChild(labelInnerShadow2);
		this.faderKnob.addChild(labelBg);
		
		this.faderKnob.cache(0, 0, width, height);
		
		if (!this.model.get('oscValue')) {
			if (this.model.get('mode') === 'centered') {
				this.faderKnob.y += this.faderKnobRange / 2;
			} else {
				this.faderKnob.y += this.faderKnobRange;
			}
		}
		
		// shouldn't this be somewhere else (not in the draw routine)?
		this.faderKnob.addEventListener('mousedown', this.handleMouseDown);
		this.faderKnob.addEventListener('pressup', this.handleMouseUp);
		
	},
	
	drawDbLabel: function() {
		var textWidth = this.dbLabel.getBounds().width;
		var textHeight = this.dbLabel.getBounds().height;
		
		this.dbLabel.x = this.model.get('coords').x
			+ this.model.get('width')/2
			- textWidth/2;
		this.dbLabel.y = this.model.get('coords').y 
			+ this.faderKnobRange
			+ this.faderKnobHeight/3;
		
		
	},
	
	addElementsToStages: function() {
		this.addContainerToStages();
		this.stages['foreground'].addChild(this.colorBar);
		//this.stages['interactive'].addChild(this.faderKnob);
		this.stages['interactive'].addChild(this.dbLabel);
		//this.stages['interactive'].update();
	},
	
	render: function() {
		this.stages['background'].update();
		this.stages['foreground'].update();
		this.stages['interactive'].update();
		return this;
	},
	
	handleFaderUpdate: function(ctx, evt) {
		var yNew = this.faderGlobalZero - evt * this.faderKnobRange;
		this.updateFaderKnob(yNew);
	},
	
	handleLevelLabelUpdate: function(evt) {
		if (evt.get('levelLabel') === '-Inf') {
			this.dbLabel.text = evt.get('levelLabel');
		} else {
			var text = evt.get('levelLabel').slice(0, -2);
			this.dbLabel.text = text;
		}
	},
	
	handleMouseDown: function(event) {
		console.log("mouse down");
		//this.handleTweenOut();
		event.addEventListener('mousemove', this.handleMouseMove);
	},
	
	
	handleMouseUp: function() {
		//this.faderKnob.removeEventListener('mousedown');
		//this.handleTweenIn();
	},
	
	handleMouseMove: function(event) {
		var deltaY = event.stageY - this.offsetDuringMove;
		this.offsetDuringMove = event.stageY;
		var yNew = this.faderKnob.y + deltaY;
		
		var yTop = this.model.get('coords').y;
		var yBottom = this.faderGlobalZero;
		if (yNew < yTop) {
			yNew = yTop;
		} else if (yNew > yBottom) {
			yNew = yBottom;
		}
		
		var newValue = (yBottom - yNew) / this.faderKnobRange;
		this.model.set('valueFromView', newValue);
		this.updateFaderKnob(yNew);
	},
	
	updateFaderKnob: function(yNew) {
		this.faderKnob.y = yNew;
		this.dbLabel.y = yNew + this.faderKnobHeight / 3;
		if (this.model.get('mode') === 'centered') {
			this.colorBarHeight = this.colorBarBottom - yNew + this.faderKnobHeight/2;
		} else {
			this.colorBarHeight = this.faderGlobalZero - yNew;
		}
		console.log('Color bar height: ' + this.colorBarHeight);
		this.drawColorBar();
		this.stages['foreground'].update();
		this.stages['interactive'].update();
		this.offsetDuringMove = yNew;
	},
	
	handleTweenOut: function() {
		createjs.Ticker.setFPS(60);
		createjs.Ticker.addEventListener('tick', this.stages['interactive']);
		this.dbLabelLastX = this.dbLabel.x;
		createjs.Tween.get(this.dbLabel).to({x: this.dbLabel.x + this.model.get('width') - 10}, 200, createjs.Ease.linear).call(this.stopTicker);
	},
	
	handleTweenIn: function() {
		createjs.Ticker.setFPS(25);
		createjs.Ticker.addEventListener('tick', this.stages['interactive']);
		this.dbLabel.x = this.dbLabelLastX;
		createjs.Tween.get(this.dbLabel, {override: true}).to({x: this.dbLabelLastX}, 300, createjs.Ease.linear).call(this.stopTicker);
		//this.stopTicker();
	},
	
	stopTicker: function() {
		setTimeout(function() {
			createjs.Ticker.setFPS(0);
			createjs.Ticker.removeAllEventListeners();
		}, 300, this);
		
	}
	
});