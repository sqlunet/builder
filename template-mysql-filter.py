#!/usr/bin/python
# template -> mysql

from __future__ import print_function
import sys
import re
import string

c1=r'CREATE TABLE(.*);$'
c2=r'CREATE TABLE\1 DEFAULT CHARSET=utf8;'

t1=r'TEXT'
t2=r'MEDIUMTEXT'

cs1=r'Sense.create=(.*)casedwordid([^,]*) NULL'
cs2=r'Sense.create=\1casedwordid\2'

pk1=r'ALTER TABLE ([^\s]*) ADD CONSTRAINT ([^\s]*) PRIMARY KEY \(([^\)]*)\)'
pk2=r'ALTER TABLE \1 ADD CONSTRAINT \2 PRIMARY KEY (\3)'

_pk1=r'ALTER TABLE ([^\s]*) DROP CONSTRAINT pk_([^\s]*)( CASCADE)'
_pk2=r'ALTER TABLE \1 DROP PRIMARY KEY'

k1=r'CREATE INDEX IF NOT EXISTS ([^\s]*) ON ([^\s]*) \(([^\)]*)\)'
k2=r'ALTER TABLE \2 ADD INDEX \1 (\3)'

_k1=r'DROP INDEX IF EXISTS k_(%[^%]*%)_([^\s]*)'
_k2=r'ALTER TABLE \1 DROP INDEX k_\1_\2'

unq1=r'CREATE UNIQUE INDEX IF NOT EXISTS ([^\s]*) ON ([^\s]*) \(([^\)]*)\)'
unq2=r'ALTER TABLE \2 ADD CONSTRAINT \1 UNIQUE KEY (\3)'

_unq1=r'DROP INDEX IF EXISTS unq_(%[^%]*%)_([^\s]*)'
_unq2=r'ALTER TABLE \1 DROP INDEX unq_\1_\2'

fk1=r'ALTER TABLE ([^\s]*) ADD CONSTRAINT fk_([^\s]*) FOREIGN KEY \(([^\)]*)\) REFERENCES ([^\s]*) \(([^\)]*)\)'
fk2=r'ALTER TABLE \1 ADD CONSTRAINT fk_\2 FOREIGN KEY k_\2 (\3) REFERENCES \4 (\5)'

_fk1=r'ALTER TABLE ([^\s]*) DROP CONSTRAINT fk_([^\s]*)( CASCADE)'
_fk2=r'ALTER TABLE \1 DROP FOREIGN KEY fk_\2'

rw11=r'(?=\b)range(?=\b)'
rw21=r'`range`'
rw12=r'(?=\b)text(?=\b)'
rw22=r'`text`'
rw13=r'(?=\b)order(?=\b)'
rw23=r'`order`'

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

#print("DIFF", file=sys.stderr)

lines=sys.__stdin__.readlines()
for i in range(len(lines)):
	line0=lines[i].strip()
	line=line0
	dump=False

	line=replace(cs1,cs2,line)
	line=replace(c1,c2,line)
	line=replace(pk1,pk2,line)
	line=replace(_pk1,_pk2,line)
	line=replace(k1,k2,line)
	line=replace(_k1,_k2,line)
	line=replace(unq1,unq2,line)
	line=replace(_unq1,_unq2,line)
	line=replace(fk1,fk2,line)
	line=replace(_fk1,_fk2,line)
	line=replace(t1,t2,line)
		
	if re.search(r'^VnRestrs',line) or re.search(r'VnSemantics',line):
		line=replace(r'unq_',r'k_',line)
		line=replace(r'unq',r'index',line)
		line=replace(r'ADD CONSTRAINT (.*) UNIQUE KEY \(([^\s]*)\)',r'ADD INDEX \1 (\2)',line)
		if re.search(r'\.index',line):
			line=replace(r'\(semantics\)',r'(semantics (255))',line)
			line=replace(r'\(restrs\)',r'(restrs (255))',line)
			line=replace(r'\(restrs,',r'(restrs (255),',line)

	if re.search(r'^VnSyntax',line):
		line=replace(r'UNIQUE KEY \(([^\s]*)\)',r'UNIQUE KEY (\1 (255))',line)
		
	if re.search(r'^SUMOFormula',line):
		if re.search(r'\.index',line):
			line=replace(r'\(formula\)',r'(formula (128))',line)

	if re.search(r'^XWnWsd',line) or re.search(r'XWnParseLft',line):
		line=replace(r'index',r'index2',line)
		line=replace(r'unq_',r'k_',line)
		line=replace(r'unq',r'index1',line)
		line=replace(r'ADD CONSTRAINT (.*) UNIQUE KEY \(([^\)]*)\)',r'ADD INDEX \1 (\2)',line)
		if re.search(r'\.index',line):
			line=replace(r',wsd',r',wsd (128)',line)
			line=replace(r',text',r',text (128)',line)
			line=replace(r',parse',r',parse (100)',line)
			line=replace(r',lft',r',lft (100)',line)

	if re.search(r'^GLFlf',line) or re.search(r'ILFWNlf',line):
		if re.search(r'\.unq',line):
			line=replace(r',lf',r',lf (128)',line)
			line=replace(r',prettylf',r',prettylf (128)',line)

	if re.search(r'^PbArg',line):
		if re.search(r'\.index1',line):
			line=replace(r'\(arg\)',r'(arg (128))',line)

	line=replace(rw11,rw21,line)
	line=replace(rw12,rw22,line)
	line=replace(rw13,rw23,line)
	outputdata(line)
	if dump:
		print(">%s" % line0,file=sys.stderr)
 		print("<%s" % line,file=sys.stderr)

