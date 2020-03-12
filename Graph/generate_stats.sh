#! /bin/bash

if [ $# -eq 0 ]
then
	echo "No parameters passed"

else
	file=$1

	if [ -f "$file" ]
	then
		cat $file | cut -c 26-37 | nl > memory_stats.log &&
		java -jar StatsToGB.jar memory_stats.log &&
		rm memory_stats.log

	else
		echo "No such file exists"
	fi
fi
