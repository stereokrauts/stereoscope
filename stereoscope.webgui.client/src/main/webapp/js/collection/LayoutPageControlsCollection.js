/** This collection stores all
 * controls/elements of a single
 * tab page of the mixer gui.
 * 
 * @author jansen
 */
define([
  'jquery',
  'underscore',
  'backbone'
], function($, _, Backbone){
	
	//avoid cyclic reference
	var ControlModel = require(['model.controls/ControlModel']);
	
	var LayoutPageControlsCollection = Backbone.Collection.extend ({

		model: ControlModel

	});

	return LayoutPageControlsCollection;
});