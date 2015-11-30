/**
 * all statics data should be defined here
 * in a hirarchical structure
 */
var statics = window.statics || {};

// layout dimensions
statics.layout = {
	IPAD_LANDSCAPE:   { x: 1024, y: 768 },
	IPAD_PORTRAIT:    { x: 768, y: 1024 },
	IPHONE_LANDSCAPE: { x: 480, y: 320 },
	IPHONE_PORTRAIT:  { x: 320, y: 480 }
};

// media queries to determine the screenwidth
statics.mediaQueries = [
                        'small',	//width: 0px - 640px
                        'medium',	//width: 641px - 1024px
                        'large',	//width: 1025px - 1440px
                        'xlarge',	//width: 1441px - 1920px
                        'xxlarge'	//width: 1921px
                        ];

// color palette
statics.color = {
	RED:	'#ff281c',
	GREEN:	'#75cc26',
	BLUE:	'#00c4a8',
	YELLOW:	'#ffed00',
	PURPLE:	'#aa7faa',
	GRAY:	'#b2b2b2',
	ORANGE:	'#f9a01c',
	BROWN:	'#826647',
	PINK:	'#ff05f2',
	WHITE:  '#ffffff'
};

statics.color.ui = {
	layoutBg:	'#3c3c3c'	
},

statics.color.ui.rgb = function(color) {
	return statics.convertHexToRgb(statics.color.ui.hex[color]);
},

// sections in the main menu
statics.menu = [
               'Mixer',
               'Layout_Manager',
               //'Pong',
               'About',
               //'DarkTheme',
               //'BrightTheme'
               ];

// OSC prefix
PREFIX = '/stereoscope';

// all OSC addresses in one object
statics.osc = {
	
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

};
	
// this can be used by all objects that do server requests
statics.createRequestObject = function(oscStr, value) {
	return {'type': 'string', 'oscStr': oscStr, 'val': value};
};

statics.convertHexToRgb = function(hex) {
	var result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
	return result ? {
		r: parseInt(result[1], 16),
	    g: parseInt(result[2], 16),
	    b: parseInt(result[3], 16)
	} : null;
};

statics.convertRgbToHex = function(rgb) {
	return "#" + ((1 << 24) + (rgb.r << 16) + (rgb.g << 8) + rgb.b).toString(16).slice(1);
};

statics.getMediaQuery = function() {
	var mediaQuery = "large";
	_.forEach(statics.mediaQueries, function(mq) {
		if (matchMedia(Foundation.media_queries[mq]).matches) {
			mediaQuery = mq;
		}
	}, this);
	return mediaQuery;
};
	