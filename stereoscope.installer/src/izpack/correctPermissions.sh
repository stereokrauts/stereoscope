#!/bin/sh

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

pushd "${SCRIPT_DIR}"

chmod -R 755 jre/*
if [[ -d jre/Contents/MacOS ]]; then
	chmod 755 stereoscope.app/Contents/MacOS/stereoscope
	cd jre/Contents/MacOS
	rm libjli.dylib
	ln -s ../Home/jre/lib/jli/libjli.dylib libjli.dylib
fi

popd