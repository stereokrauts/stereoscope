/**
 * This is the model for a single layout-representation
 * in the Layout Manager. It is basically just a string.
 */
define([
  'jquery',
  'underscore',
  'backbone',
  'commons',
  'view.layoutManager/LOManagerItemView'
], function($, _, Backbone, commons, LOManagerItemView) {

	var LOManagerItemModel = Backbone.Model.extend({

		defaults: {
			view: null,
			dispatcher: null,
			fileName: 'default',
			name: '',
			extension: '',
			orientation: 'horizontal',
			target: 'ipad',
			armed: false,
			active: false
		},

		dispatcher: null,
		view: null,

		initialize: function(attributes, options) {
			try {
				if (options.fname && options.dispatcher) {
					this.dispatcher = options.dispatcher;
					this.set('fileName', options.fname);
				} else {
					throw 'Argument "fname" and/or "dispatcher" missing.';
				}
			} catch(e) {
				var msg = 'Illegal LayoutItemModel object initialization: ' + e;
				throw new Error(msg);
			}
			this.view = new LOManagerItemView({ 
				model: this,
				id: attributes.id
			});

			var self = this;
			this.dispatcher.bind('layoutItemsDeactivate', function(e) { self.deactivate(e); });
			this.dispatcher.bind(commons.osc.LAYOUT_RESPONSE, function() { self.setActive(); });
			this.set('name', this.getFileName(this.get('fileName')));
			this.set('extension', this.getFileExtension(this.get('fileName')));
		},

		setArmed: function() {
			this.set('armed', true);
			this.dispatcher.trigger('layoutItemsDeactivate', this.get('fileName'));
			this.loadLayout();
		},

		setActive: function() {
			this.set('active', true);
			this.dispatcher.trigger('layoutItemsDeactivate', this.get('fileName'));
		},

		deactivate: function(fileName) {
			if (fileName !== this.get('fileName')) {
				this.set('active', false);
			}
		},

		loadLayout: function() {
			var mq = commons.getMediaQuery();
			var msg = '';
			if (this.get('extension') === 'stsc' || this.get('extension') === 'touchosc') {
				msg = commons.createRequestObject(commons.osc.LAYOUT_REQUEST, this.get('fileName'));
			} else if (this.get('extension') === 'std') {
				msg = commons.createRequestObject(commons.osc.LAYOUT_STD_REQUEST, mq);
			} else if (this.get('extension') === 'aux') {
				auxNumber = this.get('fileName').match(/\d+\.?\d*/);
				msg = commons.createRequestObject(commons.osc.LAYOUT_AUX_REQUEST, auxNumber + '#' + mq);
			}
			this.dispatcher.trigger('sendMessage', msg);
		},

		getFileName: function(fName) {
			return fName.substr(0,fName.lastIndexOf("."));
		},

		getFileExtension: function(fName) {
			parts = fName.split('.');
			return parts[parts.length - 2];
		}

	});

	return LOManagerItemModel;
});