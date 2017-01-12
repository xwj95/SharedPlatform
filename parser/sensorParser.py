import re
import os
import sys

phoneType = 'victoria'
dataTypes = ['ACCELEROMETER', 'GYROSCOPE', 'MAGNETIC_FIELD', 'GRAVITY', 'PROXIMITY', 'LIGHT', 'ROTATION_VECTOR', 'ACCELERATION']
dataValues = [3, 3, 3, 3, 1, 1, 5, 3]

if __name__ == '__main__':
	argv = sys.argv
	dir = argv[1]
	task = os.path.basename(dir)
	sensor_filename = os.path.join(dir, 'sensor_' + task + '.txt')
	sensor_file = open(sensor_filename)
	for line in sensor_file:
		values = map(str, line.split())
		if (len(values) < 2):
			continue
		type = int(values[0])
		time = int(values[1])
		accuracy = int(values[2])
		if (time < 0):
			continue
		message = str(time) + ',' + phoneType + ',' + dataTypes[type] + ','
		message += ';'.join(values[3:dataValues[type] + 3])
		print(message)
	sensor_file.close()
