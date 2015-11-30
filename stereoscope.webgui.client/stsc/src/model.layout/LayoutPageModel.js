/**
 * This is the model of a page in a layout.
 * It can hold controls or other pages
 * in a collection.
 * 
 * @author jansen
 */
sk.stsc.model.LayoutPageModel = Backbone.Model.extend({
	
	dispatcher: null,
	view: null,
	globalPageList: null,
	pageData: null,
	pageList: null,
	controlList: null,
	
	defaults: {
		name: "",
		pageLevel: null,
		pageId: null,
		isControlHost: false,
		controlsRendered: false,
	},
	
	initialize: function(options) {
		try {
			if (options.dispatcher && options.params) {
				this.dispatcher = options.dispatcher;
				this.globalPageList = options.params['globalPageList'];
				this.pageData = options.params['pageData'];
				this.set('pageLevel', options.params['pageLevel']);
				this.set('pageId', options.params['pageId']);
			} else {
				throw 'Argument "dispatcher" and/or "params" missing.';
			}
		} catch(e) {
			var msg = 'Illegal LayoutPageModel object initialization: ' + e;
			throw new Error(msg);
		}
		this.set('name', this.pageData.name);
		this.view = new sk.stsc.view.LayoutPageView({ model: this, canvasStack: options.params['canvasDom'] });
		this.globalPageList.add(this);
		this.pageList = new sk.stsc.collection.LayoutPagesCollection();
		this.controlList = new sk.stsc.collection.LayoutPageControlsCollection();
		this.setupPage();
	},
	
	setupPage: function() {
		console.log(this.pageData);
		//console.log(this.pageData.name);
		if (this.pageData.controls) {
			this.set('isControlHost', true);
			this.setControls(this.pageData.controls);
		} else if (this.pageData.pages) {
			this.set('isControlHost', false);
			this.setPageList(this.pageData.pages);
		} else {
			throw new Error('Unsupported layout structure. Please load a valid layout.');
			Alert('Unsupported layout structure. Please load a valid layout.');
		}
		
	},
	
	setPageList: function(pages) {
		var canvasStack = {
				background: this.view.background,
				foreground: this.view.foreground,
				interactive: this.view.interactive
		};
		_.forEach(pages, function(page, key) {
			var params = { pageData: page,
					globalPageList: this.globalPageList,
					pageLevel: this.get('pageLevel') + 1,
					pageId: this.get('pageId') + '_' + key,
					canvasDom: canvasStack};
			this.pageList.add(new sk.stsc.model.LayoutPageModel(
						{ dispatcher: this.dispatcher,
							params: params })
			);
		}, this);
	},
	
	setControls: function(controls) {
		_.forEach(controls, function(control) {
			var ctl = this.makeCtlModelEntity(control, this.view.stages);
			this.controlList.add(ctl);
		}, this);
	},
	
	makeCtlModelEntity: function(control, stageTuple) {
		var ctl;
		
		if (control['type'] === 'fader' 
			|| control['type'] === 'basicFader'
			|| control['type'] === 'volumeFader'
			|| control['type'] === 'stdRotary'
			|| control['type'] === 'basicRotary') {
			ctl = new sk.stsc.model.FaderModel({
				'dispatcher': this.dispatcher,
				'stages': stageTuple,
				'params': control
			});
		} else if (control['type'] === 'button' 
			|| control['type'] === 'basicButton'
			|| control['type'] === 'stdButton'
			|| control['type'] === 'stdToggleButton'
			|| control['type'] === 'basicToggleButton') {
			ctl = new sk.stsc.model.ButtonModel({
				'dispatcher': this.dispatcher,
				'stages': stageTuple,
				'params': control
			});
		} else if (control['type'] === 'label'
			|| control['type'] === 'basicLabel'
			|| control['type'] === 'valueLabel'
			|| control['type'] === 'staticLabel') {
			ctl = new sk.stsc.model.LabelModel({
					'dispatcher': this.dispatcher,
					'stages': stageTuple,
					'params': control 
			});
		} else if (control['type'] === 'visualFeedback'
			|| control['type'] === 'basicVisualFeedback') {
			ctl = new sk.stsc.model.VisualFeedbackModel({
				'dispatcher': this.dispatcher,
				'stages': stageTuple,
				'params': control
			});
		} else if (control['type'] === 'statefulSwitch') {
			ctl = new sk.stsc.model.StatefulSelectBarModel({
				'dispatcher': this.dispatcher,
				'stages': stageTuple,
				'params': control
			});
		} else if (control['type'] === 'frame') {
			ctl = new sk.stsc.model.FrameModel({
				'dispatcher': this.dispatcher,
				'stages': stageTuple,
				'params': control
			});
		} else {
			throw new Error('Unknown control of type '
					+ control['type'] + '. Aborting.');
		}
		return ctl;
	},
	
});