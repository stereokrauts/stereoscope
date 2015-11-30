/**
 * The model of the visual feedback control.
 * It just sets up default values and
 * creates the view.
 */
sk.stsc.model.VisualFeedbackModel = sk.stsc.model.ControlModel.extend({
	
	view: null,
	
	initialize: function(options) {
		this._super('initialize', options);
		
		this.setDefaults(options.params);
		this.setCustomFeatures(this.get('type'), this.stages);
		
	},
	
	setDefaults: function(params) {
		
	},
	
	setCustomFeatures: function(type, stages) {
		if (type === 'basicVisualFeedback') {
			this.view = new sk.stsc.view.VisualFeedbackBasicView({
				model: this,
				stages: stages
			});
		}
	}
	
});