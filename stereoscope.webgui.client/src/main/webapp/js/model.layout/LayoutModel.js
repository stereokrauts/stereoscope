/**
 * This is the main class that defines a layout.
 * 
 * @author jansen
 */
define([
  'jquery',
  'underscore',
  'backbone',
  'commons',
  'collection/LayoutPagesCollection',
  'view.layout/LayoutView',
  'model.layout/LayoutPageModel'
], function($, _, Backbone, commons, LayoutPagesCollection, LayoutView, LayoutPageModel) {

	var LayoutModel = Backbone.Model.extend({

		dispatcher: null,
		mediaQueries: null,
		pageList: null,
		demoMode: false,

		defaults: {
			setup: {},
			mixer: {},
			dimensions: commons.layout.IPAD_LANDSCAPE,
			currentlyActivePageName: null
		},

		view: null,

		initialize: function(options) {
			try {
				if (options.dispatcher && options.mq) {
					this.dispatcher = options.dispatcher;
					this.mediaQueries = options.mq;
				} else {
					throw 'Argument "dispatcher" missing.';
				}
			} catch(e) {
				var msg = 'Illegal LayoutModel object initialization: ' + e;
				throw new Error(msg);
			}

			this.pageList = new LayoutPagesCollection();
			this.view = new LayoutView({model: this, el: '#main'});

			var self = this;
			_.bindAll(this, 'setPageTree');

			this.dispatcher.bind('socketOpen', function(e) { self.setLayout(e); });
			this.dispatcher.bind(commons.osc.SYSTEM_MIXERPROP_RESPONSE, function(e) { self.handleMixerpropResponse(); });
			this.dispatcher.bind(commons.osc.LAYOUT_RESPONSE, function(e) { self.handleLayoutResponse(e); });
			this.dispatcher.bind('switchLayoutPage', function(e) { self.handlePageSwitch(e); });
			console.log('layoutModel initialized');

		},

		setLayout: function(sockStat) {
			if (sockStat && this.pageList.length === 0) {
				// we're online and there's no layout loaded yet
				this.requestMixerProperties();
			} else if (sockStat && this.pageList.length > 0) {
				// we're online again after a connection failure
				this.resyncAll();
			} else if (!sockStat && this.demoMode){
				// we're offline and in demo mode
				this.setFallback();
			} else {
				// ignore this event
			}
		},

		handleMixerpropResponse: function() {
			this.requestStdLayout();
			console.log('Received mixer property response.');
		},

		handleLayoutResponse: function(msg) {
			this.handleLayout(msg.val);
		},

		handleLayout: function(layoutObj) {
			this.set('setup', layoutObj.setup);
			this.set('mixer', layoutObj.mixer);
			this.unrenderPages(this.pageList);
			this.setDimensions(this.get('setup').mode);
			this.setPageTree({ pages: layoutObj.pages });
			this.view.resync('page0_page0');
			this.view.isPageNavRendered = false;
			
			if (this.mediaQueries.smallDevice &&
					$.cookie('dontShowDisplayNfo') != 'yes') {
				this.view.showSmallLayoutInfo();
			}
			this.dispatcher.trigger('swapContent', 'Mixer');
			//this.setAuxStateTo(1);
		},

		handlePageSwitch: function(pageId) {
			console.log('Switching to page: ' + pageId);
			this.unrenderPages(this.pageList);
			this.pageList.forEach(function(page) {
				if (page.get('pageId') === pageId) {
					this.view.lastVisited = pageId;
					this.set('currentlyActivePageName', page.get('name'));
					console.log('currentlyActivePageName: ' + this.get('currentlyActivePageName'));
					this.view.resync(pageId);
					page.view.render();
				}
			}, this);

		},

		setPageTree: function(pageTree) {
			//this.removeAllChildNodes();
			this.resetPageList();
			_.forEach(pageTree.pages, function(page, key) {
				var params = { pageData: page,
						globalPageList: this.pageList,
						pageLevel: 1,
						pageId: key,
						canvasDom: this.view.canvasStack.stack};
				new LayoutPageModel(
						{ dispatcher: this.get('dispatcher'),
							params: params }
				);
			}, this);
			if (this.pageList.at(0).get('isControlHost')) {
				this.view.lastVisited = 'page0';
			} else {
				this.view.lastVisited = 'page0_page0';
			}
		},

		renderPages: function() {
			//convenience function
			this.view.render();
		},

		unrenderPages: function(pageList) {
			pageList.forEach(function(page) {
				page.view.unrender();
			}, this);
		},

		setDarkTheme: function() {
			this.setBackground('#000000');
		},

		setBrightTheme: function() {
			this.setBackground('#FFFFFF');
		},

		setBackground: function(color) {
			this.pageList.forEach(function(page) {
				page.get('view').changeBackground(color);
			});
		},

		setFallback: function() {
			this.handleLayout(assets.json.defaultLayout);
		},

		resetPageList: function() {
			this.pageList.reset();
		},

		removeAllChildNodes: function() {
			this.pageList.forEach(function(page) {
				page.pageList.reset();
				page.controlList.reset();
			});
		},

		requestMixerProperties: function() {
			this.dispatcher.trigger('sendMessage', 
					commons.createRequestObject(commons.osc.SYSTEM_MIXERPROP_REQUEST, true));
			console.log('Mixer properties request sent.');
		},

		requestStdLayout: function() {
			var mediaQuery = this.mediaQueries.getCurrentMedia();
			console.log('Requesting standard layout...');

			var msg = commons.createRequestObject(commons.osc.LAYOUT_STD_REQUEST, mediaQuery);
			this.dispatcher.trigger('sendMessage', msg);
		},


		// this and setElementDimensions are probably futile.
		setDimensions: function(mode) {
			var dimArray = commons.layout[mode];
			if (this.checkDimensions(dimArray)) {
				this.set('dimensions', dimArray);
				//this.view.setElementDimensions(this.get('dimensions'));
			} else {
				var msg = 'target device definition not supported.';
				throw new Error(msg);
			}
		},

		checkDimensions: function(dimensions) {
			if ($.isNumeric(dimensions.x) && $.isNumeric(dimensions.y)) {
				return true;
			} else {
				return false;
			}
		},

		resyncInputs: function() {
			this.dispatcher.trigger('sendMessage',
					commons.createRequestObject(commons.osc.SYSTEM_RESYNC_CHN_LEVELS, true));
			this.dispatcher.trigger('sendMessage',
					commons.createRequestObject(commons.osc.SYSTEM_RESYNC_CHN_NAMES, true));
			this.dispatcher.trigger('sendMessage',
					commons.createRequestObject(commons.osc.SYSTEM_RESYNC_CHN_ON, true));
			//this.dispatcher.trigger('sendMessage',
			//	commons.createRequestObject(commons.osc.SYSTEM_RESYNC_OUTPUTS, true));
		},

		resyncAux: function() {
			//this.dispatcher.trigger('sendMessage',
			//	commons.createRequestObject(commons.osc.SYSTEM_RESYNC_SELECTED_AUX, true));
			this.dispatcher.trigger('sendMessage',
					commons.createRequestObject(commons.osc.SYSTEM_RESYNC_AUX_SEND_LEVELS, true));
			this.dispatcher.trigger('sendMessage',
					commons.createRequestObject(commons.osc.SYSTEM_RESYNC_AUX_SEND_ON, true));
			//this.dispatcher.trigger('sendMessage',
			//	commons.createRequestObject(commons.osc.SYSTEM_RESYNC_OUTPUTS, true));
		},

		resyncAll: function() {
			this.dispatcher.trigger('sendMessage',
					commons.createRequestObject(commons.osc.SYSTEM_RESYNC_ALL, true));
		},

		setAuxStateTo: function(auxCount) {
			this.dispatcher.trigger('sendMessage',
					commons.createRequestObject(
							commons.osc.SYSTEM_STATE_AUX_CHANGETO, auxCount));
		}

	});

	return LayoutModel;
});