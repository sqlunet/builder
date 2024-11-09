#!/bin/bash
# 13/10/2024

set -e

source define_build.sh

R='\u001b[31m'
G='\u001b[32m'
Z='\u001b[0m'

function check()
{
	local f="$1"
	if [ ! -e "${f}" ]; then
		echo -e "${R}${f} $(readlink -m ${f}) does not exist${Z}"
		return 1
	fi
	echo -e "${G}${f}${Z}"
}

dir=zip
from_oewn=/mnt/data2/devel/oewn/oewn-grind_yaml2sql/sql
from_wn=/mnt/data2/devel/oewn/oewn-grind_wndb2sql/sql31
from=..
dir=$(readlink -m ${dir})
from=$(readlink -m ${from})

zs="
${from_oewn}/oewn-${TAG}-sqlite-${BUILD}.zip
${from_wn}/wn-${TAG31}-sqlite-${BUILD}.zip
${from}/bnc/sql/bnc-data_resolved-oewn${TAG}-sqlite-${BUILD}.zip
${from}/bnc/sql31/bnc-data_resolved-wn${TAG31}-sqlite-${BUILD}.zip
${from}/fn/sql/fn-data_resolved-oewn${TAG}-sqlite-${BUILD}.zip
${from}/pb/sql/pb-data_resolved-oewn${TAG}-sqlite-${BUILD}.zip
${from}/pm/sql/pm-data_resolved-oewn${TAG}-sqlite-${BUILD}.zip
${from}/sl/sql/sl-data_resolved-oewn${TAG}-sqlite-${BUILD}.zip
${from}/sn/sql/sn-data_resolved-oewn${TAG}-sqlite-${BUILD}.zip
${from}/vn/sql/vn-data_resolved-oewn${TAG}-sqlite-${BUILD}.zip
"

rm "${dir}"/*.zip
for z in $zs; do
  if check "${z}"; then
    pushd "${dir}" > /dev/null
    ln -s "${z}"
    popd > /dev/null
  fi
done
