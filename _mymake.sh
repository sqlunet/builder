#!/bin/bash

set -e

# C O L O R S

R='\u001b[31m'
G='\u001b[32m'
B='\u001b[34m'
Y='\u001b[33m'
M='\u001b[35m'
C='\u001b[36m'
Z='\u001b[0m'

# S O U R C E

source define_age.sh

# M A I N

echo -e "${Y}O E W N${Z}"
pushd sers > /dev/null
for f in *.ser; do
  age "${f}"
done
popd  > /dev/null

echo -e "${Y}L E G A C Y${Z}"
pushd legacy > /dev/null
./generate-legacy.sh all
popd  > /dev/null

echo -e "${Y}S C H E M A${Z}"
./generate-schema.sh all

echo -e "${Y}B U I L D${Z}"
./build.sh all

echo -e "${Y}P A C K${Z}"
./pack.sh
./pack31.sh
