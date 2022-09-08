#!/bin/bash

# L O C A L

FROM=dist/oewn2022

# R E M O T E

VERSION="2"
SITE=frs.sourceforge.net
USER=bbou,sqlunet
REMOTEDIR=/home/frs/project/s/sq/sqlunet/sqlunet2/${VERSION}

# C O L O R S

R='\u001b[31m'
G='\u001b[32m'
B='\u001b[34m'
Y='\u001b[33m'
M='\u001b[35m'
C='\u001b[36m'
Z='\u001b[0m'

# M A I N

echo -e "${Y}S Q L U N E T   T O  S O U R C E F O R G E${Z}"

# D I R S

datadir=$(readlink -m "${FROM}")
echo ${datadir}

# S F T P

pushd ${datadir} > /dev/null
echo -e ${G}
ls -1hsL *.zip
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
ls -l *.zip
put *.zip
ls -l *.zip

quit
EOF
fi

