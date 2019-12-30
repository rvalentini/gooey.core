# gooey.core

Gooey effect animation showcase using randomly duplicating blue bubbles.  

![](/resources/public/bubble-spread.gif)

## Overview

This project is intended as some kind of "playground" project in order to learn and understand how SVG-filters work. 

The animation starts with a single bubble in view and redoubles the number of bubbles every few seconds in random fashion. 
This makes the animation look like a rapidly spreading organism or cell-structure and gives it a "virus-like" appearance. 

WARNING: Due to the rapid redoubling of `<div>` elements, the animation my crash or freeze your browser if you keep it running for too long.

## Technical

The animation is written entirely in ClojureScript using figwheel:main.

The gooey effect is achieved by using SVG-filters only. The `SVG` tag definitions can be found in a separate namespace: `svg-filter.cljs`
and the animation definitions can be found inside `style.css`

## Development

To get an interactive development environment, make sure you have leining installed (https://leiningen.org/) and run:

    lein fig:build

This will start the animation and auto compile and send all changes to the browser without the
need to reload. After the compilation process is complete, you will
get a Browser Connected REPL. An easy way to try it is:

    (js/alert "Am I connected?")

and you should see an alert in the browser window.

To clean all compiled files:

	lein clean

## License

Copyright © 2019 rival

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
