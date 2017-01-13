if [ ! -d "$1/../parse" ]; then
	mkdir $1/../parse
fi
python taskParser.py $1 > taskParse.txt;
python capacityParser.py $1 > capacityParse.txt;
python touchParser.py $1 > touchParse.txt;
python sensorParser.py $1 > sensorParse.txt;
cat taskParse.txt capacityParse.txt touchParse.txt sensorParse.txt > $1/../parse/parse_${1##*/}.txt;
rm taskParse.txt capacityParse.txt touchParse.txt sensorParse.txt;
