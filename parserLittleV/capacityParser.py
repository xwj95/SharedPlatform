import re
import os
import sys
import shutil

pattern = 'time=\[(\d+)\]\s+noise=\[(\d+)\]\s+notify=\[(\d+)\]\s+scan_mode=\[(\d+)\]\s+frame_no=\[(\d+)\]\s+ctrlflag=\[(\d+)\]\s+feature=\[(\d+)\]'
phoneType = 'victoria'
dataType = 'capacity'

def getStartTimestamp(dir, task):
	timestamp_filename = os.path.join(dir, 'time_' + task + '.txt')
	timestamp_file = open(timestamp_filename)
	lines = timestamp_file.readlines()
	return int(lines[1])

if __name__ == '__main__':
	argv = sys.argv
	dir = argv[1]
	task = os.path.basename(dir)
	startTimestamp = getStartTimestamp(dir, task)
	os.system('python thplogParser.py ' + dir)
	capacity_dir = 'capacity_' + task
	capacity_filename = os.path.join(capacity_dir, 'file.dif')
	capacity_file = open(capacity_filename)
	message = ''
	for line in capacity_file:
		k = re.match(pattern, line)
		if (k):
			if (len(message) > 0) and (time > 0):
				message += (';'.join(value))
				print(message)
			time = int(k.groups()[0]) - startTimestamp
			noise = int(k.groups()[1])
			notify = k.groups()[2]
			scan_mode = int(k.groups()[3])
			frame_no = int(k.groups()[4])
			ctrlflag = k.groups()[5]
			feature = k.groups()[6]
			message = str(time) + ',' + phoneType + ',' + dataType + ','
			value = list()
		else:
			values = list(map(str, line.split()))
			value.append(';'.join(values))
	if (len(message) > 0) and (time > 0):
		message += (';'.join(value))
		print(message)
	capacity_file.close()
	shutil.rmtree(capacity_dir)
