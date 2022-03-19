#!/bin/bash

R='\u001b[31m'
G='\u001b[32m'
B='\u001b[34m'
Y='\u001b[33m'
M='\u001b[35m'
C='\u001b[36m'
Z='\u001b[0m'

modules="bnc sn vn pb sl fn pm"

for m in ${modules}; do
  echo -e "${Y}${m}${Z}"
  templates_home="src/main/resources/${m}/sqltemplates"

  for op in create index anchor cleanup reference; do
    echo -e "${C}${op}${Z}"
    for f in ${m}/${templates_home}/mysql/${op}/*.sql; do
      b="`basename ${f}`"
      if [ -e "${f}" ]; then
        echo -e "${M}${b}${Z}"
      fi
    done
  done
done
