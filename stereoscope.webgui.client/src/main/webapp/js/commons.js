/**
 * This module holds all configs and static stuff
 * besides some handy functions that can be used
 * all over the app.
 */
define([
        'underscore',
], function(_) {
	'use strict';
	
	var PREFIX = '/stereoscope';
	
	return {
		name: 'Stereoscope',
		version: '3.0.0-SNAPSHOT',

		// sections in the main menu
		menu: [
		       'Mixer',
		       'Layout_Manager',
		       'Pong',
		       'About',
		       //'DarkTheme',
		       //'BrightTheme'
		],

		layout: {
			IPAD_LANDSCAPE:   { x: 1024, y: 768 },
			IPAD_PORTRAIT:    { x: 768, y: 1024 },
			IPHONE_LANDSCAPE: { x: 480, y: 320 },
			IPHONE_PORTRAIT:  { x: 320, y: 480 }
		},

		
        
        // color palette
        color: {
        	RED:	'#ff281c',
        	GREEN:	'#75cc26',
        	BLUE:	'#00c4a8',
        	YELLOW:	'#ffed00',
        	PURPLE:	'#aa7faa',
        	GRAY:	'#b2b2b2',
        	ORANGE:	'#f9a01c',
        	BROWN:	'#826647',
        	PINK:	'#ff05f2',
        	WHITE:  '#ffffff',
        	
        	ui: {
        		layoutBg:	'#3c3c3c'
        	},

        	convertHexToRgb: function(hex) {
        		var result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
        		return result ? {
        			r: parseInt(result[1], 16),
        			g: parseInt(result[2], 16),
        			b: parseInt(result[3], 16)
        		} : null;
        	},
        	
        	convertRgbToHex: function(rgb) {
        		return "#" + ((1 << 24) + (rgb.r << 16) + (rgb.g << 8) + rgb.b).toString(16).slice(1);
        	}
        },

        // this can be used by all objects that do osc server requests
        createRequestObject: function(oscStr, value) {
     	   return {'type': 'string', 'oscStr': oscStr, 'val': value};
        },
        
        convertPixelToEm: function(px) {
        	return Math.round(px * 62.5) / 1000;
        },
        
        osc: {
        	/* system messages */
        	LAYOUT_LIST_REQUEST:	PREFIX + '/system/request/frontend/layoutList',
        	LAYOUT_REQUEST:			PREFIX + '/system/request/frontend/layout/custom',
        	LAYOUT_STD_REQUEST:		PREFIX + '/system/request/frontend/layout/std',
        	LAYOUT_AUX_REQUEST:		PREFIX + '/system/request/frontend/layout/aux',
        	LAYOUT_LIST_RESPONSE:	PREFIX + '/system/response/frontend/layoutList',
        	LAYOUT_RESPONSE:		PREFIX + '/system/response/frontend/layout',
        	LAYOUT_TOSC_UPLOAD:		PREFIX + '/system/interchange/touchosc',
        	
        	SYSTEM_MIXERPROP_REQUEST:		PREFIX + '/system/request/mixerProperties',
        	SYSTEM_MIXERPROP_RESPONSE:		PREFIX + '/system/response/mixerProperties',
        	SYSTEM_HEARTBEAT:				PREFIX + '/system/heartbeat/n',
        	SYSTEM_STATE_AUX_CHANGETO:		PREFIX + '/system/state/selectedAux/changeTo/n',
        	SYSTEM_STATE_AUX_LABEL:			PREFIX + '/system/state/selectedAux/label',
        	SYSTEM_STATE_GEQ_CHANGETO:		PREFIX + '/system/state/selectedGeq/changeTo/n',
        	SYSTEM_STATE_GEQ_LABEL:			PREFIX + '/system/state/selectedGeq/label',
        	SYSTEM_RESYNC_ALL:				PREFIX + '/system/resync/all',
        	SYSTEM_RESYNC_STATEFUL:			PREFIX + '/system/resync/stateful',
        	SYSTEM_RESYNC_SELECTED_AUX:		PREFIX + '/system/resync/selectedAux',
        	SYSTEM_RESYNC_SCENE_CHANGE:		PREFIX + '/system/resync/sceneChange',
        	SYSTEM_RESYNC_CHN_LEVELS:		PREFIX + '/system/resync/channelLevels',
        	SYSTEM_RESYNC_CHN_NAMES:		PREFIX + '/system/resync/channelNames',
        	SYSTEM_RESYNC_CHN_ON:			PREFIX + '/system/resync/channelOn',
        	SYSTEM_RESYNC_AUX_SEND_LEVELS:	PREFIX + '/system/resync/auxSendLevels',
        	SYSTEM_RESYNC_AUX_SEND_ON:		PREFIX + '/system/resync/auxSendOn',
        	SYSTEM_RESYNC_GEQ_BAND_LEVELS:	PREFIX + '/system/resync/geqBandLevels',
        	SYSTEM_RESYNC_DELAYTIMES:		PREFIX + '/system/resync/delayTimes',
        	SYSTEM_RESYNC_CHANNELSTRIP:		PREFIX + '/system/resync/channelStrip',
        	SYSTEM_RESYNC_OUTPUTS:			PREFIX + '/system/resync/outputs',
        	
        	/* common mixer messages */
        	INPUT_LEVEL:		PREFIX + '/input/n/level',
        	INPUT_CHANNELON:	PREFIX + '/input/n/channelOn',
        	INPUT_LABEL:		PREFIX + '/input/n/label',
        	INPUT_LEVELLABEL:	PREFIX + '/input/n/levelLabel',
        	
        	INPUT_TOAUX_LEVEL:		PREFIX + '/input/n/toAux/m/level',
        	INPUT_TOAUX_CHANNELON:	PREFIX + '/input/n/toAux/m/channelOn',
        	INPUT_TOAUX_LEVELLABEL:	PREFIX + '/input/n/toAux/m/levelLabel',
        	
        	/* NEW! Non-stateful strip parameters. Currently just for fake server */
        	INPUT_PAN:				PREFIX + '/input/n/pan',
        	
        	INPUT_CHANNELONLABEL:	PREFIX + '/input/n/channelOnLabel',
        	INPUT_PANLABEL:			PREFIX + '/input/n/panLabel',
        	
        	INPUT_PEQ_ON:			PREFIX + '/input/n/peq/peqOn',
        	INPUT_PEQ_ONLABEL:		PREFIX + '/input/n/peq/peqOnLabel',
        	INPUT_PEQ_MODE:			PREFIX + '/input/n/peq/mode',
        	INUT_PEQ_MODELABEL:		PREFIX + '/input/n/peq/modeLabel',
        	INPUT_PEQ_Q:			PREFIX + '/input/n/peq/band/m/q',
        	INPUT_PEQ_F:			PREFIX + '/input/n/peq/band/m/f',
        	INPUT_PEQ_G:			PREFIX + '/input/n/peq/band/m/g',
        	INPUT_PEQ_QLABEL:		PREFIX + '/input/n/peq/band/m/qLabel',
        	INPUT_PEQ_FLABEL:		PREFIX + '/input/n/peq/band/m/fLabel',
        	INPUT_PEQ_GLABEL:		PREFIX + '/input/n/peq/band/m/gLabel',
        	
        	INPUT_DYNA_ON:				PREFIX + '/input/n/dynamics/m/dynaOn',
        	INPUT_DYNA_ONLABEL:			PREFIX + '/input/n/dynamics/m/dynaOnLabel',
        	INPUT_DYNA_ATTACK:			PREFIX + '/input/n/dynamics/m/attack',
        	INPUT_DYNA_ATTACKLABEL:		PREFIX + '/input/n/dynamics/m/attackLabel',
        	INPUT_DYNA_DECAY:			PREFIX + '/input/n/dynamics/m/decay',
        	INPUT_DYNA_DECAYLABEL:		PREFIX + '/input/n/dynamics/m/decayLabel',
        	INPUT_DYNA_GAIN:			PREFIX + '/input/n/dynamics/m/gain',
        	INPUT_DYNA_GAINLABEL:		PREFIX + '/input/n/dynamics/m/gainLabel',
        	INPUT_DYNA_HOLD:			PREFIX + '/input/n/dynamics/m/hold',
        	INPUT_DYNA_HOLDLABEL:		PREFIX + '/input/n/dynamics/m/holdLabel',
        	INPUT_DYNA_KNEE:			PREFIX + '/input/n/dynamics/m/knee',
        	INPUT_DYNA_KNEELABEL:		PREFIX + '/input/n/dynamics/m/kneeLabel',
        	INPUT_DYNA_RANGE:			PREFIX + '/input/n/dynamics/m/range',
        	INPUT_DYNA_RANGELABEL:		PREFIX + '/input/n/dynamics/m/rangeLabel',
        	INPUT_DYNA_RATIO:			PREFIX + '/input/n/dynamics/m/ratio',
        	INPUT_DYNA_RATIOLABEL:		PREFIX + '/input/n/dynamics/m/ratioLabel',
        	INPUT_DYNA_RELEASE:			PREFIX + '/input/n/dynamics/m/release',
        	INPUT_DYNA_RELEASELABEL:	PREFIX + '/input/n/dynamics/m/releaseLabel',
        	INPUT_DYNA_THRESHOLD:		PREFIX + '/input/n/dynamics/m/threshold',
        	INPUT_DYNA_THRESHOLDLABEL:	PREFIX + '/input/n/dynamics/m/thresholdLabel',
        	
        	
        	OUTPUT_MASTER_LEVEL:	PREFIX + '/output/master/level',
        	OUTPUT_AUX_LEVEL:		PREFIX + '/output/aux/n/level',
        	OUTPUT_BUS_LEVEL:		PREFIX + '/output/bus/n/level',
        	OUTPUT_MATRIX_LEVEL:	PREFIX + '/output/matrix/n/level',
        	OUTPUT_OMNI_LEVEL:		PREFIX + '/output/omni/n/level',
        	
        	OUTPUT_BUS_DELAY:		PREFIX + '/output/bus/n/delay',
        	OUTPUT_AUX_DELAY:		PREFIX + '/output/aux/n/delay',
        	OUTPUT_OMNI_DELAY:		PREFIX + '/output/omni/n/delay',
        	OUTPUT_MATRIX_DELAY:	PREFIX + '/output/matrix/n/delay',
        	
        	/* stateful mixer messages */
        	STATEFUL_AUX_SEND_LEVEL:		PREFIX + '/stateful/aux/level/fromChannel/n',
        	STATEFUL_AUX_SEND_LEVELLABEL:	PREFIX + '/stateful/aux/levelLabel/fromChannel/n',
        	STATEFUL_AUX_SEND_CHANNELON:	PREFIX + '/stateful/aux/channelOn/fromChannel/n',
        	STATEFUL_AUX_OUTPUT_LEVEL:		PREFIX + '/stateful/aux/level',
        	STATEFUL_GEQ_BAND_LEVEL:		PREFIX + '/stateful/dsp/geq/band/n/left/level',
        	STATEFUL_GEQ_BAND_RESET:		PREFIX + '/stateful/dsp/geq/band/n/left/reset',
        	STATEFUL_GEQ_RESET:				PREFIX + '/stateful/dsp/geq/resetGeq',
        	STATEFUL_GEQ_FLEXEQ_HIDE:		PREFIX + '/stateful/dsp/geq/isFlexEq15/hidden',
        	
        	STATEFUL_INPUT_LEVEL:			PREFIX + '/stateful/input/misc/level',
        	STATEFUL_INPUT_CHANNELON:		PREFIX + '/stateful/input/misc/channelOn',
        	STATEFUL_INPUT_PAN:				PREFIX + '/stateful/input/misc/pan',
        	STATEFUL_INPUT_LABEL:			PREFIX + '/stateful/input/misc/channelLabel',
        	STATEFUL_INPUT_LEVELLABEL:		PREFIX + '/stateful/input/misc/levelLabel',
        	STATEFUL_INPUT_CHANNELONLABEL:	PREFIX + '/stateful/input/misc/channelOnLabel',
        	STATEFUL_INPUT_PANLABEL:		PREFIX + '/stateful/input/misc/panLabel',
        	
        	STATEFUL_INPUT_PEQ_ON:			PREFIX + '/stateful/input/peq/peqOn',
        	STATEFUL_INPUT_PEQ_ONLABEL:		PREFIX + '/stateful/input/peq/peqOnLabel',
        	STATEFUL_INPUT_PEQ_MODE:		PREFIX + '/stateful/input/peq/mode',
        	STATEFUL_INPUT_PEQ_MODELABEL:	PREFIX + '/stateful/input/peq/modeLabel',
        	STATEFUL_INPUT_PEQ_Q:			PREFIX + '/stateful/input/peq/band/n/q',
        	STATEFUL_INPUT_PEQ_F:			PREFIX + '/stateful/input/peq/band/n/f',
        	STATEFUL_INPUT_PEQ_G:			PREFIX + '/stateful/input/peq/band/n/g',
        	STATEFUL_INPUT_PEQ_QLABEL:		PREFIX + '/stateful/input/peq/band/n/qLabel',
        	STATEFUL_INPUT_PEQ_FLABEL:		PREFIX + '/stateful/input/peq/band/n/fLabel',
        	STATEFUL_INPUT_PEQ_GLABEL:		PREFIX + '/stateful/input/peq/band/n/gLabel',
        	
        	STATEFUL_INPUT_DYNA_ON:				PREFIX + '/stateful/input/dynamics/n/dynaOn',
        	STATEFUL_INPUT_DYNA_ONLABEL:		PREFIX + '/stateful/input/dynamics/n/dynaOnLabel',
        	STATEFUL_INPUT_DYNA_ATTACK:			PREFIX  + '/stateful/input/dynamics/n/attack',
        	STATEFUL_INPUT_DYNA_ATTACKLABEL:	PREFIX + '/stateful/input/dynamics/n/attackLabel',
        	STATEFUL_INPUT_DYNA_DECAY:			PREFIX + '/stateful/input/dynamics/n/decay',
        	STATEFUL_INPUT_DYNA_DECAYLABEL:		PREFIX + '/stateful/input/dynamics/n/decayLabel',
        	STATEFUL_INPUT_DYNA_GAIN:			PREFIX + '/stateful/input/dynamics/n/gain',
        	STATEFUL_INPUT_DYNA_GAINLABEL:		PREFIX + '/stateful/input/dynamics/n/gainLabel',
        	STATEFUL_INPUT_DYNA_HOLD:			PREFIX + '/stateful/input/dynamics/n/hold',
        	STATEFUL_INPUT_DYNA_HOLDLABEL:		PREFIX + '/stateful/input/dynamics/n/holdLabel',
        	STATEFUL_INPUT_DYNA_KNEE:			PREFIX + '/stateful/input/dynamics/n/knee',
        	STATEFUL_INPUT_DYNA_KNEELABEL:		PREFIX + '/stateful/input/dynamics/n/kneeLabel',
        	STATEFUL_INPUT_DYNA_RANGE:			PREFIX + '/stateful/input/dynamics/n/range',
        	STATEFUL_INPUT_DYNA_RANGELABEL:		PREFIX + '/stateful/input/dynamics/n/rangeLabel',
        	STATEFUL_INPUT_DYNA_RATIO:			PREFIX + '/stateful/input/dynamics/n/ratio',
        	STATEFUL_INPUT_DYNA_RATIOLABEL:		PREFIX + '/stateful/input/dynamics/n/ratioLabel',
        	STATEFUL_INPUT_DYNA_RELEASE:		PREFIX + '/stateful/input/dynamics/n/release',
        	STATEFUL_INPUT_DYNA_RELEASELABEL:	PREFIX + '/stateful/input/dynamics/n/releaseLabel',
        	STATEFUL_INPUT_DYNA_THRESHOLD:		PREFIX + '/stateful/input/dynamics/n/threshold',
        	STATEFUL_INPUT_DYNA_THRESHOLDLABEL:	PREFIX + '/stateful/input/dynamics/n/thresholdLabel',
        	
        	// OSC address helper functions
        	replaceChannelNumber: function(address, channel) {
        		return address.replace(/\/n\/|\/n$/g, '/' + channel + '/');
        	},
        	
        	replaceCounterNumber: function(address, count) {
        		return address.replace(/\/m\/|\/m$/g, '/' + count + '/');
        	}
        }
	};
});