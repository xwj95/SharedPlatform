userName=$1
directory=sdcard/ExpData
adb pull ${directory}/${userName}
if [ ! -d "data" ]; then
	mkdir data
fi
if [ ! -d "data/${userName}" ]; then
	mkdir data/${userName}
fi
cp -rf ${userName}/ data/${userName}
rm -rf ${userName}/
