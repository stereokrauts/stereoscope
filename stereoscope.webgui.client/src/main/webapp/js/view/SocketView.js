/**
 * This view renders the little jacks
 * that indicate the status of the
 * server connection.
 */
define([
        'jquery',
        'underscore',
        'backbone',
        'initFoundation'
], function($, _, Backbone, Foundation) {
	
	var SocketView = Backbone.View.extend({

		//className: 'sockStat',
		el: $('#sockStat'),
		model: null,
		connected: null,
		disconnected: null,
		demo: null,
		displayModal: true,

		initialize: function(options) {
			try {
				if (options.model) {
					this.model = options.model;
				} else {
					throw 'Argument "model" missing.';
				}
			} catch(e) {
				var msg = 'Illegal SocketView object initialization: ' + e;
				throw new Error(msg);
			}

			this.connected = assets.img['connected-icon'];
			this.disconnected = assets.img['disconnected-icon'];
			this.demo = assets.img['demo-mode-icon'];
			$(this.connected).attr('data-tooltip', '').attr('aria-haspopup', 'true');
			$(this.connected).addClass('has-tip').attr('title', 'Connected to server');
			$(this.disconnected).attr('data-tooltip', '').attr('aria-haspopup', 'true');
			$(this.disconnected).addClass('has-tip').attr('title', 'No connection to server');
			$(this.demo).attr('data-tooltip', '').attr('aria-haspopup', 'true');
			$(this.demo).addClass('has-tip').attr('title', 'Demo mode');

			var self = this;
			this.model.dispatcher.bind('socketOpen', function(e) { self.render(e); });
		},

		render: function(sockStat) {
			if (sockStat) {
				if (this.model.get('demoMode')) {
					this.$el.empty().append(this.demo);
				} else {
					this.$el.empty().append(this.connected);
				}
			} else {
				this.$el.empty().append(this.disconnected);
				if (this.displayModal) {
					this.showModal();
				}
			}
			Foundation.initFoundation();
		},

		showModal: function() {
			var msg = '<h2>Server Connection Lost.</h2>';
			msg += '<p>Once the connection is up again, all data will be resynced. ';
			msg += 'Watch the little jacks to see the current connection status.</p>';
			msg += '<a class="close-reveal-modal">&#215;</a>';
			msg += '<a class="button custom-close-reveal-modal">Got it</a>';
			var modal = $('<div/>').attr('id', 'myModal');
			modal.addClass('reveal-modal').attr('data-reveal', '');
			modal.html(msg);
			this.$el.append(modal);
			modal.foundation('reveal', 'open');
			this.enableModalCustomClose();
			this.displayModal = false;
		},

		enableModalCustomClose: function() {
			$('a.custom-close-reveal-modal').click(function(){
				$('#myModal').foundation('reveal', 'close');
			});
		}

	});

	return SocketView;

});