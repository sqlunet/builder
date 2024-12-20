#/bin/bash

db="$1"

sql_schema="SELECT * FROM sqlite_schema;"
sql_tables_schema="SELECT * FROM sqlite_schema WHERE type='table';"
sql_indexes_schema="SELECT * FROM sqlite_schema WHERE type='index';"

sql_sizes="SELECT name, SUM(pgsize) as size
FROM sqlite_schema AS s
LEFT JOIN dbstat AS c USING (name)
GROUP BY name;"

sql_total_size="SELECT SUM(size) FROM (
SELECT name, SUM(pgsize) as size
FROM sqlite_schema AS s
LEFT JOIN dbstat AS c USING (name)
GROUP BY name);"

sql_total_table_size="SELECT SUM(size) FROM (
SELECT name, SUM(pgsize) as size
FROM sqlite_schema AS s
LEFT JOIN dbstat AS c USING (name)
WHERE type='table'
GROUP BY name);"

sql_total_index_size="SELECT SUM(size) FROM (
SELECT name, SUM(pgsize) as size
FROM sqlite_schema AS s
LEFT JOIN dbstat AS c USING (name)
WHERE type='index'
GROUP BY name);"


# sqlite3 "${db}" < "${sql}"

sqlite3 "${db}" "${sql_sizes}"

