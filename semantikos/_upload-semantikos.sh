#!/bin/bash

VERSION="6.2.0"
DBTAG="$1"
if [ -z "${DBTAG}" ]; then
	echo "Missing tag"
	exit 1
fi

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
REMOTEDIR=/home/frs/project/s/sq/sqlunet
REMOTE_SEMANTIKOS_SUBDIR0=${VERSION}
REMOTE_SEMANTIKOS_SUBDIR1=${VERSION}/android
REMOTE_SEMANTIKOS_SUBDIR2=${VERSION}/semantikos

# D I R S

datadir=data/semantikos/${DBTAG}
datadir="$(readlink -m ${datadir})"
#echo ${datadir}

# F I L E S

files_dist="distrib-ewn.hsize distrib-ewn.md5 distrib-ewn.size"
files_db="sqlunet-ewn.db sqlunet-ewn.db.md5"
files_dbzip="sqlunet-ewn.db.zip sqlunet-ewn.db.zip.md5"
files_sqlzip="sqlunet-ewn.sql.zip sqlunet-ewn.sql.zip.md5"
files="${files_dist} ${files_db} ${files_dbzip} ${files_sqlzip}"

# M A I N

echo -e "${Y}S E M A N T I K O S   D B   +   S Q L   T O  U P L O A D   T O   S O U R C E F O R G E${Z}"

pushd ${datadir} > /dev/null
echo -e ${G}
ls -1hs ${files}
echo -e ${Z}
popd > /dev/null

echo -e "${C}$REMOTEDIR/$REMOTE_SEMANTIKOS_SUBDIR1${Z}"
echo -e "${C}$REMOTEDIR/$REMOTE_SEMANTIKOS_SUBDIR2${Z}"
echo -e "${R}"
read -p "Are you sure you want to upload? " -n 1 -r
echo    # (optional) move to a new line
echo -e "${Z}"
if ! [[ $REPLY =~ ^[Yy]$ ]]; then
    exit 2
fi
echo 'Proceeding ...'

echo -e "${Y}upload${Z}"

sftp $USER@$SITE <<EOF

lcd ${datadir}
lls -l *-ewn.*

-mkdir $REMOTEDIR/$REMOTE_SEMANTIKOS_SUBDIR0
-mkdir $REMOTEDIR/$REMOTE_SEMANTIKOS_SUBDIR1
cd $REMOTEDIR/$REMOTE_SEMANTIKOS_SUBDIR1
ls -l *-ewn.*
put distrib-ewn.hsize
put distrib-ewn.md5
put distrib-ewn.size
put sqlunet-ewn.db
put sqlunet-ewn.db.md5
put sqlunet-ewn.db.zip
put sqlunet-ewn.db.zip.md5
put sqlunet-ewn.sql.zip
put sqlunet-ewn.sql.zip.md5
ls -l *-ewn.*

-mkdir $REMOTEDIR/$REMOTE_SEMANTIKOS_SUBDIR2
cd $REMOTEDIR/$REMOTE_SEMANTIKOS_SUBDIR2
ls -l *-ewn.*
put distrib-ewn.hsize
put distrib-ewn.md5
put distrib-ewn.size
put sqlunet-ewn.db
put sqlunet-ewn.db.md5
put sqlunet-ewn.db.zip
put sqlunet-ewn.db.zip.md5
put sqlunet-ewn.sql.zip
put sqlunet-ewn.sql.zip.md5
ls -l *-ewn.*

quit
EOF

