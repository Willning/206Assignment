!#bin/bash

			if [ -e "./soundAssets/$1.wav" ]

			then 

				#if a recording of the same name exists, delete it and overwrite

				rm $name.wav 

			fi 

arecord -q -d 3 -f cd -t wav  $1.wav	

mv $1.wav ./soundAssets/$1.wav