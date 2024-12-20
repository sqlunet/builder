#!/bin/bash

set -e

source define_build.sh 

# L O C A L

FROM=.

# R E M O T E

RELEASE="4"
SITE=frs.sourceforge.net
USER=bbou,sqlunet
REMOTEDIR=/home/frs/project/s/sq/sqlunet/semantikos${BUILDMAJOR}/${RELEASE}

# C O L O R S

R='\u001b[31m'
G='\u001b[32m'
B='\u001b[34m'
Y='\u001b[33m'
M='\u001b[35m'
C='\u001b[36m'
W='\u001b[37m'
K='\u001b[30m'
Z='\u001b[0m'

# M A I N

echo -e "${Y}S E M A N T I K O S  T O  S O U R C E F O R G E${Z}"

for suffix in '' -ewn -vn -fn -sn -wn; do

	# D I R S

	datadir=${FROM}/db${suffix}
	datadir="$(readlink -m ${datadir})"
	echo -e "${K}${datadir}${Z}"
	echo -e "${K}suffix=${suffix}${Z}"

	# S F T P

	pushd ${datadir} > /dev/null
	echo -e ${G}
	ls -1hsL *
	echo -e ${Z}
	popd > /dev/null

	echo -e "${C}$REMOTEDIR${Z}"
	echo -e "${R}"
	read -p "Are you sure you want to upload? " -n 1 -r
	echo    # (optional) move to a new line
	echo -e "${Z}"
	if [[ $REPLY =~ ^[Yy]$ ]]; then
		echo 'Proceeding ...'
		echo -e "${Y}upload${Z}"
		sftp $USER@$SITE <<EOF

lcd ${datadir}
lls -l *${suffix}.*

-mkdir $REMOTEDIR
cd $REMOTEDIR
ls -l *${suffix}.*
put distrib${suffix}.hsize
put distrib${suffix}.md5
put distrib${suffix}.size
put sqlunet${suffix}.db.zip
put sqlunet${suffix}.db.zip.md5
ls -l *${suffix}.*

quit
EOF

	fi
done
