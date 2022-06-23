#!/bin/bash
# 22/11/2021

# C O N S T S

wn=zip/oewn-2021-sqlite-1.21.0.zip
vn=zip/vn-data_resolved-oewn2021-sqlite-2.0.0.zip
pb=zip/pb-data_resolved-oewn2021-sqlite-2.0.0.zip
db=sqlunet-vn.sqlite
semantikos_db=sqlunet-vn.db
semantikos_db_zip=${semantikos_db}.zip
semantikos_dir=db-vn
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

echo -e "${Y}U N Z I P${Z}"

rm -fR wn
rm -fR vn
rm -fR pb

echo -e "${Y}A D D${Z}"
unzip ${wn} -d wn
unzip ${vn} -d vn
unzip ${pb} -d pb

echo -e "${M}removing indexes${Z}"
rm wn/sql/sqlite/index/*
rm vn/sql/sqlite/index/*
rm pb/sql/sqlite/index/*

echo -e "${Y}R E S T O R E${Z}"

pushd wn > /dev/null
chmod +x ./restore-sqlite.sh 
./restore-sqlite.sh -y -d "../${db}"
popd > /dev/null

pushd vn > /dev/null
chmod +x ./restore-sqlite.sh 
./restore-sqlite.sh -y -r "../${db}"
popd > /dev/null

pushd pb > /dev/null
chmod +x ./restore-sqlite.sh 
./restore-sqlite.sh -y -r "../${db}"
popd > /dev/null

pushd bnc > /dev/null
chmod +x ./restore-sqlite.sh 
./restore-sqlite.sh -y -r "../${db}"
popd > /dev/null

echo -e "${Y}A D D${Z}"
sqlite3 -init "sql/sources.sql" "${db}" .quit

echo -e "${Y}T R I M${Z}"
for t in vn_words pb_words; do
	sqlite3 "${db}" "ALTER TABLE ${t} DROP COLUMN word;"
done
sqlite3 "${db}" "DELETE FROM sources WHERE name <> 'WordNet' AND name <> 'Open English Wordnet' AND name <> 'VerbNet' AND name <> 'PropBank'"

echo -e "${Y}V A C U U M ${Z}"
sqlite3 "${db}" 'VACUUM'

echo -e "${Y}I N D I C E S${Z}"
sqlite3 -init "sql/indexes-vn-sqlite.sql" "${db}" .quit

echo -e "${Y}S E A L${Z}"
./meta.sh "${db}"
cp "${db}" "${semantikos_dir}/${semantikos_db}"

echo -e "${Y}A D D   T E X T S E A R C H   I N   N O N - D I S T   D B${Z}"
sqlite3 -init "sql/textsearch-vn-sqlite.sql" "${db}" .quit

echo -e "${Y}P A C K${Z}"
pushd ${semantikos_dir} > /dev/null
zip -r "${semantikos_db}.zip" "${semantikos_db}"
popd > /dev/null

echo -e "${Y}M D 5 / S I Z E${Z}"
pushd ${semantikos_dir} > /dev/null
files="${semantikos_db} ${semantikos_db_zip}"
echo "md5 and size for ${files}"
for f in ${files}; do
	md5sum ${f} > ${f}.md5
	stat -c '%s %n' ${f}
done
md5sum ${files} > distrib-vn.md5
stat -c '%s %n' ${files} > distrib-vn.size
ls -1hs ${files} > distrib-vn.hsize
echo -e ${G}
cat distrib-vn.hsize
echo -e ${Z}
popd > /dev/null
