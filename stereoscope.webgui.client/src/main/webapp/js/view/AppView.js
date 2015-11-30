/**
 * This is the starting point for the
 * stereoscope webapp. It should be
 * called from the html context (or
 * another js wrapper).
 * 
 * @author: jansen
 */
define([
        'jquery',
        'underscore',
        'backbone'
], function($, _, Backbone) {

	var AppView = Backbone.View.extend({

		el: $('#innerApp'),
		model: null,
		mainArea: $('#main'),
		isMixerActive: false,

		events: {
			'click .mixerPageItem': 'handleMixerPageChange',
		},

		initialize: function(options) {
			try {
				if (options.model) {
					this.model = options.model;
				} else {
					throw 'Argument "model" missing.';
				}
			} catch(e) {
				var msg = 'Illegal AppView object initialization: ' + e;
				throw new Error(msg);
			}
			_.bindAll(this, 'renderMainArea');
		},

		handleMixerPageChange: function(e) {
			var pageId = e.target.parentElement.id;
			console.log(pageId);
			this.model.dispatcher.trigger('switchLayoutPage', pageId);
			if (!this.isMixerActive) {
				this.renderMainArea('Mixer');
			} else {
				this.changeTitle('Mixer');
			}
		},

		render: function() {
			this.model.navBar.view.render('#innerWrapper', 'StaticTitle');
		},

		renderMainArea: function(item) {
			if (item === 'Mixer') {
				this.renderMixer();
			} else if (item === 'Layout_Manager') {
				this.renderLayoutManager();
			} else if (item === 'Pong') {
				this.renderPong();
			} else if (item === 'About') {
				//this.mainArea.html('<h1>About Stereoscope</h1>');
				this.renderAbout();
			} else if (item === 'DarkTheme') {
				this.renderDarkTheme();
			} else if (item === 'BrightTheme') {
				this.renderBrightTheme();
			} else if (item === 'Logger') {
				this.renderLogger();
			} else {
				this.renderMixer();
			}
			this.changeTitle(item);
			return this;
		},

		renderLayoutManager: function() {
			this.model.layoutManager.render();
			this.model.layoutManager.layoutList.layoutItems.each(function(item) {
				item.view.delegateEvents(); // restore events
			});
			this.isMixerActive = false;
		},

		renderAbout: function() {
			this.model.about.render();
			this.isMixerActive = false;
		},

		renderMixer: function() {
			this.model.layout.renderPages();
			this.isMixerActive = true;
		},

		renderPong: function() {
			this.model.pong.view.render();
			this.isMixerActive = false;
		},

		renderDarkTheme: function() {
			this.model.layout.setDarkTheme();
		},

		renderBrightTheme: function() {
			this.model.layout.setBrightTheme();
		},

		renderLogger: function() {
			this.isMixerActive = false;
		},

		changeTitle: function(item) {
			if (item === 'Mixer' && this.model.layout.get('currentlyActivePageName')) {
				item = item + ': ' + this.model.layout.get('currentlyActivePageName');
			}
			this.model.navBar.view.changeTitle(item);
		}

	});

	return AppView;

});
