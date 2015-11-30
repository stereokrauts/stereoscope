define(['underscore', 'commons'], function(_, commons) {

	describe('A commons file', function() {

		it('should have a "commons" root element', function() {
			expect(commons).toBeDefined();
		});

		it('should have a "layout" subsection', function() {
			expect(commons.layout).toBeDefined();
		});

		it('should have a "menu" subsection that is an array', function() {
			expect(commons.menu).toBeDefined();
			expect(_.isArray(commons.menu)).toBeTruthy();
		});

		it('should have a "osc" subsection', function() {
			expect(commons.osc).toBeDefined();
		});

		it('should convert color Hex to RGB', function() {
			var rgb = commons.color.convertHexToRgb('#ff05f2');
			expect(rgb).toEqual(
					jasmine.objectContaining({
						r: 255,
						g: 5,
						b: 242
					}));
		});

		it('should convert color RGB to Hex', function() {
			var hex = commons.color.convertRgbToHex({r: 255, g: 5, b: 242});
			expect(hex).toEqual('#ff05f2');
		});

		it('should convert pixel to em values', function() {
			var em1 = commons.convertPixelToEm(320);
			var em2 = commons.convertPixelToEm(667)
			expect(em1).toEqual(20);
			expect(em2).toEqual(41.688);
		});

		it('should replace the channel dummy in an osc address', function() {
			var input = '/input/n/level'; 
			var output = commons.osc.replaceChannelNumber(input, 5);
			expect(output).toEqual('/input/5/level');
		});

		it('should replace the counter dummy in an osc address', function() {
			var input = '/input/n/toAux/m/level';
			var output = commons.osc.replaceCounterNumber(input, 4);
			expect(output).toEqual('/input/n/toAux/4/level');
		});

		describe('should have layout commons which', function() {

			it('is IPAD_LANDSCAPE', function() {
				expect(commons.layout.IPAD_LANDSCAPE).toEqual({ x: 1024, y: 768 });
			});

			it('is IPAD_PORTRAIT', function() {
				expect(commons.layout.IPAD_PORTRAIT).toEqual({ x: 768, y: 1024 });
			});

			it('is IPHONE_LANDSCAPE', function() {
				expect(commons.layout.IPHONE_LANDSCAPE).toEqual({ x: 480, y: 320 });
			});

			it('is IPHONE_PORTRAIT', function() {
				expect(commons.layout.IPHONE_PORTRAIT).toEqual({ x: 320, y: 480 });
			});
		});

		describe('should provide constants for osc address patterns', function() {

			var PREFIX = '/stereoscope';

			it('LAYOUT_LIST_REQUEST', function() {
				var addr = PREFIX + '/system/request/frontend/layoutList';
				expect(commons.osc.LAYOUT_LIST_REQUEST).toEqual(addr);
			});

			it('LAYOUT_REQUEST', function() {
				var addr = PREFIX + '/system/request/frontend/layout/custom';
				expect(commons.osc.LAYOUT_REQUEST).toEqual(addr);
			});

			it('LAYOUT_LIST_RESPONSE', function() {
				var addr = PREFIX + '/system/response/frontend/layoutList';
				expect(commons.osc.LAYOUT_LIST_RESPONSE).toEqual(addr);
			});

			it('LAYOUT_RESPONSE', function() {
				var addr = PREFIX + '/system/response/frontend/layout';
				expect(commons.osc.LAYOUT_RESPONSE).toEqual(addr);
			});

			it('SYSTEM_RESYNC_ALL', function() {
				var addr = PREFIX + '/system/resync/all';
				expect(commons.osc.SYSTEM_RESYNC_ALL).toEqual(addr);
			});

			it('SYSTEM_HEARTBEAT', function() {
				var addr = PREFIX + '/system/heartbeat/n';
				expect(commons.osc.SYSTEM_HEARTBEAT).toEqual(addr);
			});

			it('SYSTEM_STATE_AUX_CHANGETO', function() {
				var addr = PREFIX + '/system/state/selectedAux/changeTo/n';
				expect(commons.osc.SYSTEM_STATE_AUX_CHANGETO).toEqual(addr);
			});

			it('SYSTEM_STATE_AUX_LABEL', function() {
				var addr = PREFIX + '/system/state/selectedAux/label';
				expect(commons.osc.SYSTEM_STATE_AUX_LABEL).toEqual(addr);
			});

			it('SYSTEM_STATE_GEQ_CHANGETO', function() {
				var addr = PREFIX + '/system/state/selectedGeq/changeTo/n';
				expect(commons.osc.SYSTEM_STATE_GEQ_CHANGETO).toEqual(addr);
			});

			it('SYSTEM_STATE_GEQ_LABEL', function() {
				var addr = PREFIX + '/system/state/selectedGeq/label';
				expect(commons.osc.SYSTEM_STATE_GEQ_LABEL).toEqual(addr);
			});

			it('INPUT_LEVEL', function() {
				var addr = PREFIX + '/input/n/level';
				expect(commons.osc.INPUT_LEVEL).toEqual(addr);
			});

			it('INPUT_CHANNELON', function() {
				var addr = PREFIX + '/input/n/channelOn';
				expect(commons.osc.INPUT_CHANNELON).toEqual(addr);
			});

			it('INPUT_LABEL', function() {
				var addr = PREFIX + '/input/n/label';
				expect(commons.osc.INPUT_LABEL).toEqual(addr);
			});

			it('INPUT_LEVELLABEL', function() {
				var addr = PREFIX + '/input/n/levelLabel';
				expect(commons.osc.INPUT_LEVELLABEL).toEqual(addr);
			});

			it('INPUT_TOAUX_LEVEL', function() {
				var addr = PREFIX + '/input/n/toAux/m/level';
				expect(commons.osc.INPUT_TOAUX_LEVEL).toEqual(addr);
			});

			it('INPUT_TOAUX_CHANNELON', function() {
				var addr = PREFIX + '/input/n/toAux/m/channelOn';
				expect(commons.osc.INPUT_TOAUX_CHANNELON).toEqual(addr);
			});

			it('INPUT_TOAUX_LEVELLABEL', function() {
				var addr = PREFIX + '/input/n/toAux/m/levelLabel';
				expect(commons.osc.INPUT_TOAUX_LEVELLABEL).toEqual(addr);
			});

			it('INPUT_PAN', function() {
				var addr = PREFIX + '/input/n/pan';
				expect(commons.osc.INPUT_PAN).toEqual(addr);
			});

			it('INPUT_PANLABEL', function() {
				var addr = PREFIX + '/input/n/panLabel';
				expect(commons.osc.INPUT_PANLABEL).toEqual(addr);
			});

			it('INPUT_CHANNELONLABEL', function() {
				var addr = PREFIX + '/input/n/channelOnLabel';
				expect(commons.osc.INPUT_CHANNELONLABEL).toEqual(addr);
			});

			it('INPUT_PEQ_ON', function() {
				var addr = PREFIX + '/input/n/peq/peqOn';
				expect(commons.osc.INPUT_PEQ_ON).toEqual(addr);
			});

			it('INPUT_PEQ_ONLABEL', function() {
				var addr = PREFIX + '/input/n/peq/peqOnLabel';
				expect(commons.osc.INPUT_PEQ_ONLABEL).toEqual(addr);
			});

			it('INPUT_PEQ_MODE', function() {
				var addr = PREFIX + '/input/n/peq/mode';
				expect(commons.osc.INPUT_PEQ_MODE).toEqual(addr);
			});

			it('INPUT_PEQ_MODELABEL', function() {
				var addr = PREFIX + '/input/n/peq/modeLabel';
				expect(commons.osc.INUT_PEQ_MODELABEL).toEqual(addr);
			});

			it('INPUT_PEQ_Q', function() {
				var addr = PREFIX + '/input/n/peq/band/m/q';
				expect(commons.osc.INPUT_PEQ_Q).toEqual(addr);
			});

			it('INPUT_PEQ_F', function() {
				var addr = PREFIX + '/input/n/peq/band/m/f';
				expect(commons.osc.INPUT_PEQ_F).toEqual(addr);
			});

			it('INPUT_PEQ_G', function() {
				var addr = PREFIX + '/input/n/peq/band/m/g';
				expect(commons.osc.INPUT_PEQ_G).toEqual(addr);
			});

			it('INPUT_PEQ_QLABEL', function() {
				var addr = PREFIX + '/input/n/peq/band/m/qLabel';
				expect(commons.osc.INPUT_PEQ_QLABEL).toEqual(addr);
			});

			it('INPUT_PEQ_FLABEL', function() {
				var addr = PREFIX + '/input/n/peq/band/m/fLabel';
				expect(commons.osc.INPUT_PEQ_FLABEL).toEqual(addr);
			});

			it('INPUT_PEQ_GLABEL', function() {
				var addr = PREFIX + '/input/n/peq/band/m/gLabel';
				expect(commons.osc.INPUT_PEQ_GLABEL).toEqual(addr);
			});

			it('INPUT_DYNA_ON', function() {
				var addr = PREFIX + '/input/n/dynamics/m/dynaOn';
				expect(commons.osc.INPUT_DYNA_ON).toEqual(addr);
			});

			it('INPUT_DYNA_ONLABEL', function() {
				var addr = PREFIX + '/input/n/dynamics/m/dynaOnLabel';
				expect(commons.osc.INPUT_DYNA_ONLABEL).toEqual(addr);
			});

			it('INPUT_DYNA_ATTACK', function() {
				var addr = PREFIX + '/input/n/dynamics/m/attack';
				expect(commons.osc.INPUT_DYNA_ATTACK).toEqual(addr);
			});

			it('INPUT_DYNA_ATTACKLABEL', function() {
				var addr = PREFIX + '/input/n/dynamics/m/attackLabel';
				expect(commons.osc.INPUT_DYNA_ATTACKLABEL).toEqual(addr);
			});

			it('INPUT_DYNA_DECAY', function() {
				var addr = PREFIX + '/input/n/dynamics/m/decay';
				expect(commons.osc.INPUT_DYNA_DECAY).toEqual(addr);
			});

			it('INPUT_DYNA_DECAYLABEL', function() {
				var addr = PREFIX + '/input/n/dynamics/m/decayLabel';
				expect(commons.osc.INPUT_DYNA_DECAYLABEL).toEqual(addr);
			});

			it('INPUT_DYNA_GAIN', function() {
				var addr = PREFIX + '/input/n/dynamics/m/gain';
				expect(commons.osc.INPUT_DYNA_GAIN).toEqual(addr);
			});

			it('INPUT_DYNA_GAINLABEL', function() {
				var addr = PREFIX + '/input/n/dynamics/m/gainLabel';
				expect(commons.osc.INPUT_DYNA_GAINLABEL).toEqual(addr);
			});

			it('INPUT_DYNA_HOLD', function() {
				var addr = PREFIX + '/input/n/dynamics/m/hold';
				expect(commons.osc.INPUT_DYNA_HOLD).toEqual(addr);
			});

			it('INPUT_DYNA_HOLDLABEL', function() {
				var addr = PREFIX + '/input/n/dynamics/m/holdLabel';
				expect(commons.osc.INPUT_DYNA_HOLDLABEL).toEqual(addr);
			});

			it('INPUT_DYNA_KNEE', function() {
				var addr = PREFIX + '/input/n/dynamics/m/knee';
				expect(commons.osc.INPUT_DYNA_KNEE).toEqual(addr);
			});

			it('INPUT_DYNA_KNEELABEL', function() {
				var addr = PREFIX + '/input/n/dynamics/m/kneeLabel';
				expect(commons.osc.INPUT_DYNA_KNEELABEL).toEqual(addr);
			});

			it('INPUT_DYNA_RANGE', function() {
				var addr = PREFIX + '/input/n/dynamics/m/range';
				expect(commons.osc.INPUT_DYNA_RANGE).toEqual(addr);
			});

			it('INPUT_DYNA_RANGELABEL', function() {
				var addr = PREFIX + '/input/n/dynamics/m/rangeLabel';
				expect(commons.osc.INPUT_DYNA_RANGELABEL).toEqual(addr);
			});

			it('INPUT_DYNA_RATIO', function() {
				var addr = PREFIX + '/input/n/dynamics/m/ratio';
				expect(commons.osc.INPUT_DYNA_RATIO).toEqual(addr);
			});

			it('INPUT_DYNA_RATIOLABEL', function() {
				var addr = PREFIX + '/input/n/dynamics/m/ratioLabel';
				expect(commons.osc.INPUT_DYNA_RATIOLABEL).toEqual(addr);
			});

			it('INPUT_DYNA_RELEASE', function() {
				var addr = PREFIX + '/input/n/dynamics/m/release';
				expect(commons.osc.INPUT_DYNA_RELEASE).toEqual(addr);
			});

			it('INPUT_DYNA_RELEASELABEL', function() {
				var addr = PREFIX + '/input/n/dynamics/m/releaseLabel';
				expect(commons.osc.INPUT_DYNA_RELEASELABEL).toEqual(addr);
			});

			it('INPUT_DYNA_THRESHOLD', function() {
				var addr = PREFIX + '/input/n/dynamics/m/threshold';
				expect(commons.osc.INPUT_DYNA_THRESHOLD).toEqual(addr);
			});

			it('INPUT_DYNA_THRESHOLDLABEL', function() {
				var addr = PREFIX + '/input/n/dynamics/m/thresholdLabel';
				expect(commons.osc.INPUT_DYNA_THRESHOLDLABEL).toEqual(addr);
			});

			it('OUTPUT_MASTER_LEVEL', function() {
				var addr = PREFIX + '/output/master/level';
				expect(commons.osc.OUTPUT_MASTER_LEVEL).toEqual(addr);
			});

			it('OUTPUT_AUX_LEVEL', function() {
				var addr = PREFIX + '/output/aux/n/level';
				expect(commons.osc.OUTPUT_AUX_LEVEL).toEqual(addr);
			});

			it('OUTPUT_BUS_LEVEL', function() {
				var addr = PREFIX + '/output/bus/n/level';
				expect(commons.osc.OUTPUT_BUS_LEVEL).toEqual(addr);
			});

			it('OUTPUT_MATRIX_LEVEL', function() {
				var addr = PREFIX + '/output/matrix/n/level';
				expect(commons.osc.OUTPUT_MATRIX_LEVEL).toEqual(addr);
			});

			it('OUTPUT_OMNI_LEVEL', function() {
				var addr = PREFIX + '/output/omni/n/level';
				expect(commons.osc.OUTPUT_OMNI_LEVEL).toEqual(addr);
			});

			it('OUTPUT_BUS_DELAY', function() {
				var addr = PREFIX + '/output/bus/n/delay';
				expect(commons.osc.OUTPUT_BUS_DELAY).toEqual(addr);
			});

			it('OUTPUT_AUX_DELAY', function() {
				var addr = PREFIX + '/output/aux/n/delay';
				expect(commons.osc.OUTPUT_AUX_DELAY).toEqual(addr);
			});

			it('OUTPUT_OMNI_DELAY', function() {
				var addr = PREFIX + '/output/omni/n/delay';
				expect(commons.osc.OUTPUT_OMNI_DELAY).toEqual(addr);
			});

			it('OUTPUT_MATRIX_DELAY', function() {
				var addr = PREFIX + '/output/matrix/n/delay';
				expect(commons.osc.OUTPUT_MATRIX_DELAY).toEqual(addr);
			});

			it('STATEFUL_AUX_SEND_LEVEL', function() {
				var addr = PREFIX + '/stateful/aux/level/fromChannel/n';
				expect(commons.osc.STATEFUL_AUX_SEND_LEVEL).toEqual(addr);
			});

			it('STATEFUL_AUX_SEND_LEVELLABEL', function() {
				var addr = PREFIX + '/stateful/aux/levelLabel/fromChannel/n';
				expect(commons.osc.STATEFUL_AUX_SEND_LEVELLABEL).toEqual(addr);
			});

			it('STATEFUL_AUX_SEND_CHANNELON', function() {
				var addr = PREFIX + '/stateful/aux/channelOn/fromChannel/n';
				expect(commons.osc.STATEFUL_AUX_SEND_CHANNELON).toEqual(addr);
			});

			it('STATEFUL_AUX_OUTPUT_LEVEL', function() {
				var addr = PREFIX + '/stateful/aux/level';
				expect(commons.osc.STATEFUL_AUX_OUTPUT_LEVEL).toEqual(addr);
			});

			it('STATEFUL_GEQ_BAND_LEVEL', function() {
				var addr = PREFIX + '/stateful/dsp/geq/band/n/left/level';
				expect(commons.osc.STATEFUL_GEQ_BAND_LEVEL).toEqual(addr);
			});

			it('STATEFUL_GEQ_BAND_RESET', function() {
				var addr = PREFIX + '/stateful/dsp/geq/band/n/left/reset';
				expect(commons.osc.STATEFUL_GEQ_BAND_RESET).toEqual(addr);
			});

			it('STATEFUL_GEQ_RESET', function() {
				var addr = PREFIX + '/stateful/dsp/geq/resetGeq';
				expect(commons.osc.STATEFUL_GEQ_RESET).toEqual(addr);
			});

			it('STATEFUL_GEQ_FLEXEQ_HIDE', function() {
				var addr = PREFIX + '/stateful/dsp/geq/isFlexEq15/hidden';
				expect(commons.osc.STATEFUL_GEQ_FLEXEQ_HIDE).toEqual(addr);
			});

			it('STATEFUL_INPUT_LEVEL', function() {
				var addr = PREFIX + '/stateful/input/misc/level';
				expect(commons.osc.STATEFUL_INPUT_LEVEL).toEqual(addr);
			});

			it('STATEFUL_INPUT_CHANNELON', function() {
				var addr = PREFIX + '/stateful/input/misc/channelOn';
				expect(commons.osc.STATEFUL_INPUT_CHANNELON).toEqual(addr);
			});

			it('STATEFUL_INPUT_PAN', function() {
				var addr = PREFIX + '/stateful/input/misc/pan';
				expect(commons.osc.STATEFUL_INPUT_PAN).toEqual(addr);
			});

			it('STATEFUL_INPUT_LABEL', function() {
				var addr = PREFIX + '/stateful/input/misc/channelLabel';
				expect(commons.osc.STATEFUL_INPUT_LABEL).toEqual(addr);
			});

			it('STATEFUL_INPUT_LEVELLABEL', function()  {
				var addr = PREFIX + '/stateful/input/misc/levelLabel';
				expect(commons.osc.STATEFUL_INPUT_LEVELLABEL).toEqual(addr);
			});

			it('STATEFUL_INPUT_CHANNELONLABEL', function() {
				var addr = PREFIX + '/stateful/input/misc/channelOnLabel';
				expect(commons.osc.STATEFUL_INPUT_CHANNELONLABEL).toEqual(addr);
			});

			it('STATEFUL_INPUT_PANLABEL', function() {
				var addr = PREFIX + '/stateful/input/misc/panLabel';
				expect(commons.osc.STATEFUL_INPUT_PANLABEL).toEqual(addr);
			});

			it('STATEFUL_INPUT_PEQ_ON', function() {
				var addr = PREFIX + '/stateful/input/peq/peqOn';
				expect(commons.osc.STATEFUL_INPUT_PEQ_ON).toEqual(addr);
			});

			it('STATEFUL_INPUT_PEQ_ONLABEL', function() {
				var addr = PREFIX + '/stateful/input/peq/peqOnLabel';
				expect(commons.osc.STATEFUL_INPUT_PEQ_ONLABEL).toEqual(addr);
			});

			it('STATEFUL_INPUT_PEQ_MODE', function() {
				var addr = PREFIX + '/stateful/input/peq/mode';
				expect(commons.osc.STATEFUL_INPUT_PEQ_MODE).toEqual(addr);
			});

			it('STATEFUL_INPUT_PEQ_MODELABEL', function() {
				var addr = PREFIX + '/stateful/input/peq/modeLabel';
				expect(commons.osc.STATEFUL_INPUT_PEQ_MODELABEL).toEqual(addr);
			});

			it('STATEFUL_INPUT_PEQ_Q', function() {
				var addr = PREFIX + '/stateful/input/peq/band/n/q';
				expect(commons.osc.STATEFUL_INPUT_PEQ_Q).toEqual(addr);
			});

			it('STATEFUL_INPUT_PEQ_F', function() {
				var addr = PREFIX + '/stateful/input/peq/band/n/f';
				expect(commons.osc.STATEFUL_INPUT_PEQ_F).toEqual(addr);
			});

			it('STATEFUL_INPUT_PEQ_G', function() {
				var addr = PREFIX + '/stateful/input/peq/band/n/g';
				expect(commons.osc.STATEFUL_INPUT_PEQ_G).toEqual(addr);
			});

			it('STATEFUL_INPUT_PEQ_QLABEL', function() {
				var addr = PREFIX + '/stateful/input/peq/band/n/qLabel';
				expect(commons.osc.STATEFUL_INPUT_PEQ_QLABEL).toEqual(addr);
			});

			it('STATEFUL_INPUT_PEQ_FLABEL', function() {
				var addr = PREFIX + '/stateful/input/peq/band/n/fLabel';
				expect(commons.osc.STATEFUL_INPUT_PEQ_FLABEL).toEqual(addr);
			});

			it('STATEFUL_INPUT_PEQ_GLABEL', function() {
				var addr = PREFIX + '/stateful/input/peq/band/n/gLabel';
				expect(commons.osc.STATEFUL_INPUT_PEQ_GLABEL).toEqual(addr);
			});

			it('STATEFUL_INPUT_DYNA_ON', function() {
				var addr = PREFIX + '/stateful/input/dynamics/n/dynaOn';
				expect(commons.osc.STATEFUL_INPUT_DYNA_ON).toEqual(addr);
			});

			it('STATEFUL_INPUT_DYNA_ONLABEL', function() {
				var addr = PREFIX + '/stateful/input/dynamics/n/dynaOnLabel';
				expect(commons.osc.STATEFUL_INPUT_DYNA_ONLABEL).toEqual(addr);
			});

			it('STATEFUL_INPUT_DYNA_ATTACK', function() {
				var addr = PREFIX  + '/stateful/input/dynamics/n/attack';
				expect(commons.osc.STATEFUL_INPUT_DYNA_ATTACK).toEqual(addr);
			});

			it('STATEFUL_INPUT_DYNA_ATTACKLABEL', function() {
				var addr = PREFIX + '/stateful/input/dynamics/n/attackLabel';
				expect(commons.osc.STATEFUL_INPUT_DYNA_ATTACKLABEL).toEqual(addr);
			});

			it('STATEFUL_INPUT_DYNA_DECAY', function() {
				var addr = PREFIX + '/stateful/input/dynamics/n/decay';
				expect(commons.osc.STATEFUL_INPUT_DYNA_DECAY).toEqual(addr);
			});

			it('STATEFUL_INPUT_DYNA_DECAYLABEL', function() {
				var addr = PREFIX + '/stateful/input/dynamics/n/decayLabel';
				expect(commons.osc.STATEFUL_INPUT_DYNA_DECAYLABEL).toEqual(addr);
			});

			it('STATEFUL_INPUT_DYNA_GAIN', function() {
				var addr = PREFIX + '/stateful/input/dynamics/n/gain';
				expect(commons.osc.STATEFUL_INPUT_DYNA_GAIN).toEqual(addr);
			});

			it('STATEFUL_INPUT_DYNA_GAINLABEL', function() {
				var addr = PREFIX + '/stateful/input/dynamics/n/gainLabel';
				expect(commons.osc.STATEFUL_INPUT_DYNA_GAINLABEL).toEqual(addr);
			});

			it('STATEFUL_INPUT_DYNA_HOLD', function() {
				var addr = PREFIX + '/stateful/input/dynamics/n/hold';
				expect(commons.osc.STATEFUL_INPUT_DYNA_HOLD).toEqual(addr);
			});

			it('STATEFUL_INPUT_DYNA_HOLDLABEL', function() {
				var addr = PREFIX + '/stateful/input/dynamics/n/holdLabel';
				expect(commons.osc.STATEFUL_INPUT_DYNA_HOLDLABEL).toEqual(addr);
			});

			it('STATEFUL_INPUT_DYNA_KNEE', function() {
				var addr = PREFIX + '/stateful/input/dynamics/n/knee';
				expect(commons.osc.STATEFUL_INPUT_DYNA_KNEE).toEqual(addr);
			});

			it('STATEFUL_INPUT_DYNA_KNEELABEL', function() {
				var addr = PREFIX + '/stateful/input/dynamics/n/kneeLabel';
				expect(commons.osc.STATEFUL_INPUT_DYNA_KNEELABEL).toEqual(addr);
			});

			it('STATEFUL_INPUT_DYNA_RANGE', function() {
				var addr = PREFIX + '/stateful/input/dynamics/n/range';
				expect(commons.osc.STATEFUL_INPUT_DYNA_RANGE).toEqual(addr);
			});

			it('STATEFUL_INPUT_DYNA_RANGELABEL', function() {
				var addr = PREFIX + '/stateful/input/dynamics/n/rangeLabel';
				expect(commons.osc.STATEFUL_INPUT_DYNA_RANGELABEL).toEqual(addr);
			});

			it('STATEFUL_INPUT_DYNA_RATIO', function() {
				var addr = PREFIX + '/stateful/input/dynamics/n/ratio';
				expect(commons.osc.STATEFUL_INPUT_DYNA_RATIO).toEqual(addr);
			});

			it('STATEFUL_INPUT_DYNA_RATIOLABEL', function() {
				var addr = PREFIX + '/stateful/input/dynamics/n/ratioLabel';
				expect(commons.osc.STATEFUL_INPUT_DYNA_RATIOLABEL).toEqual(addr);
			});

			it('STATEFUL_INPUT_DYNA_RELEASE', function() {
				var addr = PREFIX + '/stateful/input/dynamics/n/release';
				expect(commons.osc.STATEFUL_INPUT_DYNA_RELEASE).toEqual(addr);
			});

			it('STATEFUL_INPUT_DYNA_RELEASELABEL', function() {
				var addr = PREFIX + '/stateful/input/dynamics/n/releaseLabel';
				expect(commons.osc.STATEFUL_INPUT_DYNA_RELEASELABEL).toEqual(addr);
			});

			it('STATEFUL_INPUT_DYNA_THRESHOLD', function() {
				var addr = PREFIX + '/stateful/input/dynamics/n/threshold';
				expect(commons.osc.STATEFUL_INPUT_DYNA_THRESHOLD).toEqual(addr);
			});

			it('STATEFUL_INPUT_DYNA_THRESHOLDLABEL', function() {
				var addr = PREFIX + '/stateful/input/dynamics/n/thresholdLabel';
				expect(commons.osc.STATEFUL_INPUT_DYNA_THRESHOLDLABEL).toEqual(addr);
			});
		});
	});
});