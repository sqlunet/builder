#!/bin/bash

# C O L O R S

R='\u001b[31m'
G='\u001b[32m'
B='\u001b[34m'
Y='\u001b[33m'
M='\u001b[35m'
C='\u001b[36m'
Z='\u001b[0m'

# P A R A M   1

dbtag=$1
shift
if [ -z "${dbtag}" ]; then
	echo "$0 <dbtag<disttag>"
	exit 1
fi

# P A R A M   2

disttag=$1

# D I R S

datadir="$(readlink -m data/semantikos/${dbtag})"
mkdir -p ${datadir}
#echo "datadir=${datadir}"

semantikos_db=sqlunet-ewn.db
semantikos_sql=sqlunet-ewn${disttag}.sql
semantikos_db_zip=${semantikos_db}.zip
semantikos_sql_zip=${semantikos_sql}.zip

files="${semantikos_db} ${semantikos_db_zip} ${semantikos_sql} ${semantikos_sql_zip}"
echo "md5 and size for ${files}"

# D I R S

pushd ${datadir} > /dev/null

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

