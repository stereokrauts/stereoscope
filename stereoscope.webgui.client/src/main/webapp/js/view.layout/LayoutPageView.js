/**
 * This is the view that will be
 * rendered, when a LayoutPage model
 * gets initialized.
 */
define([
  'jquery',
  'underscore',
  'backbone'
], function($, _, Backbone) {

	var LayoutPageView = Backbone.View.extend({

		id: '#canvasHost',
		model: null,
		subPageButtons: [],

		background: null,
		foreground: null,
		interactive: null,
		stages: null,

		initialize: function(options) {
			try {
				if (options.model) {
					this.model = options.model;
				} else {
					throw 'Argument "model" missing.';
				}
			} catch(e) {
				var msg = 'Illegal LayoutPageView object initialization: ' + e;
				throw new Error(msg);
			}

			this.background = options.canvasStack.background;
			this.foreground = options.canvasStack.foreground;
			this.interactive = options.canvasStack.interactive;
			this.stages = this.getNewStageTuple();

			var self = this;
			this.model.on('change:pageList change:controlList', self.render, this);
		},

		render: function() {
			if (this.model.get('isControlHost') === true) {
				this.renderControls();
				this.reactiveStages();
				this.model.set('controlsRendered', true);
				console.log('page ' + this.model.get('pageId') + ' has ' + this.model.controlList.length + ' children.');
				console.log('page ' + this.model.get('name') + ' rendered.');
			}

		},

		unrender: function() {
			if (this.model.get('isControlHost') && this.model.get('controlsRendered')) {
				this.stages.background.removeAllChildren();
				this.stages.foreground.removeAllChildren();
				this.stages.interactive.removeAllChildren();
				this.stages.background.clear();
				this.stages.foreground.clear();
				this.stages.interactive.clear();

				this.stages.foreground.enableDOMEvents(false);
				this.stages.interactive.enableDOMEvents(false);
				this.disableTouchListeners(this.stages.interactive);

				this.model.controlList.forEach(function(control) {
					if (control.get('type') === 'statefulSwitch') {
						control.view.unrender();
					}
				});

				this.stages.background.update();
				this.stages.foreground.update();
				this.stages.interactive.update();

				this.model.set('controlsRendered', false);
				console.log('page ' + this.model.get('name') + ' unrendered.');
			}
		},

		renderControls: function() {
			this.model.controlList.forEach(function(control) {
				control.view.addElementsToStages();
				control.view.render();
			}, this);

		},

		getNewStageTuple: function() {
			var tuple = {
					background: new createjs.Stage(this.background.attr('id')),
					foreground: new createjs.Stage(this.foreground.attr('id')),
					interactive: new createjs.Stage(this.interactive.attr('id'))
			};
			this.enableTouchListeners(tuple.interactive);
			return tuple;
		},

		reactiveStages: function() {
			// reanable events and listeners on interactive stage
			this.stages.interactive.enableDOMEvents(false);
			this.disableTouchListeners(this.stages.interactive);
			this.stages.interactive.canvas = document.getElementById('interactiveCanvas');
			this.stages.interactive.enableDOMEvents(true);
			this.enableTouchListeners(this.stages.interactive);
			// foreground doesn't need touch listeners
			this.stages.foreground.enableDOMEvents(false);
			this.stages.foreground.canvas = document.getElementById('foregroundCanvas');
			this.stages.foreground.enableDOMEvents(true);
			// background doesn't need any listeners
			this.stages.background.canvas = document.getElementById('backgroundCanvas');

			this.stages.background.update();
			this.stages.foreground.update();
			this.stages.interactive.update();
		},

		enableTouchListeners: function(stage) {
			createjs.Touch.enable(stage);
		},

		disableTouchListeners: function(stage) {
			createjs.Touch.disable(stage);
		},


		//TODO: futile?
		changeBackground: function(bgColor) {
			if (this.model.get('isControlHost')) {
				$(this.model.get('el')).css('background-color', bgColor);
			}
		}

	});

	return LayoutPageView;
});