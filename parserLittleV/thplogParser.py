import os
import sys
	
def getThpFileList(dir):
	files = os.listdir(dir)
	files = [file for file in files if file.endswith(".thplog")]
	return files

def makeSubDir(filename):
	name, sep, surfix = filename.rpartition(".")
	try:
		os.mkdir(name)
	except:
		pass
		# print "mkdir error: ", name
	return name + str(os.sep);

def getName(name):
	if "." in name:
		dir, sep, surfix = name.rpartition(".")

def readFile(file):
	with open(file, "r") as f:
		lines = f.readlines()
	lines = [line for line in lines if line.strip() != ""]
	return lines

def writeFile(dir, filename, lines):
	subdir = makeSubDir(filename)
	files = {}
	for line in lines:
		if line.startswith("$$"):
			suffix, sep, content = line[2:].partition("$")
			if suffix in files:
				file = files[suffix]
			else:
				newfilename = subdir + "file." + suffix
				file = open(newfilename, "w")
				files[suffix] = file
		else:
			content = line
		if content.strip() != "":
			file.write(content)
	
	for suffix, file in files.items():
		file.close()

def handlFile(dir, file):
	lines = readFile(dir + str(os.sep) + file)
	writeFile(dir, file, lines)

if __name__ == '__main__':
	argv = sys.argv
	dir = argv[1]
	files = getThpFileList(dir)
	# print files
	for file in files:
		handlFile(dir, file)
	# print "work done"