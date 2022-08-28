#!/usr/bin/python3

import sys
import mysql.connector

database = sys.argv[1]
conn = mysql.connector.connect(
    host="localhost",
    user="sqlbuilder",
    password="sqlbuilder",
    database=database
)

tablecursor = conn.cursor(dictionary=True)
tablecursor.execute("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = '%s' AND TABLE_TYPE = 'BASE TABLE' ORDER BY TABLE_NAME;" % database)
tableresult = tablecursor.fetchall()
for tablerow in tableresult:
	table = tablerow['TABLE_NAME']
	print(table)

	columncursor = conn.cursor(dictionary=True)
	columncursor.execute("SELECT COLUMN_NAME,ORDINAL_POSITION,DATA_TYPE,CHARACTER_MAXIMUM_LENGTH,COLUMN_DEFAULT,IS_NULLABLE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '%s' AND TABLE_NAME = '%s';" % (database, table))
	columnresult = columncursor.fetchall()
	for columnrow in columnresult:
		column = columnrow['COLUMN_NAME']
		ordinal_position = columnrow['ORDINAL_POSITION']
		column_default = columnrow['COLUMN_DEFAULT']
		is_nullable = columnrow['IS_NULLABLE']
		data_type = columnrow['DATA_TYPE']
		character_maximum_length = columnrow['CHARACTER_MAXIMUM_LENGTH']
		print('\t%s %s' % (column, data_type))
