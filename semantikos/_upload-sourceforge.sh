#!/bin/bash

VERSION="1"

# C O L O R S

R='\u001b[31m'
G='\u001b[32m'
B='\u001b[34m'
Y='\u001b[33m'
M='\u001b[35m'
C='\u001b[36m'
Z='\u001b[0m'

# S I T E

SITE=frs.sourceforge.net
USER=bbou,sqlunet
REMOTEDIR=/home/frs/project/s/sq/sqlunet/semantikos2/${VERSION}

# M A I N

echo -e "${Y}S E M A N T I K O S  T O  S O U R C E F O R G E${Z}"
#for m in "" -ewn -vn -fn -sn; do
for m in -fn; do
echo -e "${C}m=${m}${Z}"

# D I R S

datadir=db${m}
#datadir="$(readlink -m ${datadir})"
echo ${datadir}

# S F T P

pushd ${datadir} > /dev/null
echo -e ${G}
ls -1hs ${files}
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
