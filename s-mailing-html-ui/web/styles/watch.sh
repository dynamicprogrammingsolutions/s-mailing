#!/bin/sh
while :
do
   inotifywait -e close_write *.less;
   lessc custom.less custom.css
done