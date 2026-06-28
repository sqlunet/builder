#!/bin/bash

# set -e

ms="$@"
if [ -z "${ms}" ]; then
	ms="xn wn ewn sn vn fn"
fi

# C O L O R S

export K='\u001b[30m'
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

# R E L E A S E

# make release
RELEASE_NAME="Semantikos-2025-2"
RELEASE_TITLE="Semantikos 2025-2"
RELEASE_NOTES="2025-2"

# assets
assets=""
for suffix in '' ewn vn fn sn wn; do
        suffix2="-${suffix}"
        if [ -z $suffix ]; then
                suffix2=''
        fi
	#echo -e "${K}suffix=${suffix} suffix2=${suffix2}${Z}"
	fn=db${suffix2}/distrib${suffix2}
	fn2=db${suffix2}/sqlunet${suffix2}
	for ext in hsize size md5 ; do
	        f="${fn}.${ext}"
	        if [ ! -e "${f}" ] ; then
	                echo -e "${R}${f}${Z}"        
	        fi
                assets="${assets}
                ../../../${f}"
	done
	for ext in db.zip db.zip.md5 db.md5; do
	        f="${fn2}.${ext}"
	        if [ ! -e "${f}" ] ; then
	                echo -e "${R}${f}${Z}"        
	        fi
                assets="${assets}
                ../../../${f}"
	done
 done
echo -e "${C}${assets}${Z}"

pushd dist/repos/github > /dev/null
gh auth status
#gh auth logout
#gh auth login
gh release create "${RELEASE_NAME}" --title "${RELEASE_TITLE}" --notes "${RELEASE_NOTES}" 
gh release upload "${RELEASE_NAME}" ${assets}
gh release list
gh release view

popd > /dev/null

