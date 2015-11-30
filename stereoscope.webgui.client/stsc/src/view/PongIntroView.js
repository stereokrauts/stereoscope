/**
 * This view renders the pong game and its context. 
 */
sk.stsc.view.PongIntroView = Backbone.View.extend({
	
	model: null,
	dim: null,
	header: $('<div/>').attr('id', 'pongHeadBar'),
	canvas: $('<canvas/>').attr('id', 'pongCanvas'),
	stage: null,
	runningText: null,
	titleText: null,
	
	//coords
	topAnimY: 50,
	mainAnimTop: 100,
	mainAnimBottom: 350,
	
	
	//Commodore 64 colors for tweening
	c64red: { r:136, g:0, b:0 },
	c64cyan: { r:170, g:255,b:238 },
	c64violet: { r:204, g:68, b:204 },
	c64green: { r:0, g:204, b:85 },
	c64blue: { r:0, g:0, b:170 },
	c64yellow: { r:238, g:238, b:119 },
	c64orange: { r:221, g:136, b:85 },
	c64brown: { r:102, g:68, b:0 },
	c64lightred: { r:255, g:119, b:119 },
	c64grey1: { r:51, g:51, b:51 },
	c64grey2: { r:119, g:119, b:119 },
	c64lightgreen: { r:170, g:255, b:102 },
	c64lightblue: { r:0, g:136, b:255 },
	c64grey3: { r:187, g:187, b:187 },
	
	initialize: function(options) {
		try {
			if (options.model) {
				this.model = options.model;
				this.el = options.el;
			} else {
				throw 'Argument "model" missing.';
			}
		} catch(e) {
			var msg = 'Illegal PongView object initialization: ' + e;
			throw new Error(msg);
		}
		_.bindAll(this, 'render');
		
		createjs.MotionGuidePlugin.install(createjs.Tween);
		createjs.ColorPlugin.install();
		this.dim = this.model.get('dimensions');
		this.setupPong();
	},
	
	setupPong: function() {
		this.setHeaderAttributes();
		this.setCanvasAttributes();
		this.stage = new createjs.Stage('pongCanvas');
		this.stage.autoClear = true;
		
		this.runningText = this.getCharArray(this.model.get('text4'), 'fairlight', '20px', '#000');
		this.titleText = this.getCharArray(this.model.get('text1'), 'superscreen', '120px', '#fff');
		this.renderChars(this.runningText, this.dim.x + 100, 500);
		this.renderChars(this.titleText, this.dim.x + 100, 100);
		this.setTicker(60);
		//this.tweenChars(this.runningText);
	},
	
	setHeaderAttributes: function() {
		this.header.css('width', '100%').css('height', '50px');
		
	},
	
	setCanvasAttributes: function() {
		this.canvas.attr('width', this.dim.x).attr('height', this.dim.y - 50);
		this.canvas.css('background-color', '#000000');
	},
	
	setTicker: function(fps) {
		createjs.Ticker.setFPS(fps);
		createjs.Ticker.addEventListener('tick', this.stage);
	},
	
	getCharArray: function(string, font, size, color) {
		var charArr = string.split('');
		var easelCharArr = new Array();
		_.forEach(charArr, function(char) {
			var chr = new createjs.Text(char, size + ' ' + font, color);
			easelCharArr.push(chr);
		}, this);
		return easelCharArr;
	},
	
	drawRunnerBg: function(palette) {
		var colors;
		var start = -10;
		var end = this.dim.x + 10;
		var greyPalette = [
		                   '#000000',
		                   '#ffffff',
		                   statics.convertRgbToHex(this.c64grey1),
		                   statics.convertRgbToHex(this.c64grey1),
		                   statics.convertRgbToHex(this.c64grey2),
		                   statics.convertRgbToHex(this.c64grey1),
		                   statics.convertRgbToHex(this.c64grey2),
		                   statics.convertRgbToHex(this.c64grey2),
		                   statics.convertRgbToHex(this.c64grey3),
		                   statics.convertRgbToHex(this.c64grey2),
		                   statics.convertRgbToHex(this.c64grey3),
		                   statics.convertRgbToHex(this.c64grey3),
		                   'rgba(255, 255, 255, 1)',
		                   statics.convertRgbToHex(this.c64grey3)
		                   ];
		var greenPalette = [
		                    '#000000',
		                    '#ffffff',
		                    statics.convertRgbToHex(this.c64brown),
		                    statics.convertRgbToHex(this.c64brown),
		                    statics.convertRgbToHex(this.c64green),
		                    statics.convertRgbToHex(this.c64brown),
		                    statics.convertRgbToHex(this.c64green),
		                    statics.convertRgbToHex(this.c64green),
		                    statics.convertRgbToHex(this.c64lightgreen),
		                    statics.convertRgbToHex(this.c64green),
		                    statics.convertRgbToHex(this.c64lightgreen),
		                    statics.convertRgbToHex(this.c64lightgreen),
		                    'rgba(255, 255, 255, 1)',
		                    statics.convertRgbToHex(this.c64lightgreen)
		                    ];
		
		if (palette === 'green') {
			colors = greenPalette;
		} else {
			colors = greyPalette;
		}
		var bg = new createjs.Container();
		for (var i = 0, j = 0; i < 28; i++) {
			var color;
			if (i < 14) {
				color = colors[i];
			} else {
				color = colors.pop();
			}
			for (var k = 0; k < 2; k++) {
				var line = new createjs.Shape();
				line.graphics
					.beginStroke(color)
					.moveTo(start, j)
					.lineTo(end, j)
					.endStroke();
				line.alpha = 1;
				bg.addChild(line);
				j += 1;
			}
			
		}
		bg.cache(bg.x, bg.y, this.dim.x + 20, 56);
		return bg;
		
	},
	
	renderTopRunningText: function() {
		var topRunnerBg = this.drawRunnerBg('green');
		var topRunner = new createjs.Text(this.model.get('text5'), '40px BitMapPinhole', '#000');
		topRunnerBg.y = this.topAnimY;
		topRunner.x = this.dim.x + 100;
		topRunner.y = this.topAnimY + 8;
		
		this.stage.addChild(topRunnerBg);
		this.stage.addChild(topRunner);
		
		createjs.Tween.get(topRunner, { loop: true })
			.to({ x: -1320 }, 24000, createjs.Ease.linear);
		
		var binTween = 120;
		createjs.Tween.get(topRunner, { loop: true })
		.wait(1400)
		.to({ r:this.c64blue.r, g:this.c64blue.g, b:this.c64blue.g}, 0).wait(binTween)
		.to({ r:this.c64lightblue.r, g:this.c64lightblue.g, b:this.c64lightblue.g}, 0).wait(binTween)
		.to({ r:this.c64cyan.r, g:this.c64cyan.g, b:this.c64cyan.b }, 0).wait(binTween)
		.to({ r:255, g:255, b:255 }, 0).wait(binTween + 50)
		.to({ r:this.c64cyan.r, g:this.c64cyan.g, b:this.c64cyan.b }, 0).wait(binTween)
		.to({ r:this.c64lightblue.r, g:this.c64lightblue.g, b:this.c64lightblue.g}, 0).wait(binTween)
		.to({ r:this.c64blue.r, g:this.c64blue.g, b:this.c64blue.g}, 0).wait(binTween)
		.to({ r:0, g:0, b:0}, 0).wait(binTween);
	},
	
	tweenChars: function(charArray) {
		var runningTextBg = this.drawRunnerBg('grey');
		runningTextBg.y = 486;
		this.stage.addChild(runningTextBg);
		
		var ms = 0;
		_.forEach(charArray, function(char) {
			ms += 130;
			
			createjs.Tween.get(char)
				.wait(ms)
				.to({x:-20}, 8000, createjs.Ease.linear);
			
			createjs.Tween.get(char)
				.wait(ms)
				.call(colorTween, {}, this);
			
			var binTween = 50;
			var min = 40;
			var max = 60;
			function colorTween() {
				
				createjs.Tween.get(char, {loop: true}, true)
					.wait(30)
					.to({ r:this.c64grey1.r, g:this.c64grey1.g, b:this.c64grey1.g}, 0).wait(binTween)
					.to({ r:this.c64grey2.r, g:this.c64grey2.g, b:this.c64grey2.g}, 0).wait(binTween)
					.to({ r:this.c64grey3.r, g:this.c64grey3.g, b:this.c64grey3.g}, 0).wait(binTween)
					.to({ r:255, g:255, b:255}, 0).wait(binTween)
					.to({ r:this.c64grey3.r, g:this.c64grey3.g, b:this.c64grey3.g}, 0).wait(binTween)
					.to({ r:this.c64grey2.r, g:this.c64grey2.g, b:this.c64grey2.g}, 0).wait(binTween)
					.to({ r:this.c64grey1.r, g:this.c64grey1.g, b:this.c64grey1.g}, 0).wait(binTween)
					.to({ r:1, g: 1, b: 1}, 0).wait(binTween*2)
					
					.to({ r:this.c64blue.r, g:this.c64blue.g, b:this.c64blue.b }, 0).wait(binTween)
					.to({ r:this.c64lightblue.r, g:this.c64lightblue.g, b:this.c64lightblue.b }, 0).wait(binTween)
					//.to({ r:this.c64lightgreen.r, g:this.c64lightgreen.g, b:this.c64lightgreen.b }, 0).wait(binTween)
					.to({ r:this.c64cyan.r, g:this.c64cyan.g, b:this.c64cyan.b }, 0).wait(binTween)
					.to({ r:255, g:255, b:255}, 0).wait(binTween)
					.to({ r:this.c64cyan.r, g:this.c64cyan.g, b:this.c64cyan.b }, 0).wait(binTween)
					//.to({ r:this.c64lightgreen.r, g:this.c64lightgreen.g, b:this.c64lightgreen.b }, 0).wait(binTween)
					.to({ r:this.c64lightblue.r, g:this.c64lightblue.g, b:this.c64lightblue.b }, 0).wait(binTween)
					.to({ r:this.c64blue.r, g:this.c64blue.g, b:this.c64blue.b }, 0).wait(binTween)
					
					.to({ r:this.c64grey1.r, g:this.c64grey1.g, b:this.c64grey1.g}, 0).wait(binTween)
					.to({ r:0, g: 0, b: 0}, 0).wait(binTween*2);
				
			}
			
		}, this);
		
	},
	
	renderBottomText: function() {
		var color = '#ffff';
		var stext1 = new createjs.Text(this.model.get('text2'), '20px fairlight', color);
		var stext2 = new createjs.Text(this.model.get('text3'), '20px fairlight', color);
		var startPosX = this.dim.x/2;
		var startPosY = 550;
		var offset = 30;
		stext1.x = startPosX;
		stext2.x = startPosX;
		stext1.y = startPosY;
		stext2.y = startPosY + 25;
		stext1.textAlign = 'center';
		stext2.textAlign = 'center';
		this.stage.addChild(stext1);
		this.stage.addChild(stext2);
		
		var binTween = 100;
		createjs.Tween.get(stext1, { loop: true })
			.to({ r:this.c64grey3.r, g:this.c64grey3.g, b:this.c64grey3.g}, 0).wait(binTween)
			.to({ r:this.c64grey2.r, g:this.c64grey2.g, b:this.c64grey2.g}, 0).wait(binTween)
			.to({ r:this.c64grey1.r, g:this.c64grey1.g, b:this.c64grey1.g}, 0).wait(binTween)
			.to({ r:0, g: 0, b: 0}, 0).wait(binTween*2)
			.to({ r:this.c64grey1.r, g:this.c64grey1.g, b:this.c64grey1.g}, 0).wait(binTween)
			.to({ r:this.c64grey2.r, g:this.c64grey2.g, b:this.c64grey2.g}, 0).wait(binTween)
			.to({ r:this.c64grey3.r, g:this.c64grey3.g, b:this.c64grey3.g}, 0).wait(binTween)
			.to({ r:255, g:255, b:255}, 0).wait(binTween);
			
		createjs.Tween.get(stext1)
			.wait(2000)
			.to({ x: startPosX - offset}, 2000, createjs.Ease.cubicInOut)
			.call(setNewStartPoint);
		
		createjs.Tween.get(stext2)
			.wait(2000)
			.to({ x: startPosX + offset}, 2000, createjs.Ease.cubicInOut);
		
		function setNewStartPoint() {
			stext1.x = startPosX - offset;
			createjs.Tween.get(stext1, {loop: true})
				.to({ x: startPosX + offset}, 1400, createjs.Ease.cubicInOut)
				.to({ x: startPosX - offset}, 1400, createjs.Ease.cubicInOut);
		}
	},
	
	tweenTitle: function() {
		var charDelay = 0;
		var speed = 3500;
		var leftTurnPoint = 100;
		var rightTurnPoint = this.dim.x - 250;
		var topTurnPoint = this.mainAnimTop;
		var bottomTurnPoint = this.mainAnimBottom;
		_.forEach(this.titleText, function(char, index) {
		
			charDelay += 250;
			
			if (index < 6) {
				char.color = '#00cc55';
			}
			
			createjs.Tween.get(char, { loop: true })
				.to({ y: bottomTurnPoint }, speed*1.3, createjs.Ease.linear)
				.to({ y: topTurnPoint }, speed*1.3, createjs.Ease.linear);
			
			createjs.Tween.get(char)
				.wait(charDelay)
				.to({ x: leftTurnPoint }, speed/2, createjs.Ease.cubicOut)
				.wait(1000)
				.call(tweenLoop, {}, this);
			
			function tweenLoop() {
				char.x = leftTurnPoint;
				createjs.Tween.get(char, { loop: true })
					.to({ x: rightTurnPoint }, speed, createjs.Ease.cubicOut)
					.to({ x: leftTurnPoint }, speed, createjs.Ease.cubicOut);
			}
		//}		
		}, this);
	},
	
	
	renderChars: function(charArr, x, y) {
		_.forEach(charArr, function(char) {
			char.x = x;
			char.y = y;
			this.stage.addChild(char);
		}, this);
	},
	
	render: function() {
		this.$el.empty();
		this.$el.append(this.header);
		this.$el.append(this.canvas);
		this.stage.canvas = document.getElementById('pongCanvas');
		//this.renderChars(this.runningText, this.dim.x - 10, 500);
		this.stage.update();
		this.tweenChars(this.runningText);
		this.renderBottomText();
		this.tweenTitle();
		this.renderTopRunningText();
		createjs.Sound.play(sk.stsc.resources.audio['stereopongIntro']);
		//this.stage.update();
		
	}
	
});