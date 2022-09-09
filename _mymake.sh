#!/bin/bash

# C O L O R S

R='\u001b[31m'
G='\u001b[32m'
B='\u001b[34m'
Y='\u001b[33m'
M='\u001b[35m'
C='\u001b[36m'
Z='\u001b[0m'

echo -e "${Y}B U I L D${Z}"
./build.sh all

echo -e "${Y}P A C K${Z}"
./pack.sh
#./pack31.sh
