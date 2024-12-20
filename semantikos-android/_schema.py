#!/usr/bin/python3

import sqlite3
import sys

sql_tables_schema= """SELECT name, type FROM sqlite_schema WHERE type='table';"""
sql_indexes_schema="""SELECT name, type FROM sqlite_schema WHERE type='index';"""

sql_columns="""PRAGMA table_info('%s')"""

def collectNames(rows):
	return [name for name,type in rows]

def collect(rows):
	return sorted(list(zip(*rows))[1])

def collectColumns(rows):
	return [(name,type) for cid,name,type,notnull,dflt_value,pk in rows]

def query(sql, consume):
	cursor = connection.cursor()
	cursor.execute(sql)
	rows = cursor.fetchall()
	r = consume(rows)
	cursor.close()
	return r

db=sys.argv[1]
connection = sqlite3.connect(db)

print("sql_tables_schema")
tables = sorted(query(sql_tables_schema, collectNames))
print(tables)

print("sql_indexes_schema")
indexes = sorted(query(sql_indexes_schema, collectNames))
print(indexes)

print("sql_table_columns")
for t in tables:
	columns = query(sql_columns % t, collectColumns)
	columns = ['%s %s' % (c[0],c[1]) for c in columns]
	print('%s\n\t%s' % (t, columns))


