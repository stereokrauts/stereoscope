// load('stsc/scripts/crawl.js')

load('steal/rhino/rhino.js')

steal('steal/html/crawl', function(){
  steal.html.crawl("stsc/stsc.html","stsc/out")
});
