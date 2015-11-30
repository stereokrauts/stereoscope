define(['underscore', 'backbone', 'commons', 'model.layoutManager/LOManagerItemModel'],
		function(_, Backbone, commons, LOManagerItemModel) {

	xdescribe('A Layout Item Model', function() {
		var dispatcher = null;
		var layoutItem = null;

		beforeEach(function() {
			dispatcher = _.extend({}, Backbone.Events);
			layoutItem = new LOManagerItemModel({fname: 'Horst Hrubesch', dispatcher: dispatcher});
		});

		afterEach(function() {
			layoutItem, dispatcher = null;
		});

		it('should be able to create its test objects', function() {
			expect(dispatcher).toBeDefined();
			expect(layoutItem).toBeDefined();
		});

		/*
		it('should fail if not properly initialized', function() {
			expect(function() { new LOManagerItemModel(); }).toThrow(new Error('Illegal LayoutItemModel object initialization: TypeError: options is undefined'));
			expect(function() { new LOManagerItemModel({nastyArg: 'blublub'}); }).toThrow(new Error('Illegal LayoutItemModel object initialization: Argument "name" and/or "dispatcher" missing.'));
		});
		*/

		describe('has a default value which', function() {
			it('is "name" of type String', function() {
				expect(layoutItem.get('name')).toEqual('test');
			});

			it('is "armed" of type boolean', function() {
				expect(layoutItem.get('armed')).toBeFalsy();
			});

			it('is "active" of type boolean', function() {
				expect(layoutItem.get('active')).toBeFalsy();
			});
		});


		// All the toggle stuff is now event based. Add new tests here
		/*
	it('should toggle its status (active/inactive)', function() {
		expect(layoutItem.get('active')).toBeFalsy();

		layoutItem.toggleActive();
		expect(layoutItem.get('active')).toBeTruthy();

		layoutItem.toggleActive();
		expect(layoutItem.get('active')).toBeFalsy();
	});

	it('should toggle its status (armed/unarmed)', function() {
		expect(layoutItem.get('armed')).toBeFalsy();

		layoutItem.toggleArmed();
		expect(layoutItem.get('armed')).toBeTruthy();

		layoutItem.toggleArmed();
		expect(layoutItem.get('armed')).toBeFalsy();
	});

	it('should reset its armed status', function() {
		expect(layoutItem.get('armed')).toBeFalsy();
		layoutItem.reset();
		expect(layoutItem.get('armed')).toBeFalsy();

		layoutItem.toggleArmed();
		expect(layoutItem.get('armed')).toBeTruthy();
		layoutItem.reset();
		expect(layoutItem.get('armed')).toBeFalsy();
	});

	it('should ensure that its the only armed object in its collection', function() {
		collection.add(new LOManagerItemModel({
			name: 'other', parent: parent, collection: collection
		}));

		var other = collection.findWhere({name: 'other'});
		other.toggleArmed();
		expect(other.get('armed')).toBeTruthy();

		layoutItem.toggleIntelliArmed();
		expect(layoutItem.get('armed')).toBeTruthy();
		expect(other.get('armed')).toBeFalsy();

	});

	it('should ensure that its the only active object in its collection', function() {
		collection.add(new LOManagerItemModel({
			name: 'other', parent: parent, collection: collection
		}));

		var other = collection.findWhere({name: 'other'});
		other.toggleActive();
		expect(other.get('active')).toBeTruthy();

		layoutItem.toggleIntelliActive();
		expect(layoutItem.get('active')).toBeTruthy();
		expect(other.get('active')).toBeFalsy();
	});
		 */

		it('should fire a layout request', function() {
			response = {
					fakeHandler: function() {
						return true;
					}	
			};
			spyOn(response, 'fakeHandler');
			dispatcher.bind(commons.osc.LAYOUT_REQUEST, response.fakeHandler());

			layoutItem.loadLayout();
			expect(response.fakeHandler).toHaveBeenCalled();
		});
	});
});