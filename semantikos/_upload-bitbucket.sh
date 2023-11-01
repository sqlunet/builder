#!/bin/bash

#origin="https://semantikos2@bitbucket.org/semantikos2/semantikos22.git"
ms="$1"
if [ -z "${ms}" ]; then
	ms="xn wn ewn sn vn fn"
fi

declare -A repos
repos=(
[xn]=repo1
[wn]=repo3
[ewn]=repo3
[sn]=repo3
[vn]=repo3
[fn]=repo2
)

declare -A suffixes
suffixes=(
[xn]=""
[wn]="-wn"
[ewn]="-ewn"
[sn]="-sn"
[vn]="-vn"
[fn]="-fn"
)

# C O L O R S

export R='\u001b[31m'
export G='\u001b[32m'
export B='\u001b[34m'
export Y='\u001b[33m'
export M='\u001b[35m'
export C='\u001b[36m'
export Z='\u001b[0m'
export LR='\u001b[91m'
export LG='\u001b[92m'
export LY='\u001b[93m'
export LB='\u001b[94m'
export LM='\u001b[95m'
export LC='\u001b[96m'
export LW='\u001b[97m'

# M A I N

for m in ${ms}; do

	repo=${repos[${m}]}
	suffix=${suffixes[${m}]}
	echo "m=${m}"
	echo "repo=${repo}"
	echo "suffix=${suffix}"
	
	# D I R S

	datadir="db${suffix}"
	datadir="$(readlink -m ${datadir})"
	echo "datadir=${datadir}"

	bitbucketrepo=${repo}
	bitbucketdir="dist/repos/bitbucket/${bitbucketrepo}"
	bitbucketdir="$(readlink -m ${bitbucketdir})"
	echo "bitbucketdir=${bitbucketdir}"

	# F I L E S

	files_dist="distrib${suffix}.hsize distrib${suffix}.md5 distrib${suffix}.size"
	files_db="sqlunet${suffix}.db sqlunet${suffix}.db.md5"
	files_dbzip="sqlunet${suffix}.db.zip sqlunet${suffix}.db.zip.md5"
	files_sqlzip="sqlunet${suffix}.sql.zip sqlunet${suffix}.sql.zip.md5"
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
			c2=${LY}
		fi
		echo -e "${B}${f}${Z} from ${c1}${datadir}${Z}  to ${c2}${bitbucketdir}${Z}"
	done

	read -p "Are you sure you want to copy to bitbucket repo '${bitbucketrepo}' ? " -n 1 -r
	echo    # (optional) move to a new line
	echo -e "${Z}"
	if ! [[ $REPLY =~ ^[Yy]$ ]]; then
		exit 2
	fi
	echo 'Proceeding ...'
	echo -e "${Y}upload to ${m}${Z}"
	for f in ${files}; do
		echo -e "${B}${f}${Z}"
		cp -P ${datadir}/${f} ${bitbucketdir}/
	done

	read -p "Are you sure you want to upload to bitbucket? " -n 1 -r
	echo    # (optional) move to a new line
	echo -e "${Z}"
	if ! [[ $REPLY =~ ^[Yy]$ ]]; then
		echo
 		continue
	fi
	echo 'Proceeding ...'
	echo -e "${Y}upload${Z}"
	pushd ${bitbucketdir} > /dev/null
	git status
	#git add .
	#git commit -m 'Rebuild'
	#git push origin master
	popd > /dev/null
	echo
done

