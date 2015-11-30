define(['underscore', 'backbone', 'easel', 'view.layoutManager/LOManagerItemView'],
		function(_, Backbone, createjs, LOManagerItemView) {

	xdescribe('A Layout Item View', function() {
		var layoutItemView = null, itemModel = null;

		beforeEach(function() {
			var ItemModel = Backbone.Model.extend({
				toggleIntelliArmed: function() { return true; }
			});

			itemModel = new ItemModel();
			layoutItemView = new LOManagerItemView({model: itemModel});
		});

		afterEach(function() {
			layoutItemView, itemModel = null;
		});

		it('should be able to create its test objects', function() {
			expect(itemModel).toBeDefined();
			expect(layoutItemView).toBeDefined();
		});

		/*
		it('should fail if not properly initialized', function() {
			expect(function() { new LOManagerItemView(); }).toThrow(new Error('Illegal LayoutItemView object initialization: TypeError: options is undefined'));
			expect(function() { new LOManagerItemView({nastyArg: 'blublub'}); }).toThrow(new Error('Illegal LayoutItemView object initialization: Argument "model" missing.'));
		});
		*/

		describe('has default values that', function() {

			it('is a html/css class', function() {
				expect($(layoutItemView.el).hasClass('layoutItem')).toBeTruthy();
				expect(layoutItemView.className).toEqual('layoutItem');
			});
		});

		// rewritten: now event based
		/*
	it('should change the model\'s state to "armed"', function() {
		spyOn(itemModel, 'toggleIntelliArmed');
		layoutItemView.arm();
		expect(itemModel.toggleIntelliArmed).toHaveBeenCalled();
	});
		 */

		it('should write it\'s name as html string', function() {
			expect(layoutItemView.addHtmlItemName('blublub')).toEqual('<span class="layoutItemName">blublub</span>');
		});

		it('should built a html button', function() {
			expect(layoutItemView.addHtmlButton('blublub')).toEqual(
			'<input class="fire" type="button" name="blublub" value="Load this layout" />');
		});

		it('should write "active" as html string', function() {
			expect(layoutItemView.addHtmlActiveString()).toEqual('<span class="active">(active)</span>');
		});

		describe('can render html and', function() {
			/**
			 * this is dangerous because it tests the cached
			 * results, not the resulting dom itself. It should
			 * be equal but it's not the same. Mocking the dom
			 * can be quite a complex task and is over the shelf.
			 */
			it('should return the view object after rendering', function() {
				expect(layoutItemView.render()).toEqual(layoutItemView);
			});

			it('should output its name if not armed or active', function() {
				layoutItemView.model.set({name: 'blub', armed: false, active: false});
				var expectable = $(layoutItemView.el).find('span.layoutItemName');
				expect(expectable.text()).toEqual('blub');
			});

			it('should output its name and a button when armed', function() {
				layoutItemView.model.set({name: 'blub', armed: true, active: false});
				var expectable = $(layoutItemView.el).find('span.layoutItemName');
				expect(expectable.text()).toEqual('blub');
				var expectable = $(layoutItemView.el).find('input');
				expect(expectable.attr('class')).toEqual('fire');
				expect(expectable.attr('type')).toEqual('button');
				expect(expectable.attr('name')).toEqual('blub');
				expect(expectable.attr('value')).toEqual('Load this layout');

			});

			it('should output its name and an "active" label when active', function() {
				layoutItemView.model.set({name: 'blub', armed: false, active: true});
				var expectable = $(layoutItemView.el).find('span.layoutItemName');
				expect(expectable.text()).toEqual('blub');
				var expectable = $(layoutItemView.el).find('span.active');
				expect(expectable.text()).toEqual('(active)');
			});

		});

	});
});