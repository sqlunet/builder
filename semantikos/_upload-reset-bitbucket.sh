#!/bin/bash
#origin="https://semantikos@bitbucket.org/semantikos/semantikos2.git"

REPODIR='dist/repos/bitbucket'
MAKE_REPO=true

# C O L O R S

R='\u001b[31m'
G='\u001b[32m'
B='\u001b[34m'
Y='\u001b[33m'
M='\u001b[35m'
C='\u001b[36m'
Z='\u001b[0m'

# D A T A   S E T U P

echo -e "${Y}F I L L   R E P O S${Z}"
./_copy_to_bitbucket_repos.sh

# S O U R C E

source _reset-git.sh

# R E M O T E   R E P O

for repo in repo1 repo2 repo3; do
	echo -e "${Y}${repo}${Z}"
	
	read -p "Are you sure you want to reset bitbucket repo? " -n 1 -r
	echo # (optional) move to a new line
	echo -e "${Z}"
	if ! [[ $REPLY =~ ^[Yy]$ ]]; then
		continue
	fi
	echo 'Resetting ...'

	pushd "${REPODIR}/${repo}" > /dev/null

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

