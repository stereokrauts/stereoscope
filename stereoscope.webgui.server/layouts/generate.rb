#!/usr/bin/ruby

def output_fader(name, oscMessage, coords_x, coords_y, width, height)
	<<-EOF
			{
				"type": "basicFader",
				"name": "#{name}",
				"oscMessage": "#{oscMessage}",
				"dataType": "float",
				"initValue": 0.0,
				"coords": {"x":#{coords_x},"y":#{coords_y}},
				"width": #{width},
				"height": #{height},
				"color": "red",
				"visibility": true,
				"orient": "horizontal",
				"mode": "normal"
			},
EOF
end

def output_button(name, oscMessage, coords_x, coords_y, width, height)
	<<-EOF
			{
				"type": "basicToggleButton",
				"name": "#{name}",
				"oscMessage": "#{oscMessage}",
				"dataType": "boolean",
				"initValue": false,
				"coords": {"x":#{coords_x},"y":#{coords_y}},
				"width": #{width},
				"height": #{height},
				"color": "red",
				"visibility": true,
				"orient": "horizontal",
			},
EOF
end

def output_label(name, value, coords_x, coords_y, width, height)
	<<-EOF
			{
				"type": "staticLabel",
				"name": "#{name}",
				"oscMessage": "/null",
				"dataType": "string",
				"initValue": "#{value}",
				"coords": {"x":#{coords_x},"y":#{coords_y}},
				"width": #{width},
				"height": #{height},
				"color": "red",
				"visibility": true,
				"orient": "horizontal",
				"background": false,
				"edge": false,
				"size": 12
			},
EOF
end


def output_input(i)
	coords_x = 50*(i-1) + 20
	
	content = output_fader("basicFader input#{i}", "/stereoscope/input/#{i}/level", coords_x, 50, 30, 200)
	content += output_button("basicToggleButton input#{i}", "/stereoscope/input/#{i}/channelOn", coords_x, 260, 30, 30)
	content += output_label("label input#{i}", "Input #{i}", coords_x, 300, 30, 30)

	return content
end

def output_inputs_i(outputs)
	result = ''
	outputs.times { |i|
		result += output_input(i+1)
	}
	return result
end

def output_inputs(outputs)
	<<-EOF
		"page0": {
			"name": "Inputs",
			"controls": [
				#{output_inputs_i(outputs)}
			]}
EOF
end

File.open('16ch.stsc.json', 'w') { |file|
	contents =<<-EOF
{
	"setup": {
		"version": 1.0,
		"mode": "IPAD_LANDSCAPE"
	},
	"mixer": {
		"mixerModel": "yamaha-01v96",
		"channelCount": 40,
		"auxCount": 8,
		"busCount": 8,
		"matrixCount": 0,
		"outputCount": 2,
		"geqCount": 0
	},
	"pages": {
	#{output_inputs(16)}
	}
}
EOF

	file << contents
}


