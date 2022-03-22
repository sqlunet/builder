#!/bin/bash

VERSION="2021"

# C O L O R S

R='\u001b[31m'
G='\u001b[32m'
B='\u001b[34m'
Y='\u001b[33m'
M='\u001b[35m'
C='\u001b[36m'
Z='\u001b[0m'

# D I R S

datadir="data/semantikos/XX"
datadir="$(readlink -m ${datadir})"
echo "datadir=${datadir}"

bitbucketdir="data/bitbucket/semantikos2-${VERSION}/XX"
bitbucketdir="$(readlink -m ${bitbucketdir})"
echo "bitbucketdir=${bitbucketdir}"

# F I L E S

files_dist="distrib-ewn.hsize distrib-ewn.md5 distrib-ewn.size"
files_db="sqlunet-ewn.db sqlunet-ewn.db.md5"
files_db="sqlunet-ewn.db.md5 sqlunet-ewn.sql.md5"
files_dbzip="sqlunet-ewn.db.zip sqlunet-ewn.db.zip.md5"
files_sqlzip="sqlunet-ewn.sql.zip sqlunet-ewn.sql.zip.md5"
files="${files_dist} ${files_db} ${files_dbzip} ${files_sqlzip}"

# M A I N

echo -e "${Y}S E M A N T I K O S   D B  +  S Q L  U P L O A D   T O   B I T B U C K E T   G I T   R E P O${Z}"
for f in ${files}; do
	c1=${G}
	if [ ! -e "${datadir}/${f}" ];then
		c1=${R}
	fi
	c2=${G}
	if [ -e "${bitbucketdir}/${f}" ];then
		c2=${R}
	fi
	echo -e "${B}${f}${Z} from ${c1}${datadir}${Z}  to ${c2}${bitbucketdir}${Z}"
done

read -p "Are you sure you want to copy to bitbucket repo? " -n 1 -r
echo    # (optional) move to a new line
echo -e "${Z}"
if ! [[ $REPLY =~ ^[Yy]$ ]]; then
    exit 2
fi
echo 'Proceeding ...'
echo -e "${Y}upload${Z}"
for f in ${files}; do
	echo -e "${B}${f}${Z}"
	cp -P ${datadir}/${f} ${bitbucketdir}/
done

read -p "Are you sure you want to upload to bitbucket? " -n 1 -r
echo    # (optional) move to a new line
echo -e "${Z}"
if ! [[ $REPLY =~ ^[Yy]$ ]]; then
    exit 2
fi
echo 'Proceeding ...'
echo -e "${Y}upload${Z}"
pushd ${bitbucketdir} > /dev/null
git status
git add .
git commit -m 'Rebuild'
git push origin master
popd > /dev/null

