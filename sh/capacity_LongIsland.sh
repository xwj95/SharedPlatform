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
if [ ! -d "${directory}/${userName}/${taskName}/capacity_${taskName}" ]; then
	mkdir ${directory}/${userName}/${taskName}/capacity_${taskName}
fi
echo $$ > ${directory}/${userName}/${taskName}/capacity_${taskName}/toBeKilled.pid
dmesg -c

cat ~/proc/t*/t*
i=0
j=0
while :
do
let i=i+1
cat ~/sys/touchscreen/touch_chip_info
if [ $i -eq 64 ];
then
dmesg -c | grep "ts_thread" > ${directory}/${userName}/${taskName}/capacity_${taskName}/capacityData$j.txt
i=0
let j=j+1
fi
done 
