import re
import os
import sys
import copy
import collections

pattern = '^\[\s+(\d+\.\d+)\]\s+/dev/input/([a-z0-9/]*).*\s+([A-Z_]+)\s+([0-9a-f]+|UP|DOWN)\s*$'
phoneType = 'victoria'
dataType = 'touch'

def getStartTimestamp(dir, task):
	timestamp_filename = dir + '/time_' + task + '.txt'
	timestamp_file = open(timestamp_filename)
	lines = timestamp_file.readlines()
	return long(lines[2])

def getInitTouchevent():
	touch_event = {
		'time': -1,
		'type': 'move',
		'id': -1,
		'x': 0,
		'y': 0,
		'pressure': 0,
		'touch_major': 0,
		'touch_minor': 0,
		'orientation': 0,
		'tool_type': 0
	}
	return touch_event

def getInitTouchevents():
	return collections.OrderedDict()

def printTouchevent(touch_event):
	message = str(touch_event['time']) + ',' + phoneType + ',' + dataType + ','
	message += touch_event['type'] + ';' + str(touch_event['id']) + ';' + str(touch_event['x']) + ';' + str(touch_event['y'])
	print message

def printTouchevents(touch_events):
	for id, touch_event in touch_events.items():
		printTouchevent(touch_event)

if __name__ == '__main__':
	argv = sys.argv
	dir = argv[1]
	task = os.path.basename(dir)
	startTimestampNs = getStartTimestamp(dir, task)
	touch_filename = dir + '/touch_' + task + '.txt'
	touch_file = open(touch_filename)
	touch_event = getInitTouchevent()
	touch_events = getInitTouchevents()
	last_touch_events = getInitTouchevents()
	last_up_time = -1
	for line in touch_file:
		k = re.match(pattern, line)
		if (k):
			time = long((long(float(k.groups()[0]) * 1000000) - startTimestampNs / 1000) / 1000)
			device = k.groups()[1]
			event = k.groups()[2]
			value = k.groups()[3]
			if (value != 'UP') and (value != 'DOWN'):
				value = int(value, 16)
			if (event == 'SYN_REPORT'):
				if (device == 'event4'):
					if (last_up_time != time):
						last_up_time = -1
					printTouchevents(last_touch_events)
					last_touch_events = copy.deepcopy(touch_events)
					touch_events = getInitTouchevents()
					touch_event = getInitTouchevent()
				elif (device == 'event3'):
					pass
				else:
					pass
			elif (event == 'SYN_MT_REPORT'):
				touch_event['time'] = time
				touch_events[touch_event['id']] = copy.deepcopy(touch_event)
				touch_event = getInitTouchevent()
			elif (event == 'BTN_TOUCH'):
				if (value == 'UP'):
					last_up_time = time
					touch_events = copy.deepcopy(last_touch_events)
					for id, touch_event in last_touch_events.items():
						touch_events[id]['time'] = time
						touch_events[id]['type'] = 'up'
				elif (value == 'DOWN'):
					for id, touch_event in touch_events.items():
						if (id not in last_touch_events) or (last_touch_events[id]['type'] == 'up'):
							# print 'down'
							touch_events[id]['type'] = 'down'
				else:
					assert(False)
			elif (event == 'ABS_MT_TRACKING_ID'):
				if last_up_time > 0:
					if (value in last_touch_events) and (len(last_touch_events) > 1):
						last_touch_events[value]['type'] = 'move'
				touch_event['id'] = value
			elif (event == 'ABS_MT_POSITION_X'):
				touch_event['x'] = value
			elif (event == 'ABS_MT_POSITION_Y'):
				touch_event['y'] = value
			elif (event == 'ABS_MT_TOUCH_MAJOR'):
				touch_event['touch_major'] = value
			elif (event == 'ABS_MT_TOUCH_MINOR'):
				touch_event['touch_minor'] = value
			elif (event == 'ABS_MT_PRESSURE'):
				touch_event['pressure'] = value
			elif (event == 'ABS_MT_ORIENTATION'):
				touch_event['orientation'] = value
			elif (event == 'ABS_MT_TOOL_TYPE'):
				touch_event['tool_type'] = value
			elif (event == 'KEY_POWER'):
				if (value == 'UP'):
					pass
				elif (value == 'DOWN'):
					pass
				else:
					assert(False)
			elif (event == 'KEY_EXIT'):
				if (value == 'UP'):
					pass
				elif (value == 'DOWN'):
					pass
				else:
					assert(False)
			elif (event == 'KEY_ENTER'):
				if (value == 'UP'):
					pass
				elif (value == 'DOWN'):
					pass
				else:
					assert(False)
			elif (event == 'KEY_VOLUMEDOWN'):
				if (value == 'UP'):
					pass
				elif (value == 'DOWN'):
					pass
				else:
					assert(False)
			elif (event == 'KEY_VOLUMEUP'):
				if (value == 'UP'):
					pass
				elif (value == 'DOWN'):
					pass
				else:
					assert(False)
			elif (event == 'KEY_MUTE'):
				if (value == 'UP'):
					pass
				elif (value == 'DOWN'):
					pass
				else:
					assert(False)
			else:
				assert(False)
