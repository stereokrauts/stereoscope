/**
 * 
 */
sk.stsc.view.SocketView = Backbone.View.extend({
	
	//className: 'sockStat',
	el: $('#sockStat'),
	connected: null,
	disconnected: null,
	displayModal: true,
	
	initialize: function(options) {
		try {
			if (options.dispatcher) {
				this.dispatcher = options.dispatcher;
			}
		} catch(e) {
			var msg = 'Illegal SocketView object initialization: ' + e;
			throw new Error(msg);
		}
		
		this.connected = sk.stsc.resources.img['connected-icon'];
		this.disconnected = sk.stsc.resources.img['disconnected-icon'];
		$(this.connected).attr('data-tooltip', '').attr('aria-haspopup', 'true');
		$(this.connected).addClass('has-tip').attr('title', 'Connected to server');
		$(this.disconnected).attr('data-tooltip', '').attr('aria-haspopup', 'true');
		$(this.disconnected).addClass('has-tip').attr('title', 'No connection to server');
		
		var self = this;
		this.dispatcher.bind('socketOpen', function(e) { self.render(e); });
	},
	
	render: function(sockStat) {
		if (sockStat) {
			this.$el.empty().append(this.connected);
		} else {
			this.$el.empty().append(this.disconnected);
			if (this.displayModal) {
				this.showModal();
			}
		}
		initFoundation();
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