#!/usr/bin/python

from __future__ import print_function
import sys
import re
import string

u1=r'\sUNSIGNED'
u2=r''
s1=r'\s(?=\b)SIGNED(?=\b)'
s2=r''
i10=r'INT \(1\)'
i20=r'INTEGER'
i11=r'SMALLINT'
i21=r'INTEGER'
mt1=r'MEDIUMTEXT'
mt2=r'TEXT'
d1=r'DATETIME'
d2=r'INTEGER'
en10=r'position ENUM \([^\)]*\)'
en20=r'position CHARACTER (2)'
en1=r'ENUM \([^\)]*\)'
en2=r'CHARACTER (1)'
ai1=r'NOT NULL AUTO_INCREMENT'
ai2=r'PRIMARY KEY AUTOINCREMENT'

cv1=r'CREATE OR REPLACE VIEW ([^\s]*) AS'
cv2=r'DROP VIEW IF EXISTS \1; CREATE VIEW \1 AS'
cv10=r'base%\$(X|Y|Z)\$~\$(X|Y|Z)\$'
cv20=r'base%$\1$$\2$'

pk1=r'^([^=]*).create=(.*),PRIMARY KEY(.*)$'
pk2=r'\1.create=\2,CONSTRAINT pk_%\1.table% PRIMARY KEY\3'
pk10=r',PRIMARY KEY .*\);'
pk20=r' );'

cmp1=r'STRCMP\((.*) COLLATE utf8_bin,\?\)=0'
cmp2=r'\1 = ?'
cicmp1=r'LOWER\(([^\)]+)\) = LOWER\(\?\)'
cicmp2=r'\1 = ? COLLATE NOCASE'

bc1=r'\sBINARY COLLATE utf8_bin'
bc2=r''

gc1=r'GROUP_CONCAT\(DISTINCT ([^\s]*) (ORDER BY [^\s]* SEPARATOR )([^\s\)]*)\)'
gc2=r'GROUP_CONCAT(DISTINCT \1,\3)'

cfk1=r'([^\.]*\.fk[0-9]*)=.*$'
cfk2=r'\1='
_cfk1=r'([^\.]*\.no-fk[0-9]*)=.*$'
_cfk2=r'\1='
cpk1=r'([^\.]*\.pk[0-9]*)=.*$'
cpk2=r'\1='
_cpk1=r'([^\.]*\.no-pk[0-9]*)=.*$'
_cpk2=r'\1='

def outputdata(data):
	print("%s" % (data))
	return

def replace(s1,s2,line):
	line2=re.sub(s1,s2,line)
	if re.search(s1,line):
		print("matches [%s] %s" % (s1,line),file=sys.stderr)
		if line != line2:
			print('\t>%s' % line, file=sys.stderr)
			print('\t<%s' % line2, file=sys.stderr)
			#exit()
			pass
	return line2

print("DIFF", file=sys.stderr)

lines=sys.__stdin__.readlines()
for i in range(len(lines)):
	line=lines[i].strip()
	
	line=replace(cfk1,cfk2, line)
	line=replace(_cfk1,_cfk2, line)
	line=replace(cpk1,cpk2, line)
	line=replace(_cpk1,_cpk2, line)
		
	if re.match("^.*=CREATE TABLE.*$", line):
		line=replace(ai1,ai2,line)
		if re.match("^.*AUTOINCREMENT.*$", line):
			line=replace(pk10,pk20,line)

		line=replace(u1,u2,line)
		line=replace(s1,s2,line)
		line=replace(i10,i20,line)
		line=replace(i11,i21,line)
		line=replace(pk1,pk2,line)
		line=replace(en10,en20,line)
		line=replace(en1,en2,line)
		line=replace(d1,d2,line)
		line=replace(bc1,bc2,line)

	if re.match("^.*=CREATE OR REPLACE VIEW.*$", line):
		line=replace(cv1,cv2,line)
		line=replace(cv10,cv20,line)
		
	line=replace(cmp1,cmp2,line)
	line=replace(cicmp1,cicmp2,line)
	line=replace(gc1,gc2,line)
	
	outputdata(line)
