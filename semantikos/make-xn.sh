#!/bin/bash
# 22/03/2022

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

wn=zip/oewn-${tag}-sqlite-1.${tag:2:2}.zip
bnc=zip/bnc-data_resolved-oewn${tag}-sqlite-${version}.zip
sn=zip/sn-data_resolved-oewn${tag}-sqlite-${version}.zip
vn=zip/vn-data_resolved-oewn${tag}-sqlite-${version}.zip
pb=zip/pb-data_resolved-oewn${tag}-sqlite-${version}.zip
sl=zip/sl-data_resolved-oewn${tag}-sqlite-${version}.zip
fn=zip/fn-data_resolved-oewn${tag}-sqlite-${version}.zip
pm=zip/pm-data_resolved-oewn${tag}-sqlite-${version}.zip
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
rm -fR sn
rm -fR vn
rm -fR pb
rm -fR sl
rm -fR fn
rm -fR pm

echo -e "${Y}A D D${Z}"
unzip -q ${wn} -d wn
unzip -q ${bnc} -d bnc
unzip -q ${sn} -d sn
unzip -q ${vn} -d vn
unzip -q ${pb} -d pb
unzip -q ${sl} -d sl
unzip -q ${fn} -d fn
unzip -q ${pm} -d pm

echo -e "${M}removing indexes${Z}"
for m in wn bnc sn vn pb sl fn pm; do
	rm -f ${m}/sql/sqlite/index/*
done

echo -e "${M}tweaking restore script${Z}"
sed -i 's/-a "${op}" == "reference"/-a "${op}" == "index" -o "${op}" == "reference"/' wn/restore-sqlite.sh

echo -e "${Y}R E S T O R E${Z}"

m=wn
echo -e "${Y}${m}${Z}"
pushd ${m} > /dev/null
chmod +x ./restore-sqlite.sh 
./restore-sqlite.sh -y -d "../${db}"
popd > /dev/null

for m in bnc sn vn pb sl fn pm; do
	echo -e "${Y}${m}${Z}"
	pushd ${m} > /dev/null
	chmod +x ./restore-sqlite.sh 
	./restore-sqlite.sh -y -r "../${db}"
	popd > /dev/null
done

echo -e "${Y}A D D${Z}"
sqlite3 -init "sql/pm.sql" "${db}" .quit
sqlite3 -init "sql/sources.sql" "${db}" .quit

echo -e "${Y}T R I M${Z}"
for t in bnc_bncs bnc_convtasks bnc_imaginfs bnc_spwrs vn_words pb_words fn_words; do
	sqlite3 "${db}" "ALTER TABLE ${t} DROP COLUMN word;"
done

echo -e "${Y}V A C U U M ${Z}"
sqlite3 "${db}" 'VACUUM'

echo -e "${Y}I N D I C E S${Z}"
sqlite3 -init "sql/indexes-all-sqlite.sql" "${db}" .quit

echo -e "${Y}S E A L${Z}"
./meta.sh "${db}"
cp "${db}" "${semantikos_dir}/${semantikos_db}"

echo -e "${Y}A D D   T E X T S E A R C H   A N D    V I E W   I N   N O N - D I S T   D B${Z}"
sqlite3 -init "sql/textsearch-wn-sqlite.sql" "${db}" .quit
sqlite3 -init "sql/textsearch-sn-sqlite.sql" "${db}" .quit
sqlite3 -init "sql/textsearch-vn-sqlite.sql" "${db}" .quit
sqlite3 -init "sql/textsearch-fn-sqlite.sql" "${db}" .quit
sqlite3 -init "sql/views-wn-sqlite.sql" "${db}" .quit

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
md5sum ${files} > distrib.md5
stat -c '%s %n' ${files} > distrib.size
ls -1hs ${files} > distrib.hsize
echo -e ${G}
cat distrib.hsize
echo -e ${Z}
popd > /dev/null

mv "${db}" dist/
