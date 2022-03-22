#!/bin/bash
# 22/11/2021

# C O N S T S

wn=zip/oewn-2021-sqlite-1.21.0.zip
bnc=zip/bnc-data_resolved-oewn2021-sqlite-2.0.0.zip
sn=zip/sn-data_resolved-oewn2021-sqlite-2.0.0.zip
vn=zip/vn-data_resolved-oewn2021-sqlite-2.0.0.zip
pb=zip/pb-data_resolved-oewn2021-sqlite-2.0.0.zip
sl=zip/sl-data_resolved-oewn2021-sqlite-2.0.0.zip
fn=zip/fn-data_resolved-oewn2021-sqlite-2.0.0.zip
pmc=zip/pm-data_resolved-oewn2021-sqlite-2.0.0.zip
db=sqlunet.sqlite
semantikos_db=sqlunet.db
semantikos_db_zip=${semantikos_db}.zip
semantikos_dir=db
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
rm -fR bnc

echo -e "${Y}A D D${Z}"
unzip ${wn} -d wn
unzip ${bnc} -d bnc
unzip ${sn} -d sn
unzip ${vn} -d vn
unzip ${pb} -d pb
unzip ${sl} -d sl
unzip ${pm} -d pm

echo -e "${R}removing indexes${Z}"
rm wn/sql/sqlite/index/*
rm bnc/sql/sqlite/index/*

echo -e "${Y}R E S T O R E${Z}"

pushd wn > /dev/null
chmod +x ./restore-sqlite.sh 
./restore-sqlite.sh -d "../${db}"
popd > /dev/null

pushd bnc > /dev/null
chmod +x ./restore-sqlite.sh 
./restore-sqlite.sh -r "../${db}"
popd > /dev/null

echo -e "${Y}A D D${Z}"
sqlite3 -init "sources.sql" "${db}" .quit

echo -e "${Y}T R I M${Z}"
for t in bncs convtasks imaginfs spwrs; do
	sqlite3 "${db}" "ALTER TABLE bnc_${t} DROP COLUMN word;"
done

echo -e "${Y}V A C U U M ${Z}"
sqlite3 "${db}" 'VACUUM'

echo -e "${M}I N D I C E S${Z}"
sqlite3 -init "sqlite-all-indexes.sql" "${db}" .quit

echo -e "${Y}S E A L${Z}"
./meta.sh "${db}"

echo -e "${Y}P A C K${Z}"
cp "${db}" "${semantikos_dir}/${semantikos_db}"
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
md5sum ${files} > distrib-ewn.md5
stat -c '%s %n' ${files} > distrib-ewn.size
ls -1hs ${files} > distrib-ewn.hsize
echo -e ${G}
cat distrib-ewn.hsize
echo -e ${Z}
popd > /dev/null
