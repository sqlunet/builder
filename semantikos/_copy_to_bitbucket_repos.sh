#!/bin/bash
# 06/09/2023

ms="$@"
if [ -z "${ms}" ]; then
	ms="xn wn ewn sn vn fn"
fi

REPODIR='dist/repos/bitbucket'
options="-i"
options=""

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

# F U N C

function confirm()
{
	local from="$1"
	local to="$2"
	echo -en "Copy ${C}$(basename $1)${Z} to ${M}$(basename $2)${Z} ? "
	read -n 1 -r
	#echo    # (optional) move to a new line
	echo -e "${Z}"
	if ! [[ $REPLY =~ ^[Yy]$ ]]; then
		return 2
	fi
	return 0
}

function copy()
{
	local from="$1"
	local to="$2"
	if [ ! -e "${from}" ]; then 
		echo -e "${R}SRC ${from} does not exist${Z}"
		return 1
	fi
	if [ ! -e "${to}" ]; then
		echo -e "${R}DEST ${to} does not exist${Z}"
		return 2
	fi
	if confirm "${from}" "${to}"; then
		cp ${options} -p "${from}"/distrib* "${to}"
		cp ${options} -p "${from}"/*.zip "${to}"
		cp ${options} -p "${from}"/*.zip.md5 "${to}"
		cp ${options} -p README.md "${to}"
	fi
}

# M A I N

for m in ${ms}; do

	echo -e "${Y}${m}${Z}"

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
	bitbucketdir="${REPODIR}/${bitbucketrepo}"
	bitbucketdir="$(readlink -m ${bitbucketdir})"
	echo "bitbucketdir=${bitbucketdir}"

	copy "${datadir}" "${bitbucketdir}"

done

