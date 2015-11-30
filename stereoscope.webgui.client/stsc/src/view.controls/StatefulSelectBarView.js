/**
 * This control let you choose one from different
 * stateful elements like aux, geq or others.
 * 
 * Unlike all other control views this is done in HTML
 * so stage objects are not in use here.
 */
sk.stsc.view.StatefulSelectBarView = Backbone.View.extend({
	
	model: null,
	//tagName: 'dl',
	events: {
		'click .stateful-select': 'handleStatefulSwitch'
	},
	
	
	initialize: function(options) {
		try {
			if (options.model) {
				this.model = options.model;
			} else {
				throw 'Argument "model" missing.';
			}
		} catch(e) {
			var msg = 'Illegal StatefulSelectBarView object initialization: ' + e;
			throw new Error(msg);
		};
		this.$el = $('<dl/>').addClass('stateful-select-bar');
		_.bindAll(this, 'handleStatefulSwitch');
		this.setupStatefulBar();
		
	},
	
	setupStatefulBar: function() {
		this.setSelectBarHtml();
	},
	
	handleStatefulSwitch: function(e) {
		console.log(e);
		var identifier = this.model.get('name').replace(/\s/g, '-');
		var active = $(e.target.parentElement);
		var index = e.target.id.slice(-1);
		var oscStr = this.model.units[index - 1];
		$('.' + identifier).each(function(index) {
			console.log(index);
			$(this.parentElement).removeClass('active');
		});
		active.addClass('active');
		this.model.fireEvent(oscStr);
	},
	
	render: function() {
		$('#canvasHost').append(this.$el);
		this.setYPosition();
	},
	
	unrender: function() {
		this.$el.remove();
	},
	
	setSelectBarHtml: function() {
		var unit = $('<dt/>').html(this.model.get('valueFromMixer')).css('margin-right', 15);
		//this.selectBar = $('<dl/>').addClass('stateful-select-bar').append(unit);
		this.$el.append(unit);
		this.addUnitItems(this.selectBar, this.model.units);
		
	},
	
	addUnitItems: function(selectBar, units) {
		var identifier = this.model.get('name').replace(/\s/g, '-');
		for (var i = 1; i <= units.length; i++) {
			var cnt = i;
			if (i < 10) {
				cnt = '0' + i;
			}
			var anchor = $('<a/>').attr('href', '#').html(cnt);
			anchor.addClass('stateful-select').addClass(identifier);
			anchor.attr('id', identifier + '-' + i);
			var def = $('<dd/>').append(anchor);
			if (i === 1) {
				def.addClass('active');
			}
			this.$el.append(def);
		}
	},
	
	setYPosition: function() {
		if (this.$el.height() >= 32) {
			var height = this.$el.height() / 3;
			this.$el.css('bottom', height);
		}
	},
	
	addElementsToStages: function() {
		// just here because of convention.
	}
	
});