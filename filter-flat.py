#!/usr/bin/python

from __future__ import print_function
import sys
import re

lines=sys.__stdin__.readlines()
for line in lines:
	if re.match("^\s*--.*$", line):
		continue
	line = re.sub(r"\s+", " ", line.rstrip())
	print(line, end='')
print()