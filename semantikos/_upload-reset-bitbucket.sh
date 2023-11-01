#!/bin/bash

ms="$@"
if [ -z "${ms}" ]; then
	ms="xn wn ewn sn vn fn"
fi

MAKE_REPO=true

# S O U R C E

source _bitbucket_repos.sh

# C O L O R S

R='\u001b[31m'
G='\u001b[32m'
B='\u001b[34m'
Y='\u001b[33m'
M='\u001b[35m'
C='\u001b[36m'
Z='\u001b[0m'

# S O U R C E

source _reset-git.sh ${ms}

# D A T A   S E T U P

echo -e "${Y}F I L L   R E P O S${Z}"
./_copy_to_bitbucket_repos.sh ${ms}

# R E M O T E   R E P O
echo -e "${Y}R E S E T   R E P O S${Z}"

rs=
for m in ${ms}; do
	r=${repos[${m}]}
	#echo "m=${m}"
	#echo "repo=${r}"
	if [[ "${rs}" != *"${r}"* ]]; then
		rs="${rs} ${r}"
	fi
done
rs=$(echo $rs | xargs -n1 | sort | xargs)

for r in ${rs}; do
	echo -e "${Y}${r}${Z}"
	
	echo -e "${R}Reset ${r}${Z} ..."
	read -p "Are you sure you want to reset bitbucket repo '${r}'? " -n 1 -r
	echo # (optional) move to a new line
	echo -e "${Z}"
	if ! [[ $REPLY =~ ^[Yy]$ ]]; then
		continue
	fi
	echo -e "Resetting ${G}${r}${Z} ..."

	pushd "${REPODIR}/${r}" > /dev/null

	if [ "true" == "${MAKE_REPO}" ]; then
		full_reset	
		commit="$(get_head_commit)"
		echo -e "${G}${commit}${Z}"
	else
		commit="$(get_head_commit)"
		echo -e "${M}${commit}${Z}"	
	fi
	
	popd > /dev/null
done

