#!bin/bash

			if [ -e "../soundAssets/$1.wav" ]

			then 

				#if a recording of the same name exists, delete it and overwrite

				rm ./soundAssets/$1.wav 

			fi 

arecord -q -d 3 -f cd -t wav  ./soundAssets/$1.wav	

