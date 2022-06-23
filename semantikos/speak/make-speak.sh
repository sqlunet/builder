#!/bin/bash
# 22/03/2022

# C O N S T S

db=sqlunet-speak.db
thisdir=`dirname $(readlink -m "$0")`

# C O L O R S

R='\u001b[31m'
G='\u001b[32m'
B='\u001b[34m'
Y='\u001b[33m'
M='\u001b[35m'
C='\u001b[36m'
Z='\u001b[0m'

# R U N

if [ ! -z "${SKIP}" ]; then # ---------------------------------------START-SKIP
echo
fi # ------------------------------------------------------------------END-SKIP


echo -e "${Y}T R I M${Z}"
for t in bnc_bncs bnc_spwrs bnc_imaginfs bnc_convtasks domains relations semrelations lexrelations synsets senses samples vframes vtemplates morphs poses adjpositions lexes_morphs senses_adjpositions senses_vframes senses_vtemplates meta sources; do
	echo -e "${Y}${t}${Z}"
	sqlite3 "${db}" "DROP TABLE IF EXISTS ${t};"
done

echo -e "${C}delete pronunciationless${Z}"
sqlite3 "${db}" "DELETE FROM words WHERE wordid IN (SELECT wordid FROM words LEFT JOIN lexes_pronunciations USING (wordid) WHERE pronunciationid IS NULL);"
sqlite3 "${db}" "DELETE FROM casedwords WHERE wordid IN (SELECT wordid FROM casedwords LEFT JOIN lexes_pronunciations USING (wordid) WHERE pronunciationid IS NULL);"

for t in lexes lexes_pronunciations; do
	echo -e "${M}${t}${Z}"
	#sqlite3 "${db}" "ALTER TABLE ${t} DROP COLUMN posid;"
done

echo -e "${Y}V A C U U M ${Z}"
sqlite3 "${db}" 'VACUUM'

