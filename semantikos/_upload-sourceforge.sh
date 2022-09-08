#!/bin/bash

# L O C A L

FROM=dist

# R E M O T E

VERSION="2"
SITE=frs.sourceforge.net
USER=bbou,sqlunet
REMOTEDIR=/home/frs/project/s/sq/sqlunet/semantikos2/${VERSION}

# C O L O R S

R='\u001b[31m'
G='\u001b[32m'
B='\u001b[34m'
Y='\u001b[33m'
M='\u001b[35m'
C='\u001b[36m'
Z='\u001b[0m'

# M A I N

echo -e "${Y}S E M A N T I K O S  T O  S O U R C E F O R G E${Z}"

for m in '' -ewn -vn -fn -sn -wn; do

	# D I R S

	datadir=${FROM}/db${m}
	datadir="$(readlink -m ${datadir})"
	echo ${datadir}

	# S F T P

	echo -e "${C}m=${m}${Z}"

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
lls -l *${m}.*

-mkdir $REMOTEDIR
cd $REMOTEDIR
ls -l *${m}.*
put distrib${m}.hsize
put distrib${m}.md5
put distrib${m}.size
put sqlunet${m}.db.zip
put sqlunet${m}.db.zip.md5
ls -l *${m}.*

quit
EOF

	fi

done
