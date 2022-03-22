#!/bin/bash

dbfile=$1

ft=`stat -c %Y ${dbfile}`
fz=`stat -c %s ${dbfile}`

sqlite3 ${dbfile} << EOF
CREATE TABLE IF NOT EXISTS meta (created INTEGER, dbsize INTEGER );
DELETE FROM meta;
INSERT INTO meta (created, dbsize) VALUES(CURRENT_TIMESTAMP,$fz);
EOF

sqlite3 ${dbfile} << EOF
SELECT * FROM meta;
EOF
