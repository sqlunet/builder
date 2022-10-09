#!/usr/bin/python3

import sys
import re

# CREATE

c_en1=r'([^\s]+)\s*ENUM\s*\(([^)]+)\)'
c_en2=r'\1 CHARACTER (1) CHECK( \1 IN (\2) )'

c_cs1=r'CHARACTER\s+SET\s+utf8\s*'
c_cs2=r''

c_bc1=r'COLLATE\s+utf8_bin\s*'
c_bc2=r''

c_cc1=r'COLLATE\s+utf8mb4_0900_as_cs\s*'
c_cc2=r''

c_dcs1=r'DEFAULT\s+CHARSET=utf8mb4'
c_dcs2=r''

c_uk1=r'UNIQUE\s+KEY\s'
c_uk2=r'UNIQUE'

# INDEX

k_k1=r'ALTER\s+TABLE\s+([^\s]+)\s+ADD\s+KEY\s+([^\s]+) \(([^)]+)\);'
k_k2=r'CREATE INDEX \2 ON \1 (\3);'

k_uk1=r'ALTER\s+TABLE\s+([^\s]+)\s+ADD\s+CONSTRAINT\s+([^\s]+)\s+UNIQUE KEY\s+\(([^)]+)\);'
k_uk2=r'CREATE UNIQUE INDEX \2 ON \1 (\3);'

k_pk1=r'ALTER\s+TABLE\s+([^\s]+)\s+ADD\s+CONSTRAINT\s+([^\s]+)\s+PRIMARY KEY\s+\(([^)]+)\);'
k_pk2=r'CREATE UNIQUE INDEX \2 ON \1 (\3);'

k_l1=r'\([0-9]+\)'
k_l2=r''

# VIEWS
v1=r'CREATE\s+OR\s+REPLACE\s+VIEW\s+([^\s]+)'
v2=r'DROP VIEW IF EXISTS \1; CREATE VIEW IF NOT EXISTS \1'

gco1=r"GROUP_CONCAT\s*\((.+)\s+ORDER\s+BY\s+[^\s]+\s+(.*)\s*\)"
gco2=r"GROUP_CONCAT(\1 \2)"

gcs1=r"GROUP_CONCAT\s*\((.+)\s+SEPARATOR\s+'[^\s]+'\s*([^)]*)\s*\)"
gcs2=r"GROUP_CONCAT(\1\2)"

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
creating=False
altering=False
lines=sys.__stdin__.readlines()
for line in lines:
	line0=line
	line=line.strip()

	if re.match("^.*CREATE TABLE.*$", line):
		creating=True
	elif re.match("^.*ALTER TABLE.*$", line):
		altering=True
	elif re.match("^.*CREATE.*VIEW.*$", line):
		view=True

	if creating:
		line=replace(c_en1,c_en2,line)
		line=replace(c_bc1,c_bc2,line)
		line=replace(c_cc1,c_cc2,line)
		line=replace(c_cs1,c_cs2,line)
		line=replace(c_dcs1,c_dcs2,line)
		line=replace(c_uk1,c_uk2,line)
	elif altering:
		line=replace(k_l1,k_l2,line)
		line=replace(k_k1,k_k2,line)
		line=replace(k_uk1,k_uk2,line)
		line=replace(k_pk1,k_pk2,line)
	elif view:
		line=replace(v1,v2,line)

	line=replace(gco1,gco2,line)
	line=replace(gcs1,gcs2,line)

	print(line)
	if dump and line0 != line:
		print(">%s" % line0,file=sys.stderr)
		print("<%s" % line,file=sys.stderr)
