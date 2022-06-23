#!/bin/bash

# P A R A M S

# Usage modules                  dbtag        dbdir
# Usage [bnc|sn|vn|pb|sl|fn|pm]  oewn21|other sql|other

dbmodules="$1"
shift
if [ -z "${dbmodules}" ]; then
  dbmodules="bnc sn vn pb sl fn pm"
fi

dbtag=$1
shift
if [ -z "${dbtag}" ]; then
  dbtag=wn31
fi

dbdir=$1
shift
if [ -z "${dbdir}" ]; then
  dbdir=sql31
fi

# S O U R C E S

source define_colors.sh

# M A I N

echo -e "${C}packing ${Y}${dbtag}${Z}"
echo "ant pack with dbtag=${dbtag}"

for dbmodule in ${dbmodules}; do
  pushd ${dbmodule} > /dev/null
    wd=$(readlink - m .)
    rm *.zip
    echo -e "${Y}${wd}${Z}"
    for dbdata in data data_resolved data_updated; do
      echo -e "${M}${dbmodule} ${dbdata} ${dbtag} ${dbdir}${Z}"
      ant -f ../make-dist-sql.xml -Dbasedir=${wd} -Ddbmodule=${dbmodule} -Ddbdata=${dbdata} -Ddbdir=${dbdir} -Ddbtag=${dbtag}
    done
  popd > /dev/null
done