userName=$1;
taskName=$2;
directory=sdcard/ExpData;
set `ls -t data/log/dmd_log | grep logtofile`;
echo $1 >> ${directory}/${userName}/${taskName}/capacity_fileName.txt;
cp data/log/dmd_log/$1 ${directory}/${userName}/${taskName};
mv ${directory}/${userName}/${taskName}/$1 ${directory}/${userName}/${taskName}/capacity_${taskName}.thplog;