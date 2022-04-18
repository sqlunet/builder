#!/bin/bash
# 22/11/2021

# C O N S T S

fn=zip/fn-data_resolved-all-sqlite-2.0.0.zip
db=sqlunet-fn.sqlite
semantikos_db=sqlunet-fn.db
semantikos_db_zip=${semantikos_db}.zip
semantikos_dir=db_fn
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

rm -fR fn

echo -e "${Y}A D D${Z}"
unzip ${fn} -d fn

echo -e "${M}removing indexes${Z}"
rm fn/sql/sqlite/index/*

echo -e "${Y}R E S T O R E${Z}"

pushd fn > /dev/null
chmod +x ./restore-sqlite.sh 
./restore-sqlite.sh -y -d -r "../${db}"
popd > /dev/null

echo -e "${Y}A D D${Z}"
sqlite3 -init "sources.sql" "${db}" .quit

echo -e "${Y}T R I M${Z}"
sqlite3 "${db}" "DELETE FROM sources WHERE name <> 'FrameNet'"

echo -e "${Y}V A C U U M ${Z}"
sqlite3 "${db}" 'VACUUM'

echo -e "${M}I N D I C E S${Z}"
sqlite3 -init "indexes-fn-sqlite.sql" "${db}" .quit

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
md5sum ${files} > distrib-fn.md5
stat -c '%s %n' ${files} > distrib-fn.size
ls -1hs ${files} > distrib-fn.hsize
echo -e ${G}
cat distrib-fn.hsize
echo -e ${Z}
popd > /dev/null
