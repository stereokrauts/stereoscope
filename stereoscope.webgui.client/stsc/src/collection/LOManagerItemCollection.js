/**
 * This collection stores abstract items
 * that represent a layout. These items
 * are just kind of placeholders that are
 * able to load the corresponding layout from
 * the server.
 * 
 * @author jansen
 */
sk.stsc.collection.LayoutItemCollection = Backbone.Collection.extend ({
	
	model: sk.stsc.model.LayoutItemModel,
	
	getCheckedLayout: function() {
		return this.findWhere({checked: true});
	}
	
});