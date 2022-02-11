#!/usr/bin/python
# template -> mysql

from __future__ import print_function
import sys
import re
import string

k1=r'CREATE INDEX ([^\s]*) ON ([^\s]*) \(([^\)]*)\)'
k2=r'ALTER TABLE \2 ADD KEY \1 (\3)'

unq1=r'CREATE UNIQUE INDEX ([^\s]*) ON ([^\s]*) \(([^\)]*)\)'
unq2=r'ALTER TABLE \2 ADD CONSTRAINT \1 UNIQUE KEY (\3)'

def outputdata(data):
	print("%s" % (data))
	return

def replace(s1,s2,line):
	line2=re.sub(s1,s2,line)
	if re.search(s1,line):
		#print("matches [%s] %s" % (s1,line),file=sys.stderr)
		pass
	if line != line2:
		#print(line, file=sys.stderr)
		#print(line2, file=sys.stderr)
		#exit()
		pass
	return line2

dump=False
lines=sys.__stdin__.readlines()
for i in range(len(lines)):
	line0=lines[i].strip()
	line=line0

	line=replace(k1,k2,line)
	line=replace(unq1,unq2,line)

	outputdata(line)
	if dump and line0 != line:
		print(">%s" % line0,file=sys.stderr)
 		print("<%s" % line,file=sys.stderr)
