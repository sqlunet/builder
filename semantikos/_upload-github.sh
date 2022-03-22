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

# D I R S

githubdir=github
githubdir="$(readlink -m ${githubdir})"
echo "githubdir=${githubdir}"

datadir="/opt/data/nlp/wordnet/WordNet-XX/xewn"
datadir="$(readlink -m ${datadir})"
echo ${datadir}

# F I L E S

declare -A files
files=([sqlite]="xewnXX-sqlite.zip" [mysql]="xewnXX-mysql.zip")

# M A I N

echo -e "${Y}M Y S Q L   +   S Q L I T E   T O  U P L O A D   T O   G I T H U B${Z}"
for k in ${!files[@]}; do 
	d="${datadir}/${k}"
	for f in ${files[$k]}; do
		echo -e "${B}${files[$k]}${Z} as ${G}${k}${Z} to ${C}${d}${Z}"
	done
done

read -p "Are you sure you want to upload to github? " -n 1 -r
echo    # (optional) move to a new line
echo -e "${Z}"
if ! [[ $REPLY =~ ^[Yy]$ ]]; then
    exit 2
fi
echo 'Proceeding ...'

for k in ${!files[@]}; do 
	d="${datadir}/${k}"
	echo -e "${M}${k}${Z}"
	for f in ${files[$k]}; do
		echo -e "${G}${f}${Z} to ${C}${d}${Z}"
		cp ${githubdir}/${f} ${datadir}/${k}
		pushd ${datadir}/${k} > /dev/null
		git status
		git add .
		git commit -m 'Rebuild'
		git push origin master
		popd > /dev/null
	done
done

