#!/bin/bash

# reset git local and remote repositories using the same git remote url
# nothing local is deleted in working directory
# attach to remote url in $1 parameter
function recreate_git()
{
	local origin="$1"
	# delete repo control but not data
	rm -fR .git
	# meke init
	git init
	git branch -M main
	git add .
	git commit -m "Initial"
	# attach to given origin
	git remote add origin "${origin}"
	# mirror local to remote
	git gc
}

# extract git remote url
function get_git_remote_url()
{
	export origin=$(git config --get remote.origin.url)
	echo "${origin}"
}

# extract git head commit
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
function rebuild_local()
{
	origin=$(get_git_remote_url)
	recreate_git "${origin}"
	commit=$(get_head_commit)
	echo "head commit: ${commit}"
}

function mirror_to_remote()
{
	git push --force --mirror
}

echo "sourced reset_git"
