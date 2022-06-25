#!/bin/bash
#origin="https://semantikos@bitbucket.org/semantikos/semantikos2.git"

REPODIR='bitbucket'
MAKE_REPO=true

# C O L O R S

R='\u001b[31m'
G='\u001b[32m'
B='\u001b[34m'
Y='\u001b[33m'
M='\u001b[35m'
C='\u001b[36m'
Z='\u001b[0m'

# R E P O   S E T U P
for repo in repo1 repo2 repo3; do
	echo -e "${Y}${repo}${Z}"
	pushd "${REPODIR}/${repo}" > /dev/null
	
	origin=$(git config --get remote.origin.url)
	echo -e "${M}${origin}${Z}"
	
	if [ ! -z "${MAKE_REPO}"]; then
		
		rm -fR .git
		git init
		git add *
		git commit -m "Initial"
		git remote add origin "${origin}"
		
		commit="$(git rev-parse HEAD)"
		echo -e "${G}${commit}${Z}"
	
	fi
	
	popd > /dev/null
done

# R E M O T E   R E P O
for repo in repo1 repo2 repo3; do
	echo -e "${C}${repo}${Z}"
	pushd "${REPODIR}/${repo}" > /dev/null
	
	git push origin master
	
	commit="$(git rev-parse HEAD)"
	echo -e "${G}${commit}${Z}"
	
	popd > /dev/null
done

