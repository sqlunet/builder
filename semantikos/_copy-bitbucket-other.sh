#!/bin/bash

#origin="https://semantikos2@bitbucket.org/semantikos2/semantikos22.git"
m="$1"

# C O L O R S

R='\u001b[31m'
G='\u001b[32m'
B='\u001b[34m'
Y='\u001b[33m'
M='\u001b[35m'
C='\u001b[36m'
Z='\u001b[0m'

# D I R S

datadir="db-${m}"
datadir="$(readlink -m ${datadir})"
echo "datadir=${datadir}"

bitbucketrepo="repo2"
bitbucketdir="dist/repos/bitbucket/${bitbucketrepo}"
bitbucketdir="$(readlink -m ${bitbucketdir})"
echo "bitbucketdir=${bitbucketdir}"

# F I L E S

files_dist="distrib-${m}.hsize distrib-${m}.md5 distrib-${m}.size"
files_db="sqlunet-${m}.db sqlunet-${m}.db.md5"
files_dbzip="sqlunet-${m}.db.zip sqlunet-${m}.db.zip.md5"
files_sqlzip="sqlunet-${m}.sql.zip sqlunet-${m}.sql.zip.md5"
files="${files_dist} ${files_db} ${files_dbzip} ${files_sqlzip}"
files="${files_dist} ${files_dbzip}"

# M A I N

echo -e "${Y}S E M A N T I K O S   D B  +  S Q L  U P L O A D   T O   B I T B U C K E T   G I T   R E P O${Z}"
mkdir -p "${bitbucketdir}"
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

read -p "Are you sure you want to copy to bitbucket repo '${bitbucketrepo}' ? " -n 1 -r
echo    # (optional) move to a new line
echo -e "${Z}"
if ! [[ $REPLY =~ ^[Yy]$ ]]; then
    exit 2
fi
echo 'Copying ...'
echo -e "${Y}copy${Z}"
for f in ${files}; do
	echo -e "${B}${f}${Z}"
	cp -P ${datadir}/${f} ${bitbucketdir}/
done
exit

read -p "Are you sure you want to commit to bitbucket? " -n 1 -r
echo    # (optional) move to a new line
echo -e "${Z}"
if ! [[ $REPLY =~ ^[Yy]$ ]]; then
    exit 2
fi
echo 'Committing ...'
echo -e "${Y}commit${Z}"
pushd ${bitbucketdir} > /dev/null
git status
git add .
git commit -m 'Rebuild'

read -p "Are you sure you want to push to bitbucket? " -n 1 -r
echo    # (optional) move to a new line
echo -e "${Z}"
if ! [[ $REPLY =~ ^[Yy]$ ]]; then
    exit 2
fi
echo 'Pushing ...'
echo -e "${Y}push${Z}"
git push origin master
popd > /dev/null

