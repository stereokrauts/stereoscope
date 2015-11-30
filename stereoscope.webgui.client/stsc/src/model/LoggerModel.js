/**
 * 
 */
sk.stsc.model.LoggerModel = Backbone.Model.extend({
	
	defaults: {
		dispatcher: null,
		log: null,
		popUpAppender: null
	},
	
	initialize: function(options) {
		try {
			if (options.dispatcher) {
				this.dispatcher = options.dispatcher;
			} else {
				throw 'Argument "dispatcher" missing.';
			}
		} catch(e) {
			var msg = 'Illegal LoggerModel object initialization: ' + e;
			throw new Error(msg);
		}
		
		this.log = log4javascript.getDefaultLogger();
		//this.popUpAppender = new log4javascript.PopUpAppender();
		//this.addAppender(this.get('popUpAppender'));
		this.log.debug("Debugging message (appears in pop-up)");
	    this.log.error("Error message (appears in pop-up and in server log)");
	},
	
	addAppender: function(appender) {
		this.get('log').addAppender(appender);
	}
});