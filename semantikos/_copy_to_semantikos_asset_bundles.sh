#!/bin/bash
# 06/09/2023

PROJECTDIR='/opt/devel/android-sqlunet-as/semantikos'
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
	echo -en "Copy ${C}$(basename $1)${Z} to ${M}$2${Z} ? "
	read -n 1 -r
	echo
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
		cp -p ${options} -Pv "${from}"/distrib* "${from}"/*.zip "${from}"/*.zip.md5 "${to}"
	fi
}

# M A I N

echo -e "${Y}xn${Z}"
from="db"
to="${PROJECTDIR}/db_ewn_asset/src/main/assets/XX"
copy "${from}" "${to}"

echo -e "${Y}wn31${Z}"
from="db-wn31"
to="${PROJECTDIR}/dbwn_wn31_asset/src/main/assets/31"
copy "${from}" "${to}"

echo -e "${Y}oewn${Z}"
from="db-oewn"
to="${PROJECTDIR}/dbewn_ewn_asset/src/main/assets/XX"
copy "${from}" "${to}"

echo -e "${Y}vn${Z}"
from="db-vn"
to="${PROJECTDIR}/dbvn_ewn_asset/src/main/assets/XX"
copy "${from}" "${to}"

echo -e "${Y}fn${Z}"
from="db-fn"
to="${PROJECTDIR}/dbfn_ewn_asset/src/main/assets/XX"
copy "${from}" "${to}"

echo -e "${Y}sn${Z}"
from="db-sn"
to="${PROJECTDIR}/dbsn_ewn_asset/src/main/assets/XX"
copy "${from}" "${to}"

