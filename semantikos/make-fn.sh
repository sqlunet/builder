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

fn=zip/fn-data_resolved-oewn${tag}-sqlite-${version}.zip
db=sqlunet-fn.sqlite
semantikos_db=sqlunet-fn.db
semantikos_db_zip=${semantikos_db}.zip
semantikos_dir=db-fn
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
unzip -q ${fn} -d fn

echo -e "${M}removing indexes${Z}"
rm -f fn/sql/sqlite/index/*

echo -e "${Y}R E S T O R E${Z}"

pushd fn > /dev/null
chmod +x ./restore-sqlite.sh 
./restore-sqlite.sh -y -d -r "../${db}"
popd > /dev/null

echo -e "${Y}A D D${Z}"
sqlite3 -init "sql/sources.sql" "${db}" .quit

echo -e "${Y}T R I M${Z}"
sqlite3 "${db}" "DELETE FROM sources WHERE name <> 'FrameNet'"

echo -e "${Y}V A C U U M ${Z}"
sqlite3 "${db}" 'VACUUM'

echo -e "${Y}I N D I C E S${Z}"
sqlite3 -init "sql/indexes-fn-sqlite.sql" "${db}" .quit

echo -e "${Y}S E A L${Z}"
./meta.sh "${db}" "wn/sql/build"
cp "${db}" "${semantikos_dir}/${semantikos_db}"

echo -e "${Y}A D D   T E X T S E A R C H   I N   N O N - D I S T   D B${Z}"
sqlite3 -init "sql/textsearch-fn-sqlite.sql" "${db}" .quit
sqlite3 -init "sql/textsearch-fn2-sqlite.sql" "${db}" .quit

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
md5sum ${files} > distrib-fn.md5
stat -c '%s %n' ${files} > distrib-fn.size
ls -1hs ${files} > distrib-fn.hsize
echo -e ${G}
cat distrib-fn.hsize
echo -e ${Z}
popd > /dev/null

mv "${db}" dist/
