#!/usr/bin/python2
# mysql -> mysql

from __future__ import print_function
import sys
import re
import string

dummy1=r'test'
dummy2=r'test'

def replace(s1,s2,line):
	line2=re.sub(s1,s2,line)
	if re.search(s1,line):
		#print("matches [%s] %s" % (s1,line),file=sys.stderr)
		pass
	if line != line2:
		#print('\t>%s' % line, file=sys.stderr)
		#print('\t<%s' % line2, file=sys.stderr)
		#exit()
		pass
	return line2

dump=False
lines=sys.__stdin__.readlines()
for line in lines:
	line0=line
	line=line.strip()

	line=replace(test1,test2,line)

	print(line)
	if dump and line0 != line:
		print(">%s" % line0,file=sys.stderr)
 		print("<%s" % line,file=sys.stderr)
