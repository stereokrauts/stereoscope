# Stereoscope #

[![Build Status](https://travis-ci.org/stereokrauts/stereoscope.svg?branch=master)](https://travis-ci.org/stereokrauts/stereoscope)

## What it is ##

Stereoscope is a versatile software controller for digital audio mixers. The following mixers are currently supported:

* Yamaha 01V96(i)
* Yamaha 02R96 (V2)
* Yamaha DM1000
* Yamaha DM2000
* Yamaha LS9
* Yamaha M7CL
* Yamaha PM5D
* Roland M380
* Roland M400
* Behringer DDX3216

Technically it's a server/client solution that can be controlled with various clients with OSC (Open Sound Control) support. It also comes with a webclient based on websockets/json.

## Building ##

Stereoscope uses a maven build. All you need is maven V3 and Java7 JDK or better.

A full build can be done with:

        mvn clean package
