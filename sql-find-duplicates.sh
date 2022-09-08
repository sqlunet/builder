#!/bin/bash

#./sql-find-duplicates.sh /opt/devel/oewn/oewn-grind_wndb2sql/sql31/temp-sqlite/oewn31-work.sqlite senses sensekey senseid synsetid wordid


db="$1"
table="$2"
col="$3"
sel1="$4"
sel2="$5"
sel3="$6"

#cat <<EOF
sqlite3 "${db}" <<EOF
SELECT ${col}, COUNT(*), GROUP_CONCAT(DISTINCT ${sel1}), GROUP_CONCAT(DISTINCT ${sel2}), GROUP_CONCAT(DISTINCT ${sel3}) 
FROM ${table}
GROUP BY ${col} 
HAVING COUNT(*) > 1
ORDER BY COUNT(*) DESC, ${col} ASC;
EOF
