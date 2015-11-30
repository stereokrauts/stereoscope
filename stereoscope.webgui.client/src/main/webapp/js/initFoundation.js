/**
 * Initialize foundation framework:
 * Set behaviour for off-canvas menus.
 * Should be recalled after content based on
 * foundation stuff are rendered to the main div.
 */
define([
    'jquery',
	'foundation'
], function($, foundation) {
	'use strict';
	
	var initFoundation = function() {
		//window.initFoundation = function() {
			$(document).foundation({
				offcanvas : {
					// Sets method in which offcanvas opens.
					// [ move | overlap_single | overlap ]
					open_method: 'move', 
					// Should the menu close when a menu link is clicked?
					// [ true | false ]
					close_on_click: true
				}
			});
		};
		
	//};
	
	return {initFoundation: initFoundation};
});