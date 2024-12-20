#!/bin/bash
# 06/09/2023

set -e

REPODIR='dist/repos/bitbucket'

declare -A repos
repos=(
[xn]=repo1
[wn]=repo2
[ewn]=repo2
[sn]=repo2
[vn]=repo2
[fn]=repo3
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

declare -A content
content=(
[repo1]="xn"
[repo2]="wn ewn sn vn"
[repo3]="fn"
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

ms="$@"
if [ -z "${ms}" ]; then
	ms="${!repos[@]}"
fi

rs=
for k in ${ms}; do
	r=${repos[${k}]}
	#echo "m=${m}"
	#echo "repo=${r}"
	if [[ "${rs}" != *"${r}"* ]]; then
		rs="${rs} ${r}"
	fi
done
rs=$(echo $rs | xargs -n1 | sort | xargs)
for r in ${rs}; do
	echo -e "${Y}${r}${Z} ${content[${r}]}"
	pushd "${REPODIR}/${r}" > /dev/null
	echo -en "${M}"
	git remote -v
	echo -en "${Z}"
	popd > /dev/null
done
export BITBUCKETREPOS=${rs}

