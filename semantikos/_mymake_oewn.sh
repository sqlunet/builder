#!/bin/bash
# 05/09/2023

source define_build.sh

tag="${TAG}"
version="${BUILD}"

export B='\u001b[30m'
export R='\u001b[31m'
export G='\u001b[32m'
export B='\u001b[34m'
export Y='\u001b[33m'
export M='\u001b[35m'
export C='\u001b[36m'
export W='\u001b[37m'
export Z='\u001b[0m'
export BG_B='\u001b[40m'
export BG_R='\u001b[41m'
export BG_G='\u001b[42m'
export BG_Y='\u001b[43m'
export BG_B='\u001b[44m'
export BG_M='\u001b[45m'
export BG_C='\u001b[46m'
export BG_W='\u001b[47m'

echo -e "${BG_B}${W} OEWN ${tag} ${version} ${Z}"
./make-oewn.sh "${tag}" "${version}"

