/** This collection stores page
 * collections which in turn store
 * controls and elements of a mixer gui
 * 
 * @author jansen
 */
define([
  'jquery',
  'underscore',
  'backbone'
], function($, _, Backbone){
	
	//avoid cyclic reference
	var LayoutPageModel = require(['model.layout/LayoutPageModel']);

	var LayoutPagesCollection = Backbone.Collection.extend ({

		model: LayoutPageModel,
		comparator: 'pageLevel'

	});

	return LayoutPagesCollection;
});