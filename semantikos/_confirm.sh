#!/bin/bash
# 06/09/2023


# C O L O R S

export R='\u001b[31m'
export G='\u001b[32m'
export B='\u001b[34m'
export Y='\u001b[33m'
export M='\u001b[35m'
export C='\u001b[36m'
export Z='\u001b[0m'

# F U N C

function confirm()
{
	local title="$1"
	local message="$2"
	local proceed="$3"

	echo -e "${title}"
	echo -en "${message} "
	read -n 1 -r
	echo -e "${Z}"
	if ! [[ $REPLY =~ ^[Yy]$ ]]; then
		return 2
	fi
	echo -e "${proceed}"
	return 0
}

