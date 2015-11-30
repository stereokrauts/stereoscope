//steal/js stsc/scripts/compress.js

load("steal/rhino/rhino.js");
steal('steal/build','steal/build/scripts','steal/build/styles',function(){
	steal.build('stsc/scripts/build.html',{to: 'stsc'});
});
