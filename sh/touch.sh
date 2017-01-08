userName=$1;
taskName=$2;
directory=sdcard/ExpData
if [ ! -d "${directory}" ]; then
	mkdir ${directory}
fi
if [ ! -d "${directory}/${userName}" ]; then
	mkdir ${directory}/${userName}
fi
if [ ! -d "${directory}/${userName}/${taskName}" ]; then
	mkdir ${directory}/${userName}/${taskName}
fi
getevent -lt >${directory}/${userName}/${taskName}/touch_${taskName}.txt