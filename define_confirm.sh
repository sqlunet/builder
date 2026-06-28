#!/bin/bash

#
# Copyright (c) 2024. Bernard Bou.
#

R='\u001b[31m'
G='\u001b[32m'
B='\u001b[34m'
Y='\u001b[33m'
M='\u001b[35m'
C='\u001b[36m'
W='\u001b[37m'
Z='\u001b[0m'
E='\u001b[1m'
ZE='\u001b[22m'

function confirm() {
    echo -en "${W}${E}${1}${Z}${ZE}"
    read -p " ?> " -n 1 -r
    echo -e "${Z}"
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        return 0
    fi
    return 1
}

function confirm_or_exit() {
    if ! confirm "${1}"; then
        exit 1
    fi
}

