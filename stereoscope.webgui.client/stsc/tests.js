/**
 * This is the configuration file for
 * Jasmine in-browser unit testing.
 * Files in a single block are loaded
 * simultaniously so dependencies shouldn't
 * be solved in one block.
 * 
 * @author jansen
 */

steal(
		'./css/stsc.css',
		//'../lib/jquery-1.11.1.js',
		'../jasmine-standalone/lib/jasmine-2.0.0-rc2/jasmine.css',
		'../jasmine-standalone/lib/jasmine-2.0.0-rc2/jasmine.js',
		'../jasmine-standalone/lib/jasmine-2.0.0-rc2/jasmine-html.js',
		'../lib/sockjs-0.3.4.js',
		'../lib/easeljs-0.7.1.min.js',
		'../lib/preloadjs-0.4.1.min.js',
		'../lib/jquery-2.0.3.js',
		'../lib/underscore'
).then(
		'../jasmine-standalone/lib/jasmine-2.0.0-rc2/boot.js',
		'../lib/backbone',
		'../lib/foundation/foundation'
).then(
		'../lib/foundation/foundation.abide.js',
		'../lib/foundation/foundation.accordion.js',
		'../lib/foundation/foundation.alert.js',
		'../lib/foundation/foundation.clearing.js',
		'../lib/foundation/foundation.dropdown.js',
		'../lib/foundation/foundation.equalizer.js',
		'../lib/foundation/foundation.interchange.js',
		'../lib/foundation/foundation.joyride.js',
		'../lib/foundation/foundation.magellan.js',
		'../lib/foundation/foundation.offcanvas.js',
		'../lib/foundation/foundation.orbit.js',
		'../lib/foundation/foundation.reveal.js',
		'../lib/foundation/foundation.slider.js',
		'../lib/foundation/foundation.tab.js',
		'../lib/foundation/foundation.tooltip.js',
		'../lib/foundation/foundation.topbar.js'
).then(
		'../jasmine-standalone/lib/plugins/jasmine-jquery',
		'../jasmine-standalone/lib/plugins/jasmine-stereoscope',
		'./src/_super',
		'./src/namespace',
		'./src/statics'
).then(
		// collections go here
		'./src/collection/LOManagerItemCollection',
		'./src/collection/LayoutPagesCollection',
		'./src/collection/LayoutPageControlsCollection'
).then(
		// source files go here
		'./src/model.controls/ControlModel',
		'./src/view.controls/ControlView',
		'./src/view/AppMenuListView',
		'./src/model/SocketModel',
		'./src/model.layout/LayoutPageModel',
		'./src/model/NavBarModel',
		'./src/model/LoggerModel',
		'./src/model/PongModel'
).then(
		'./src/model.layout/LayoutModel',
		'./src/model.layoutManager/LOManagerItemModel',
		'./src/model.layoutManager/LOManagerListModel',
		'./src/model/AppMenuListModel',
		'./src/model/AppModel',
		'./src/model.controls/FaderModel',
		'./src/model.controls/ButtonModel',
		'./src/model.controls/LabelModel',
		'./src/model.controls/VisualFeedbackModel',
		'./src/view.layout/LayoutPageView',
		'./src/view.layout/CanvasStackView',
		'./src/view.layout/LayoutView'
).then(
		'./src/view.controls/FaderView',
		'./src/view.controls/FaderBasicView',
		'./src/view.controls/FaderVolumeView',
		'./src/view.controls/ToggleButtonBasicView',
		'./src/view.controls/ToggleButtonStdView',
		'./src/view.controls/ButtonBasicView',
		'./src/view.controls/SubPageButtonView',
		'./src/view.controls/LabelBasicView',
		'./src/view.controls/LabelStaticView',
		'./src/view.controls/LabelValueView',
		'./src/view.controls/RotaryBasicView',
		'./src/view.controls/RotaryStdView',
		'./src/view.controls/VisualFeedbackBasicView',
		'./src/view/SocketView',
		'./src/view/PongIntroView',
		'./src/view/AboutView',
		'./src/view.layoutManager/LOManagerItemView',
		'./src/view.layoutManager/LOManagerListView',
		'./src/view.layoutManager/LOManagerView',
		'./src/view/NavBarView',
		'./src/view/AppMenuItemView',
		'./src/view/AppMenuMixerButtonView',
		'./src/view/AppView'
				
).then( 
		// spec files go here
		//,
		
		'./spec/SocketModelSpec',
		'./spec/LayoutModelSpec',
		'./spec/LayoutPageModelSpec'
		/*
		'./spec/FaderBasicViewSpec',
		'./spec/FaderViewSpec',
		'./spec/ControlModelSpec',
		'./spec/FaderModelSpec',
		
		'./spec/LayoutItemModelSpec',
		'./spec/LayoutItemViewSpec',
		'./spec/LayoutListModelSpec',
		'./spec/LayoutListViewSpec',
		//'./spec/LayoutPageModelSpec',
		//'./spec/LayoutRootPageModelSpec'
		'./spec/AppModelSpec',
		'./spec/AppViewSpec',
		'./spec/AppMenuButtonModelSpec',
		'./spec/AppMenuButtonViewSpec',
		'./spec/AppMenuListModelSpec',
		'./spec/AppMenuListViewSpec',
		'./spec/AppMenuItemViewSpec'
		*/
).then(
		// trivial tests last
		'./spec/staticsSpec',
		'./spec/namespaceSpec'
		//'./spec/backboneSuperSpec'
);