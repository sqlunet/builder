#!/bin/bash

set -e

# S O U R C E S

source define_colors.sh
source define_build.sh

# P A R A M S

# Usage modules                  dbtag        dbdir
# Usage [bnc|sn|vn|pb|sl|fn|pm]  oewn2X|other sql|other

dbmodules="$1"
shift
if [ -z "${dbmodules}" -o "${dbmodules}" == "all" ]; then
  dbmodules="bnc sn vn pb sl fn su pm"
fi

dbtag=$1
shift
if [ -z "${dbtag}" ]; then
  dbtag="oewn${TAG}"
fi

dbdir=$1
shift
if [ -z "${dbdir}" ]; then
  dbdir=sql
fi

version=$1
shift
if [ -z "${version}" ]; then
  version="${BUILD}"
fi

# D I R S

distdir=$(readlink -m "dist/${dbtag}")
mkdir -p "${distdir}"

# M A I N

echo -e "${C}packing ${Y}${dbtag}${Z}"
echo "ant pack with dbtag=${dbtag}"

for dbmodule in ${dbmodules}; do
  pushd ${dbmodule} > /dev/null
    wd=$(readlink - m .)
    rm -f *.zip
    echo -e "${Y}${wd}${Z}"
    for dbdata in data data_resolved data_updated; do
      echo -e "${M}${dbmodule} ${dbdata} ${dbtag} ${dbdir}${Z}"
      ant -f ../make-dist-sql.xml -Dbasedir=${wd} -Ddbmodule=${dbmodule} -Ddbdata=${dbdata} -Ddbdir=${dbdir} -Ddbtag=${dbtag} -Dversion=${version}
      target1="${dbdir}/${dbmodule}-${dbdata}-${dbtag}-mysql-${version}.zip"
      target2="${dbdir}/${dbmodule}-${dbdata}-${dbtag}-sqlite-${version}.zip"
      for t in "${target1}" "${target2}"; do
      	t2=$(readlink -m ${t})
      	ln -sf "${t2}" "${distdir}"
      	echo -e "${G}${t}${Z}"
      done
    done
  popd > /dev/null
done
