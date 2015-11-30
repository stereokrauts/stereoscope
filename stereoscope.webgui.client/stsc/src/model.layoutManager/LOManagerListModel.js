sk.stsc.model.LOManagerListModel = Backbone.Model.extend({
	
	view: null,
	dispatcher: null,
	layoutItems: null,
	
	defaults: {
		layout: null,
		listRoot: null,
		filter: false,
		filterType: ''
	},
	
	initialize: function(options) {
		try {
			if (options.dispatcher && options.htmlId) {
				this.dispatcher = options.dispatcher;
			} else {
				throw 'Argument "dispatcher" missing.';
			}
		} catch(e) {
			var msg = 'Illegal LayoutListModel object initialization: ' + e;
			throw new Error(msg);
		}
		this.layoutItems = new sk.stsc.collection.LayoutItemCollection();
		this.view = new sk.stsc.view.LOManagerListView({
			model: this,
			el: '#' + options.htmlId
		});
		
		var self = this;
		_.bindAll(this, 'resetLayoutItemCollection', 'setNewLayoutItemModelList', 'requestLayoutList');
		this.dispatcher.bind('socketOpen', function(e) { self.requestLayoutList(e); });
		this.dispatcher.bind(statics.osc.SYSTEM_MIXERPROP_RESPONSE, function(e) { self.requestLayoutList(); });
		this.dispatcher.bind(statics.osc.LAYOUT_LIST_RESPONSE, function(e) { self.handleListResponse(e); });
		this.dispatcher.bind(statics.osc.LAYOUT_RESPONSE, function(e) { self.handleLayoutResponse(e); });
		this.dispatcher.bind('layoutItemsDeactivate', function(e) { self.view.render(); });
		//this.requestLayoutList();
		
	},
	
	requestLayoutList: function() {
		var msg = statics.createRequestObject(statics.osc.LAYOUT_LIST_REQUEST, 'request');
		this.dispatcher.trigger('sendMessage', msg);
	},
	
	handleListResponse: function(msg) {
		var delimiter = this.getListDelimiter(msg.val);
		var list = this.removeLastDelimiter(msg.val);
		var layoutNames = list.split(delimiter);
		var modelArray = this.setNewLayoutItemModelList(layoutNames, this.get('dispatcher'));
		this.resetLayoutItemCollection(modelArray);
	},
	
	handleLayoutResponse: function(msg) {
		this.set('layout', msg.val);
	},
	
	setNewLayoutItemModelList: function(nameArray, dispatcher) {
		var models = new Array();
		
		// first add the std layout (which isn't a file) and set it active
		/*
		models.push(new sk.stsc.model.LOManagerItemModel(
				{ id: 1 },
				{ fname: 'standard-layout.stsc.std', 
					dispatcher: dispatcher })
		);
		models[0].set('active', true);
		*/
		// then the other 'real' layouts (from disk)
		for (var i = 0; i < nameArray.length; i++) {
			models[i] = new sk.stsc.model.LOManagerItemModel(
					{ id: i + 1 },
					{ fname: nameArray[i],
						dispatcher: dispatcher });
		}
		return models;
	},
	
	resetLayoutItemCollection: function(layoutModels) {
		if (layoutModels) {
			console.log(layoutModels.length);
			this.layoutItems.reset(layoutModels);
		} else {
			this.layoutItems.reset();
		}
			
		
	},
	
	getListDelimiter: function(string) {
		return string.charAt(string.length - 1);
	},
	
	removeLastDelimiter: function(string) {
		return string.substring(0, string.length - 1);
	},
	
});