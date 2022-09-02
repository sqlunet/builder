#!/usr/bin/python3

import sys
import mysql.connector

class colors:
	
	pref = "\033["
 
	black=f'{pref}30m'
	red='31m'
	green='32m'
	blue='34m'
	yellow='33m'
	magenta='35m'
	cyan='36m'
	white='37m'

	bg_black='40m'
	bg_red='41m'
	bg_green='42m'
	bg_yellow='43m'
	bg_blue='44m'
	bg_magenta='45m'
	bg_cyan='46m'
	bg_white='47m'

	light_black='90m'
	light_red='91m'
	light_green='92m'
	light_yellow='93m'
	light_blue='94m'
	light_magenta='95m'
	light_cyan='96m'
	light_white='97m'

	light_bg_black='100m'
	light_bg_red='101m'
	light_bg_green='102m'
	light_bg_yellow='103m'
	light_bg_blue='104m'
	light_bg_magenta='105m'
	light_bg_cyan='106m'
	light_bg_white='107m'

	bold='1m'
	stop_bold='21m'
	underline='4m'
	stop_underline='24m'
	blink='5m'

	reset = '0m'

# Alternative to print, uses white color by default but accepts any color from the Colors class.
def print_color(text, color=colors.white, is_bold=False):
	print(f'{colors.pref}{color}' + text + f'{colors.pref}{colors.reset}')

database = sys.argv[1]
table1 = sys.argv[2]
table2 = sys.argv[3]
joincolumn = sys.argv[4]
idcolumn1 = sys.argv[5]
idcolumn2 = sys.argv[6]
print(f'{table1}({idcolumn1}) -- {joincolumn} -> {table2}({idcolumn2}) ')

conn = mysql.connector.connect(
    host="localhost",
    user="sqlbuilder",
    password="sqlbuilder",
    database=database
)

def query(conn, table1, table2, jcolumn, gcolumn1, icolumn2):
	cursor = conn.cursor()
	statement = f"SELECT concat_ws('-',{gcolumn1}) AS i, COUNT(*) AS c, GROUP_CONCAT(DISTINCT concat_ws('-',{icolumn2})) FROM {table1} LEFT JOIN {table2} USING ({jcolumn}) GROUP BY {gcolumn1} HAVING c > 1 ORDER BY c DESC LIMIT 5;";
	print_color(statement, color=colors.black)	
	cursor.execute(statement)
	result = cursor.fetchall()
	for row in result:
		src = row[0]
		count = row[1]
		concat = row[2]
		print_color(f"{table1} {src} -> {count} {table2} [{concat}]", color=colors.magenta)
		
def cardinality(conn, table1, table2, jcolumn, gcolumn1, icolumn2):
	cursor = conn.cursor()
	statement = f"SELECT MAX(c) AS maxcardinality, MIN(c) AS mincardinality FROM (SELECT COUNT({jcolumn}) AS c FROM {table1} LEFT JOIN {table2} USING ({jcolumn}) GROUP BY {gcolumn1}) AS subquery;";
	print_color(statement, color=colors.black)	
	cursor.execute(statement)
	result = cursor.fetchall()
	row = result[0]
	maxcount = row[0]
	mincount = row[1]
	print_color(f"1 of {table1} -> [{mincount} .. {maxcount}] {table2}", color=colors.cyan)
		
def main():
	cardinality(conn, table1, table2, joincolumn, idcolumn1, idcolumn2)
	query(conn, table1, table2, joincolumn, idcolumn1, idcolumn2)
	cardinality(conn, table2, table1, joincolumn, idcolumn2, idcolumn1)
	query(conn, table2, table1, joincolumn, idcolumn2, idcolumn1)

main()
