#!/bin/bash
# 22/03/2022

REPODIR='bitbucket'
options="-i"
options=

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
	cp ${options} -p "${from}"/distrib* "${to}"
	cp ${options} -p "${from}"/*.zip "${to}"
	cp ${options} -p "${from}"/*.zip.md5 "${to}"
	cp ${options} -p README.md "${to}"
}

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

