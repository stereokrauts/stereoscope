/**
 * This collection stores abstract items
 * that represent a layout. These items
 * are just kind of placeholders that are
 * able to load the corresponding layout from
 * the server.
 * 
 * @author jansen
 */
define([
  'jquery',
  'underscore',
  'backbone'
], function($, _, Backbone){

	//avoid cyclic reference
	var LOManagerItemModel = require(['model.layoutManager/LOManagerItemModel']);
	
	var LOManagerItemCollection = Backbone.Collection.extend ({

		model: LOManagerItemModel,

		getCheckedLayout: function() {
			return this.findWhere({checked: true});
		}

	});

	return LOManagerItemCollection;
});