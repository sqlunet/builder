#!/bin/bash

# set -e

source define_colors.sh
source _confirm.sh

# P A R A M S

# stage
from="$1"
shift

# tag
tag="$1"
shift
if [ -z "${tag}" ]; then
	echo -e "${R}No tag${Z}"
	exit 1
fi

ms="$@"
if [ -z "${ms}" ]; then
	ms="xn wn ewn sn vn fn"
fi

# release params
RELEASE_NAME="Semantikos-${tag}"
RELEASE_TITLE="Semantikos ${tag}"
RELEASE_NOTES="${tag} snapshot"

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

# M A I N

echo -e "${Y}Make GitHub release${Z}"
echo -e "Github assets:
${C}${assets}${Z}"
if confirm 'Github' "Proceed $from with release ${RELEASE_NAME}?" 'proceeding...'; then

case "$from" in
       initial) echo -e "${bY}${K}initial${Z}"
                ;&
       auth) echo -e "${bY}${K}auth${Z}"
                pushd dist/repos/github > /dev/null
                gh auth status
                #gh auth logout
                #gh auth login
                popd > /dev/null
                ;&

        create) echo -e "${bY}${K}create${Z}"
                pushd dist/repos/github > /dev/null
                gh release create "${RELEASE_NAME}" --title "${RELEASE_TITLE}" --notes "${RELEASE_NOTES}" 
                popd > /dev/null
                ;&

        upload) echo -e "${bY}${K}upload${Z}"
                pushd dist/repos/github > /dev/null
                gh release upload "${RELEASE_NAME}" ${assets}
                popd > /dev/null
                ;&
                
        list) echo -e "${bY}${K}list${Z}"
                pushd dist/repos/github > /dev/null
                gh release list
                popd > /dev/null
                ;&
               
        view) echo -e "${bY}${K}view${Z}"
                pushd dist/repos/github > /dev/null
                gh release view "${RELEASE_NAME}"
                popd > /dev/null
                ;&
                
        end) echo -e "${bY}${K}end${Z}"
                ;;
                
        flush) echo -e "${bY}${K}end${Z}"
                pushd dist/repos/github > /dev/null
                for a in ${assets}; do
                  an=$(basename ${a})
                  echo "delete ${an}"
                  gh release delete-asset "${RELEASE_NAME}" ${an}
                done
                popd > /dev/null
                ;;
esac

fi
