#!/bin/bash

# P A R A M S

dbtag=$1
shift
if [ -z "${dbtag}" ]; then
  dbtag=oewn2021
fi
dbdir=$1
shift
if [ -z "${dbdir}" ]; then
  dbdir=sql
fi

dbmodules="$*"
if [ -z "${dbmodules}" ]; then
  #dbmodule=`basename $(pwd)`
  #dbmodules=${dbmodule}
  dbmodules="bnc sn vn pb sl fn pm"
fi
echo dbmodule=${dbmodule}

# S O U R C E S

source define_colors.sh

# M A I N

echo -e "${C}packing ${Y}${dbtag}${Z}"
echo "ant pack with dbtag=${dbtag}"

for dbmodule in ${dbmodules}; do
  pushd ${dbmodule} > /dev/null
    wd=$(readlink - m .)
    echo -e "${Y}${wd}${Z}"
    for dbdata in data data_resolved data_updated; do
      echo -e "${M}${dbmodule} ${dbdata} ${dbtag} ${dbdir}${Z}"
      ant -f ../make-dist-sql.xml -Dbasedir=${wd} -Ddbmodule=${dbmodule} -Ddbdata=${dbdata} -Ddbdir=${dbdir} -Ddbtag=${dbtag}
    done
  popd > /dev/null
done