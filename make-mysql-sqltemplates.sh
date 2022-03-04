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

  for op in create index reference; do
    for f in ${m}/${templates_home}/mysql/${op}/*.sql; do
      f2="${f}.tmp"
      b="`basename ${f}`"
      echo -e "${M}${b}${Z}"
      if [ ! -e "${f}" ]; then
       echo -e "${R}${f}${Z}"
      else
        echo -e "${C}${f} -> ${f2}${Z}"
      fi
      #cat ${f} | ./filter-mysql.py > "${f2}"
      mv -f "${f2}" "${f}"
     done
  done
done
