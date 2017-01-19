import re
import os
import sys

phoneType = 'longisland'
dataType_instruction = 'instruction'
dataType_tag = 'tag'

if __name__ == '__main__':
	argv = sys.argv
	dir = argv[1]
	task = os.path.basename(dir)
	task_filename = os.path.join(dir, 'info_' + task + '.txt')
	task_file = open(task_filename)
	lines = task_file.readlines()
	message = '0,' + phoneType + ',' + dataType_instruction + ',' + lines[1].replace("\n", "")
	print(message)
	message = '0,' + phoneType + ',' + dataType_tag + ',' + lines[2].replace("\n", "")
	print(message)
	task_file.close()
