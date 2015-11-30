/**
 * This view manages the canvas stack consisting
 * of three canvas objects that can be rendered on:
 * background: static graphics
 * foreground: dynamic graphics without event listeners
 * interactive: dynamic graphics containing event listeners
 * 
 * @author jansen
 */
sk.stsc.view.CanvasStackView = Backbone.View.extend({
	
	background: $('<canvas/>').attr('id', 'backgroundCanvas'),
	foreground: $('<canvas/>').attr('id', 'foregroundCanvas'),
	interactive: $('<canvas/>').attr('id', 'interactiveCanvas'),
	stack: null,
	
	initialize: function(options) {
		try {
			if (options.dimensions) {
				this.setCanvasStack();
			} else {
				throw 'Argument "dimensions" missing.';
			}
		} catch(e) {
			var msg = 'Illegal CanvasStackView object initialization: ' + e;
			throw new Error(msg);
		}
		this.setCanvasStack();
		this.setCanvasAttributes(options.dimensions);
		this.setCanvasZPosition();
	},
	
	setCanvasStack: function() {
		this.stack = { 'background': this.background,
		               'foreground': this.foreground,
		               'interactive': this.interactive };
	},
	
	setCanvasAttributes: function(dim) {
		canvasHeight = dim.y - $('<ul/>').attr('id', 'pageTabList').height();
		_.forEach(this.stack, function(canvas) {
			canvas.attr('width', dim.x);
			canvas.attr('height', canvasHeight);
			canvas.css('position', 'absolute');
			canvas.css('background-color', 'transparent');
		}, this);
	},
	
	setCanvasZPosition: function() {
		this.background.css('z-index', 10);
		this.foreground.css('z-index', 20);
		this.interactive.css('z-index', 30);
	},
	
	render: function() {
		_.forEach(this.stack, function(canvas) {
			this.$el.append(canvas);
		}, this);
	}
	
});