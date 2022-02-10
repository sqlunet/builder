#!/bin/bash

R='\u001b[31m'
G='\u001b[32m'
B='\u001b[34m'
Y='\u001b[33m'
M='\u001b[35m'
C='\u001b[36m'
Z='\u001b[0m'

templates_home="src/main/resources/sqltemplates"
modules="bnc vn pb fn sn"

for m in ${modules}; do
  echo -e "${Y}${m}${Z}"
  pushd "${m}/${templates_home}" > /dev/null
  echo -e "mysql -> sqlite/"
  #cp -Rp "mysql/create" "mysql/index" "sqlite/"
  for f in mysql/index/*; do
    cat ${f}
  done
  popd > /dev/null
done
