if not exist "%1\..\parse" (
	mkdir %1\..\parse
)
python taskParser.py %1 > taskParse.txt;
python capacityParser.py %1 > capacityParse.txt;
python touchParser.py %1 > touchParse.txt;
python sensorParser.py %1 > sensorParse.txt;
type taskParse.txt capacityParse.txt touchParse.txt sensorParse.txt > %1\..\parse\parse_%~n1.txt;
del taskParse.txt capacityParse.txt touchParse.txt sensorParse.txt;
