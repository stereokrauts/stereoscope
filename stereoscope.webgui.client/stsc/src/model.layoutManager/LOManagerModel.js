/**
 * This is the Layout Manager Model
 * that handles file import.
 */
sk.stsc.model.LOManagerModel = Backbone.Model.extend({
	
	dispatcher: null,
	view: null,
	
	initialize: function(options) {
		try {
			if (options.dispatcher) {
				this.dispatcher = options.dispatcher;
			} else {
				throw 'Argument "dispatcher" missing.';
			}
		} catch(e) {
			var msg = 'Illegal LOManagerModel object initialization. ' + e;
			throw new Error(msg);
		}
		
		this.view = new sk.stsc.view.LOManagerView({ model: this });
	},
	
	
	
	
	
});