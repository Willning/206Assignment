#!/bin/bash

echo hello

ffmpeg -nostats -f lavfi -i color=color=red -vf drawtext="fontfile ./TibetanMachineUni.tff:text='$1':fontcolor=black:fontsize=56: x=(w-text_w)/2: y=(h-text_h)/2" -t 3  -s 320x240 ./videoAssets/$1.mp4


ffmpeg  -i ./videoAssets/$1.mp4 	-i ./soundAssets/$1.wav -c:v copy -c:a aac -strict experimental -map 0:v:0 -map 1:a:0 ./creations/$1.mp4 

