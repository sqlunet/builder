#!/bin/bash

set -e

dbfile="$1"
buildtag="$2"
echo "file $dbfile"
echo "tag $buildtag"
ft=`stat -c %Y "${dbfile}"`
fz=`stat -c %s "${dbfile}"`
if [ -e "${buildtag}" -a ! -d "${buildtag}" ]; then
	build=$(cat "${buildtag}")
else
	build="${buildtag}"
fi
build="${build//\'/\'\'}"
#echo "size ${fz}"
#echo "build <${build}>"

sqlite3 ${dbfile} << EOF
CREATE TABLE IF NOT EXISTS meta (created INTEGER, dbsize INTEGER, build TEXT);
DELETE FROM meta;
INSERT INTO meta (created,dbsize,build) VALUES (CURRENT_TIMESTAMP,${fz},'${build}');
EOF

sqlite3 ${dbfile} << EOF
SELECT created, dbsize, SUBSTR(build,INSTR('${build}','Date'),39) FROM meta;
EOF
