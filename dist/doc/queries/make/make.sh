#!/bin/bash

thisdir="`dirname $(readlink -m $0)`/"
thisdir="$(readlink -m ${thisdir})"

export R='\u001b[31m'
export G='\u001b[32m'
export B='\u001b[34m'
export Y='\u001b[33m'
export M='\u001b[35m'
export C='\u001b[36m'
export Z='\u001b[0m'

pushd ${thisdir} > /dev/null
echo -e "${Y}queries${Z}"
./make-queries.sh
echo -e "${Y}queries (one)${Z}"
./make-queries.sh -one
echo -e "${Y}pdf${Z}"
./make-queries-pdf.sh
echo -e "${Y}stats${Z}"
./make-stats.sh
popd > /dev/null

