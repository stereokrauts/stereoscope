/**
 * This is the config script for the
 * js optimizer r.js.
 * Together with require.js it compiles
 * javascript and css files.
 * 
 * All paths are relative to the location
 * of this file.
 */
({
	/*
	optimize: "closure",
	closure: {
		CompilerOptions: {
			languageIn: Packages.com.google.javascript.jscomp.CompilerOptions.LanguageMode.ECMASCRIPT5
		}
	},
	*/
	
	/**
	 * uglify2 is a powerful code parser/generator
	 * see the following for useful options:
     * "output" - http://lisperator.net/uglifyjs/codegen
     * "compress" - http://lisperator.net/uglifyjs/compress
	 */
	/*
	optimize: "uglify2",
	uglify2: {
		compress: {
			dead_code: true
		}
	},
	*/
	optimize: "uglify",
	appDir: "../webapp",
	baseUrl: "js",
	mainConfigFile: "../webapp/js/main.js",
	//mainConfigFile: "main.js",
	keepBuildDir: false,
	inlineText: true,
	findNestedDependencies: true,
	
	//name: "lib/almond",
	//name: "stereoscope", 
	//wrap: true,
	wrapShim: true,		//wraps shims in a define() so they get loaded before the app starts
	//include: ['loader'],
	//include: ['loader'],
    //insertRequire: ['loader'],
	//out: "../webapp/stereoscope.js",
	//module: ['lib/almond'],
	modules: [ { 
		//name: 'loader',
		name: 'lib/almond',
		//include: ['lib/almond'],
		//insertRequire: ['lib/almond'],
		include: ['loader'],
		insertRequire: ['loader'],
		//wrapShim: true,
		out: 'stereoscope.js'
		} ],
	dir: "../../../bin",
	//out: 'stereoscope.js',
	//dir: "../../../target/classes/src/main/webapp",
	removeCombined: true,
	
	optimizeCss: "none"
})
