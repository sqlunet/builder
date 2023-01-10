#!/bin/bash

# reset git local and remote repositories using the same git remote url
# nothing local is deleted in working directory
# attach to remote url in $1 parameter
function recreate_git()
{
	local origin="$1"
	rm -fR .git
	git init
	git branch -M main
	git add .
	git commit -m "Initial"
	git remote add origin "${origin}"
	git gc
	git push --force --mirror
}

# get git remote url
function get_git_remote_url()
{
	export origin=$(git config --get remote.origin.url)
	echo "${origin}"
}

# get git head commit
function get_head_commit()
{
	export commit="$(git rev-parse HEAD)"
	echo "${commit}"
}


# full reset
# save remote url
# recreate git local repo
# force push to remote
# read head commit
function full_reset()
{
	origin=$(get_git_remote_url)
	recreate_git "${origin}"
	commit=$(get_head_commit)
	echo "head commit: ${commit}"
}

echo "sourced reset_git"
