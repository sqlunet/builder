#!/usr/bin/python

from __future__ import print_function
import sys
import re
import string

# CREATE

c_en1=r'([^\s]+) ENUM \(([^)]+)\)'
c_en2=r'\1 CHARACTER (1) CHECK( \1 IN (\2) )'

c_cs1=r'CHARACTER SET utf8\s?'
c_cs2=r''

c_bc1=r'COLLATE utf8_bin\s?'
c_bc2=r''

c_dcs1=r'DEFAULT\s+CHARSET=utf8mb3'
c_dcs2=r''

c_uk1=r'UNIQUE KEY\s'
c_uk2=r'UNIQUE'

# INDEX

k_k1=r'ALTER TABLE ([^\s]+) ADD KEY ([^\s]+) \(([^)]+)\);'
k_k2=r'CREATE INDEX \2 ON \1 (\3);'

k_uk1=r'ALTER TABLE ([^\s]+) ADD CONSTRAINT ([^\s]+) UNIQUE KEY \(([^)]+)\);'
k_uk2=r'CREATE UNIQUE INDEX \2 ON \1 (\3);'

k_l1=r'\([0-9]+\)'
k_l2=r''

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

	if re.match("^CREATE TABLE.*$", line):
		line=replace(c_en1,c_en2,line)
		line=replace(c_bc1,c_bc2,line)
		line=replace(c_cs1,c_cs2,line)
		line=replace(c_dcs1,c_dcs2,line)
		line=replace(c_uk1,c_uk2,line)
	elif re.match("^ALTER TABLE.*$", line):
		line=replace(k_l1,k_l2,line)
		line=replace(k_k1,k_k2,line)
		line=replace(k_uk1,k_uk2,line)

	print(line)
	if dump and line0 != line:
		print(">%s" % line0,file=sys.stderr)
 		print("<%s" % line,file=sys.stderr)
