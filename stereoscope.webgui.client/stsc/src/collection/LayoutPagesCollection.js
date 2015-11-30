/** This collection stores page
 * collections which in turn store
 * controls and elements of a mixer gui
 * 
 * @author jansen
 */
sk.stsc.collection.LayoutPagesCollection = Backbone.Collection.extend ({
	
	model: sk.stsc.model.LayoutPageModel,
	comparator: 'pageLevel'
	
});