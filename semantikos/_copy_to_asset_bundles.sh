#!/bin/bash
# 22/03/2022

PROJECTDIR='/opt/devel/android-sqlunet-as/semantikos'
options="-i"

# C O L O R S

R='\u001b[31m'
G='\u001b[32m'
B='\u001b[34m'
Y='\u001b[33m'
M='\u001b[35m'
C='\u001b[36m'
Z='\u001b[0m'

function copy()
{
	local from="$1"
	local to="$2"
	[ -e "${from}" ] || echo -e "${Z}${from} does not exist${Z}"
	[ -e "${to}" ] || echo -e "${Z}${to} does not exist${Z}"
	#echo cp -P "${from}" "${to}"
	cp ${options} -P "${from}"/distrib* "${to}"
	cp ${options} -P "${from}"/*.zip "${to}"
	cp ${options} -P "${from}"/*.zip.md5 "${to}"
}

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

