#!/bin/bash
# 22/11/${tag}

# P A R A M S

tag=$1
if [ -z "${tag}" ]; then
	echo "define tag as 1st param"
	exit 1
fi
version=$2
if [ -z "${version}" ]; then
	echo "define version as 2nd param"
	exit 2
fi

# C O N S T S

wn=zip/oewn-${tag}-sqlite-${version}.zip
sn=zip/sn-data_resolved-oewn${tag}-sqlite-${version}.zip
bnc=zip/bnc-data_resolved-oewn${tag}-sqlite-${version}.zip
db=sqlunet-sn.sqlite
semantikos_db=sqlunet-sn.db
semantikos_db_zip=${semantikos_db}.zip
semantikos_dir=db-sn
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
rm -fR sn

echo -e "${Y}A D D${Z}"
unzip -q ${wn} -d wn
unzip -q ${sn} -d sn

echo -e "${M}removing indexes${Z}"
rm -f wn/sql/sqlite/index/*
rm -f sn/sql/sqlite/index/*
rm -f bnc/sql/sqlite/index/*

echo -e "${M}tweaking restore script${Z}"
sed -i 's/-a "${op}" == "reference"/-a "${op}" == "index" -o "${op}" == "reference"/' wn/restore-sqlite.sh

echo -e "${Y}R E S T O R E${Z}"

pushd wn > /dev/null
chmod +x ./restore-sqlite.sh 
./restore-sqlite.sh -y -d "../${db}"
popd > /dev/null

pushd sn > /dev/null
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
for t in sn_syntagms; do
	sqlite3 "${db}" "ALTER TABLE ${t} DROP COLUMN sensekey1;"
	sqlite3 "${db}" "ALTER TABLE ${t} DROP COLUMN sensekey2;"
done
sqlite3 "${db}" "DELETE FROM sources WHERE name <> 'WordNet' AND name <> 'Open English Wordnet' AND name <> 'SyntagNet' AND name <> 'BNC'"

echo -e "${Y}V A C U U M ${Z}"
sqlite3 "${db}" 'VACUUM'

echo -e "${Y}I N D I C E S${Z}"
sqlite3 -init "sql/indexes-sn-sqlite.sql" "${db}" .quit

echo -e "${Y}S E A L${Z}"
./meta.sh "${db}"
cp "${db}" "${semantikos_dir}/${semantikos_db}"

echo -e "${Y}A D D   T E X T S E A R C H   I N   N O N - D I S T   D B${Z}"
sqlite3 -init "sql/textsearch-sn-sqlite.sql" "${db}" .quit

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
md5sum ${files} > distrib-sn.md5
stat -c '%s %n' ${files} > distrib-sn.size
ls -1hs ${files} > distrib-sn.hsize
echo -e ${G}
cat distrib-sn.hsize
echo -e ${Z}
popd > /dev/null

mv "${db}" dist/
