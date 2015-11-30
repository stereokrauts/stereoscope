/**
 * LayoutView: The view that sets up the basic layout dom.
 * It uses a jquery UI tablist and provides a 3-layer canvas stack
 * for performance optimization.
 */
sk.stsc.view.LayoutView = Backbone.View.extend({
	
	model: null,
	pageNavList: $('#mixerPageList'),
	canvasHost: $('<div/>').attr('id', 'canvasHost'),
	canvasStack: null,
	lastVisited: 'page0_page0',
	isPageNavRendered: false,
	
	initialize: function(options) {
		try {
			if (options.model) {
				this.model = options.model;
				this.el = options.el;
			} else {
				throw 'Argument "model" missing.';
			}
		} catch(e) {
			var msg = 'Illegal LayoutView object initialization: ' + e;
			throw new Error(msg);
		}
		
		this.canvasStack = new sk.stsc.view.CanvasStackView({
			el: this.canvasHost,
			dimensions: this.model.get('dimensions')
		});
		
		this.setCanvasStack();
	},
	
	setCanvasStack: function() {
		this.canvasHost.css('position', 'relative');
		this.canvasStack.render();
		
	},
	
	render: function() {
		this.$el.empty();
		this.$el.append(this.canvasHost);
		
		if (!this.isPageNavRendered) {
			this.pageNavList.empty();
			this.renderPageNavList();
			this.isPageNavRendered = true;
		}
		this.renderPage(this.lastVisited);
	},
	
	renderPage: function(pageId) {
		this.model.pageList.forEach(function(page) {
			if (page.get('pageId') === pageId) {
				page.view.render();
			}
		});
	},
	
	renderPageNavList: function() {
		this.model.pageList.forEach(function(page) {
			if (page.get('pageLevel') === 1) {
				if (!page.get('isControlHost')) {
					this.renderPageNavLabel(page.get('name'));
					this.renderPageChildItems(page);
				} else {
					this.renderPageNavListItem(page.get('pageId'), page.get('name'));
				}
			}
		}, this);
	},
	
	renderPageChildItems: function(page) {
		page.pageList.forEach(function(subPage) {
			this.renderPageNavListItem(subPage.get('pageId'), subPage.get('name'));
		}, this);
	},
	
	renderPageNavListItem: function(pageId, pageName) {
		var textColor = this.getSectionColor(pageName);
		var link = $('<a/>', { href: '#', text: pageName })
			.addClass('mixerPageItem')
			.css('color', textColor);
		var item = $('<li/>').attr('id', pageId).append(link);
		this.pageNavList.append(item);
	},
	
	renderPageNavLabel: function(pageName) {
		var textColor = this.getSectionColor(pageName);
		var label = $('<label/>').text(pageName).css('color', textColor);
		var item = $('<li/>').append(label);
		//item.css('color', textColor);
		this.pageNavList.append(item);
	},
	
	resync: function(pageId) {
		var rootId = pageId.split('_')[0];
		if (rootId === "page0") {
			this.model.resyncInputs();
		} else if (rootId === "page1") {
			console.log('resync aux!');
			this.model.resyncAux();
		} else {
			this.model.resyncAll();
		}
	},
	
	getSectionColor: function(pageName) {
		var color = '#fff';
		var sec = pageName.split(/[0-9]/);
		if (sec[0] === 'Inputs') {
			color = statics.color.BLUE;
		} else if (sec[0] === 'Auxiliaries') {
			color = statics.color.GREEN;
		}
		return color;
	}
	
});