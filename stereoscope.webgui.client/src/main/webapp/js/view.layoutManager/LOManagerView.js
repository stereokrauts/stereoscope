/**
 * The LayoutManager View
 * It's just a container that holds the LayoutList.
 * It doesn't need model (yet) since there is no data to handle.
 * 
 * TODO: file handling should be moved into a model.
 */
define([
        'jquery',
        'underscore',
        'backbone',
        'commons',
        'model.layoutManager/LOManagerListModel'
], function($, _, Backbone, commons, LOManagerListModel) {

	var LOManagerView = Backbone.View.extend({

		dispatcher: null,
		layoutList: null,
		//heading:  $('<h1/>').text('Layout Manager'),
		listContainer: $('<div/>').attr('id', 'layoutListContainer'),
		listRoot: $('<ul/>').attr('id', 'layoutItems'),

		events: {
			'click .layoutFilter_all': 'resetFilter',
			'click .layoutFilter_native': 'setNativeFilter',
			'click .layoutFilter_touchosc': 'setTouchOscFilter',
			'click #fileSelectRelay': 'handleFileSelect'
		},

		initialize: function(options) {
			try {
				if (options.dispatcher) {
					this.dispatcher = options.dispatcher;
				} else {
					throw 'Argument "dispatcher" missing.';
				}
			} catch(e) {
				var msg = 'Illegal LayoutManagerView object initialization. ' + e;
				throw new Error(msg);
			}
			_.bindAll(this, 'resetFilter', 'setNativeFilter', 'setTouchOscFilter', 'handleFiles', 'sendToServer');

			this.listContainer.append(this.listRoot);
			this.render();
			this.setLayoutList();
		},

		render: function() {
			this.$el.empty();
			this.$el.append(this.listContainer);
		},

		setLayoutList: function() {
			this.layoutList = new LOManagerListModel({
				dispatcher: this.dispatcher,
				htmlId: this.listRoot.attr('id')
			});
		},

		setListContainerHtml: function() {

			var html = '<div id="layoutListHeader">' +
				//'<span class="layoutHeading">[LM out of order in this release]</span>' +
				'<ul id="filterList" class="button-group">' +
				'<li><a href="#" class="no-underscore button disabled">Filter:</a></li>' +
				'<li><a href="#" class="layoutFilter_all button">All</a></li>' +
				'<li><a href="#" class="layoutFilter_native button">Native</a></li>' +
				'<li><a href="#" class="layoutFilter_touchosc button">TouchOSC</a></li>' +
				'</ul>' +
				'<input type="file" id="fileSelect" accept=".touchosc" multiple style="display:none"></input>' +
				'<a href="#" id="fileSelectRelay" class="button success import-button">Import TouchOSC Layout</a>' +
				'</div>';

			//var html = $('<h3/>').text('Layouts');
			this.listContainer.html(html);
			this.listContainer.append(this.listRoot);

		},

		setFooter: function() {
			var footer = $('<div/>').attr('id', 'layoutListFooter');
			/*var importButton = $('<button/>').addClass('layoutImportButton');
		importButton.text('Import Layout');
		footer.append(importButton);*/
			this.listContainer.append(footer);
		},

		resetFilter: function() {
			this.layoutList.set('filter', false);
			this.layoutList.set('filterType', '');
			this.layoutList.view.render();
		},

		setNativeFilter: function() {
			this.layoutList.set('filter', true);
			this.layoutList.set('filterType', 'json');
			this.layoutList.view.render();
		},

		setTouchOscFilter: function() {
			this.layoutList.set('filter', true);
			this.layoutList.set('filterType', 'touchosc');
			this.layoutList.view.render();
		},

		handleFileSelect: function() {
			fileSelector = document.getElementById('fileSelect');
			fileSelector.addEventListener("change", this.handleFiles, false);
			if (fileSelector) {
				fileSelector.click();
			}
		},

		handleFiles: function(evt) {
			var files = evt.target.files;
			var alertMsg = '<h3>';
			if (files.length > 1) {
				alertMsg += 'Files successfully uploaded:</h3><ul>';
			} else {
				alertMsg += 'File successfully uploaded:</h3><ul>';
			}

			for (var i = 0; i < files.length; i++) {
				var file = files[i];
				this.readFile(file);
				alertMsg += '<li>' + file.name + '</li>';
			}
			alertMsg += '</ul>';
			this.showModal(alertMsg);
		},

		readFile: function(file) {
			var fileReader = new FileReader();
			fileReader.onload = this.sendToServer;
			fileReader.fname = file.name;
			fileReader.fsize = file.size;
			fileReader.readAsBinaryString(file);
		},

		sendToServer: function(evt) {
			var ascii = btoa(evt.target.result);
			var msg = {
					type: 'bin',
					oscStr: commons.osc.LAYOUT_TOSC_UPLOAD,
					file: {
						fname: evt.target.fname,
						fsize: evt.target.fsize,
						data: ascii
					}
			};
			this.dispatcher.trigger('sendMessage', msg);
		},

		showModal: function(msg) {
			var modal = $('<div/>');
			modal.addClass('reveal-modal').attr('data-reveal', '');
			modal.html(msg);
			this.$el.append(modal);
			modal.foundation('reveal', 'open');
			setTimeout(function() {
				modal.foundation('reveal', 'close');
				//modal.remove();
			}, 3000, this);
		}

	});

	return LOManagerView;
});
