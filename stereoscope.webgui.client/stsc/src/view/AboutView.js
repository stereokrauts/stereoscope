/**
 * The classic 'About' section.
 * It doesn't have a model because
 * there is no data to process.
 */
sk.stsc.view.AboutView = Backbone.View.extend({
	
	initialize: function(options) {
		if (options) {
			this.el = options.el;
		}
	},
	
	render: function() {
		this.$el.empty();
		this.$el.append(this.getHtml());
		initFoundation();
	},
	
	getHtml: function() {
		var html = '<div class="aboutSec">'; 
			
		html += '<div class="row">';
		html += '<div class="large-12 columns">';
		html += '<h1>Stereoscope 3 RC ${project.version}-${buildNumber}</h1>';
		html += '<p>Copyright 2010-2014 Stereokrauts GbR</p>';
		html += '</div></div>';
		
		html += '<div class="scrollBar">';
		html += '<div class="row" data-equalizer>';
		
		html += '<div class="large-6 medium-6 columns">' 
			+ '<div class="panel aboutBox" data-equalizer-watch>'
			+ '<p class="aboutBoxHeader">Contact Info</p>'
			+ '<ul class="aboutBoxList">'
			+ '<li class="aboutAddress addressFirst">Stereokrauts GbR</li>'
			+ '<li class="aboutAddress">Rappenberghalde 23</li>'
			+ '<li class="aboutAddress">72070 T&#252;bingen</li>'
			+ '<li class="aboutAddress">GERMANY</li>'
			+ '<li class="aboutAddress"><i class="fi-mail">&nbsp;</i>info@stereokrauts.com</li>'
			+ '<li class="aboutAddress"><i class="fi-telephone">&nbsp;</i>+49 7121 1459 053 - 0</li>'
			+ '</ul>'
			+ '</div></div>';
		
		html += '<div class="large-6 medium-6 columns">'
			+ '<div class="panel aboutBox" data-equalizer-watch>'
			+ '<p class="aboutBoxHeader">Find us on the web</p>'
			+ '<ul class="aboutBoxList">'
			+ '<li class="aboutItem"><a class="sk-link" target="_blank" href="http://stereokrauts.com"><i class="fi-home">&nbsp;</i>stereokrauts.com</a></li>'
			+ '<li class="aboutItem"><a class="sk-link" target="_blank" href="http://stereokrauts.tumblr.com"><i class="fi-social-tumblr">&nbsp;</i>stereokrauts.tumblr.com</a></li>'
			+ '<li class="aboutItem"><a class="sk-link" target="_blank" href="https://www.facebook.com/stereokrauts"><i class="fi-social-facebook">&nbsp;</i>facebook.com/stereokrauts</a></li>'
			+ '<li class="aboutItem"><a class="sk-link" target="_blank" href="https://twitter.com/stereokrauts"><i class="fi-social-twitter">&nbsp;</i>twitter.com/stereokrauts</a></li>'
			+ '<li class="aboutItem"><a class="sk-link" target="_blank" href="http://www.youtube.com/user/Stereokrauts"><i class="fi-social-youtube">&nbsp;</i>youtube.com/user/Stereokrauts</a></li>'
			+ '</ul>'
			+ '</div></div>';
		
		html += '</div>'; //end div.row
		
		
		html += '<div class="row" data-equalizer>';
		
		html += '<div class="large-6 medium-6 columns">'
			+ '<div class="panel aboutBox" data-equalizer-watch>'
			+ '<p class="aboutBoxHeader">Stereoscope contains software developed by</p>'
			+ '<ul class="aboutBoxList">'
			+ '<li class="aboutItem"><a target="_blank" href="http://sojamo.de">Andreas Schlegel</a></li>'
			+ '<li class="aboutItem"><a target="_blank" href="http://apache.org">Apache Software Foundation</a></li>'
			+ '<li class="aboutItem"><a target="_blank" href="http://www.codehaus.org/">Codehaus</a></li>'
			+ '<li class="aboutItem"><a target="_blank" href="http://www.eclipse.org">Eclipse Foundation</a></li>'
			+ '<li class="aboutItem"><a target="_blank" href="http://www.spring.io">Spring Community</a></li>' 
			+ '<li class="aboutItem"><a target="_blank" href="http://java.com">Oracle Corporation</a></li>'
			+ '</ul>'
			+ '</div></div>';
		
		html += '<div class="large-6 medium-6 columns">'
			+ '<div class="panel aboutBox" data-equalizer-watch>'
			+ '<p class="aboutBoxHeader">3rd party software used in the graphical user interface</p>'
			+ '<ul class="aboutBoxList">'
			+ '<li class="aboutItem"><a target="_blank" href="http://backbonejs.org/">Backbone.js</a></li>'
			+ '<li class="aboutItem"><a target="_blank" href="http://www.createjs.com/#!/CreateJS">CreateJS</a></li>'
			+ '<li class="aboutItem"><a target="_blank" href="https://github.com/ftlabs/fastclick">Fastclick.js</a></li>'
			+ '<li class="aboutItem"><a target="_blank" href="http://foundation.zurb.com/">Foundation</a></li>'
			+ '<li class="aboutItem"><a target="_blank" href="http://jquery.com/">jQuery</a></li>'
			//+ '<li><a href="http://log4javascript.org/">log4javascript</a></li>'
			+ '<li class="aboutItem"><a target="_blank" href="http://modernizr.com/">Modernizr</a></li>'
			+ '<li class="aboutItem"><a target="_blank" href="http://sockjs.org/">SockJS</a></li>'
			+ '<li class="aboutItem"><a target="_blank" href="http://underscorejs.org/">Underscore.js</a></li>' 
			+ '</ul>'
			+ '</div></div>';
		
		html += '</div>'; // end div.row
		html += '</div>'; // end div.scrollBar
		html += '</div>';

		return html;
	}
}); 
