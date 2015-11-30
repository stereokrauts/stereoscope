//steal/js stsc/scripts/compress.js

load("steal/rhino/rhino.js");
steal('steal/clean',function(){
	steal.clean('stsc/stsc.html',{indent_size: 1, indent_char: '\t'});
});
