/**
 * This model registers new media queries
 * and offers a function to read the current one.
 * Media queries are used on the server
 * to roll out UI layouts suited for each
 * individual device.
 * Rules are defined in the file _media-queries.scss.
 */
define([ 'jquery', 'underscore', 'backbone', 'commons', 'initFoundation', 'foundation' ],
		function($, _, Backbone, commons, fd) {

	var MediaQueryModel = Backbone.Model.extend({
		
		smallDevice: false,
		
		/**
		 * mediaQueries - An Array with default queries
		 * offered by Foundation. Custom queries from
		 * registry should be added via registerMediaQueries().
		 */
		mediaQueries: [
		               
                       'small',		//width: 0px - 640px
                       'medium',	//width: 641px - 1024px
                       'large',		//width: 1025px - 1440px
                       'xlarge',	//width: 1441px - 1920px
                       'xxlarge'	//width: 1921px
        ],
        
        registry: {
        	'iPhonePortrait': 'stereoscope-mq-ipp',		// w320, h480
        	'iPhoneLandscape': 'stereoscope-mq-ipl',	// w480, h320
        	'iPhone5Portrait': 'stereoscope-mq-ip5p',	// w320, h568
        	'iPhone5Landscape': 'stereoscope-mq-ip5l',	// w568, h320
        	'iPhone6Portrait': 'stereoscope-mq-ip6p',	// w375, h667
        	'iPhone6Landscape': 'stereoscope-mq-ip6l',	// w667, h375
        	'iPhone6plusPortrait': 'stereoscope-mq-ip6pp',	// w414, h736
        	'iPhone6plusLandscape': 'stereoscope-mq-ip6pl',	// w736, h414
        },
        
        initialize: function(options) {
        	if (options && options.register) {
        		this.registerMediaQueries(this.registry);
        		this.setSmallDevice();
        	}
        	
        	_.forEach(Foundation.media_queries, function(query, name) {
        		console.log('Media Query: ' + name + ' val: ' + query);
        	});
        	
        },
        
        registerMediaQueries: function(registry) {
        	_.forEach(registry, function(cssClass, queryName) {
        		if ($.inArray(queryName, this.mediaQueries) === -1) {
        			console.log('Adding mq: ' + queryName + ' with css class: ' + cssClass);
        			this.mediaQueries.push(queryName);
        			Foundation.utils.register_media(queryName, cssClass);
        		}
        	}, this);
        },
        
        getCurrentMedia: function() {
        	var current = "large";
        	_.forEach(this.mediaQueries, function(mq) {
        		if (matchMedia(Foundation.media_queries[mq]).matches) {
        			current = mq;
        		}
        	}, this);
        	return current;
        },
        
        setSmallDevice: function() {
        	var current = this.getCurrentMedia();
        	_.forEach(this.registry, function(mq, mqName) {
        		if (current === mqName) {
        			this.smallDevice = true;
        		}
        	}, this);
        }
        
        
	});
	
	return MediaQueryModel;
});