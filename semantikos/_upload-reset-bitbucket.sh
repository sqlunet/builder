#!/bin/bash

ms="$@"
if [ -z "${ms}" ]; then
	ms="xn wn ewn sn vn fn"
fi

MAKE_REPO=true

# S O U R C E

source _bitbucket_repos.sh
source _reset-git.sh
source _confirm.sh

# C O L O R S

R='\u001b[31m'
G='\u001b[32m'
B='\u001b[34m'
Y='\u001b[33m'
M='\u001b[35m'
C='\u001b[36m'
Z='\u001b[0m'

# C O L L E C T   R E P O S

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
echo -e "${C}repos=${rs}${Z}"

# L O C A L   D A T A   C O P Y

echo -e "${Y}L O C A L   R E P O S${Z}"

./_copy_to_bitbucket_repos.sh ${ms}

# R E M O T E   R E P O

echo -e "${Y}R E M O T E   R E P O S${Z}"

for r in ${rs}; do
	echo -e "${Y}${r}${Z}"
	
	# prompt
	if ! confirm "Rebuilding '${G}${r}${Z}'" "Are you sure you want to rebuild bitbucket repo '${G}${r}${Z}'?" "Rebuilding '${G}${r}${Z}'"; then
		continue
	fi

	# action
	pushd "${REPODIR}/${r}" > /dev/null
	if [ "true" == "${MAKE_REPO}" ]; then
		rebuild_local	
		commit="$(get_head_commit)"
	fi
	commit="$(get_head_commit)"
	echo -e "${M}${commit}${Z}"	
	popd > /dev/null
		
	# prompt
	echo "${R}Past this point the repository is not recoverable from remote copy !${Z}"
	if confirm "Mirroring '${G}${r}${Z}'" "Are you sure you want to mirror bitbucket repo '${G}${r}${Z}' to remote?" "Mirroring '${G}${r}${Z}'"; then
		continue
	fi

	# action
	pushd "${REPODIR}/${r}" > /dev/null
	mirror_to_remote
	popd > /dev/null
	
done

