/**
 * MediaQueryModelSpec 
 */
define(['jquery', 'underscore', 'backbone', 'initFoundation', 'model/MediaQueryModel', 'foundation'],
		function($, _, Backbone, foundation, MediaQueryModel) {
	
	describe('A Media Query Model', function() {
		
		beforeEach(function() {
			foundation.initFoundation();
			this.mq = new MediaQueryModel();
		});
		
		afterEach(function() {
			this.mq = null;
		});
	
		it('should be able to create its test objects', function() {
			expect(this.mq).toBeDefined();
		});
		
		it('should contain an array with default queries at startup', function() {
			expect(this.mq.mediaQueries).toEqual(
					jasmine.arrayContaining([
					                         'small',
					                         'medium',
					                         'large',
					                         'xlarge',
					                         'xxlarge'
					                         ]));
		});
	
		it('should contain a registry for custom queries', function() {
			expect(this.mq.registry).toEqual(
					jasmine.objectContaining({
						'iPhonePortrait': 'stereoscope-mq-ipp',
			        	'iPhoneLandscape': 'stereoscope-mq-ipl'
			        }));
		});
		
		it('should register custom queries to the queries array', function() {
			this.mq.registerMediaQueries(this.mq.registry);
			expect(this.mq.mediaQueries).toEqual(
					jasmine.arrayContaining(['iPhonePortrait', 'iPhoneLandscape']));
		});
		
		it('should register query classes to the DOM', function() {
			this.mq.registerMediaQueries(this.mq.registry);
			expect($('meta').attr('class', 'stereoscope-mq-ipp')).toBeInDOM();
		});
		
		it('should set "smallDevice" flag on small screens', function() {
			spyOn(this.mq, 'getCurrentMedia').and.returnValue('iPhoneLandscape');
			this.mq.registerMediaQueries(this.mq.registry);
			this.mq.setSmallDevice();
			expect(this.mq.smallDevice).toBeTruthy();
		});
		
		it('should leave "smallDevice" flag untouched on not-so-small screens', function() {
			spyOn(this.mq, 'getCurrentMedia').and.returnValue('medium');
			this.mq.registerMediaQueries(this.mq.registry);
			this.mq.setSmallDevice();
			expect(this.mq.smallDevice).toBeFalsy();
		})
	
	});

});