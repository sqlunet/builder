#!/bin/bash
# 06/09/2023

REPODIR='dist/repos/bitbucket'
options="-i"
options=""

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
	echo -en "Copy ${C}$(basename $1)${Z} to $(basename $2) ? "
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

echo -e "${Y}xn${Z}"
from="db"
to="${REPODIR}/repo1"
copy "${from}" "${to}"


echo -e "${Y}wn31${Z}"
from="db-wn31"
to="${REPODIR}/repo2"
copy "${from}" "${to}"

echo -e "${Y}oewn${Z}"
from="db-oewn"
to="${REPODIR}/repo2"
copy "${from}" "${to}"

echo -e "${Y}vn${Z}"
from="db-vn"
to="${REPODIR}/repo2"
copy "${from}" "${to}"

echo -e "${Y}sn${Z}"
from="db-sn"
to="${REPODIR}/repo2"
copy "${from}" "${to}"


echo -e "${Y}fn${Z}"
from="db-fn"
to="${REPODIR}/repo3"
copy "${from}" "${to}"

