#!/usr/bin/python3

import sys
import re

lines=sys.__stdin__.readlines()
for line in lines:
	line = line.strip()
	if re.match("^\s*--.*$", line):
		continue
	line = re.sub(r"\s+", " ", line)
	if re.match("^\s*$", line):
		continue
	print(line, end='\n')
