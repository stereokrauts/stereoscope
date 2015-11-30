/**
 * This is the base control view.
 * All other controls are subclassed
 * from this.
 * 
 * @author jansen
 *   
 */
sk.stsc.view.ControlView = Backbone.View.extend({
	
	model: null,
	stages: null,
	container: null,
	rotation: 0,
	
	initialize: function(options) {
		try {
			if (options.model) {
				this.model = options.model;
			} else {
				throw 'Argument "model" missing.';
			}
		} catch(e) {
			var msg = 'Illegal ControlView object initialization: ' + e;
			throw new Error(msg);
		};
		this.stages = this.model.stages;
	},
	
	setupContainer: function() {
		this.container = {
			background: new createjs.Container(),
			foreground: new createjs.Container(),
			interactive: new createjs.Container()
		};
	},
	
	setContainerCoords: function(x, y) {
		this.container['background'].x = x;
		this.container['background'].y = y;
		this.container['foreground'].x = x;
		this.container['foreground'].y = y;
		this.container['interactive'].x = x;
		this.container['interactive'].y = y;
	},
	
	addContainerToStages: function() {
		this.stages['background'].addChild(this.container['background']);
		this.stages['foreground'].addChild(this.container['foreground']);
		this.stages['interactive'].addChild(this.container['interactive']);
	},
	
	removeContainerFromStages: function() {
		this.stages['background'].removeChild(this.container['background']);
		this.stages['foreground'].removeChild(this.container['foreground']);
		this.stages['interactive'].removeChild(this.container['interactive']);
	},
	
	setRotation: function(orientation) {
		var rotation;
		if (orientation === 'vertical') {
			rotation = 270;
		} else if (orientation === 'horizontal_inv') {
			rotation = 180;
		} else if (orientation === 'vertical_inv') {
			rotation = 90;
		} else {
			rotation = 0;
		}
		return rotation;
	},
	
	rotateContainer: function(rotation) {
		this.container['background'].rotation = rotation;
		this.container['foreground'].rotation = rotation;
		this.container['interactive'].rotation = rotation;
	}
	
	
});