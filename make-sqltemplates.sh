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
  #cp -Rp "${m}/${templates_home}/mysql/create" "${m}/${templates_home}/mysql/index" "${m}/${templates_home}/sqlite/"
  for f in ${m}/${templates_home}/mysql/index/*.sql; do
    f2="${f}.tmp"
    b="`basename ${f}`"
    echo -e "${M}${b}${Z}"
    cat ${f} | ./template-mysql-filter.py > "${f2}"
    mv -f "${f2}" "${f}"
   done
  for f in ${m}/${templates_home}/mysql/reference/*.sql; do
    f2="${f}.tmp"
    b="`basename ${f}`"
    echo -e "${M}${b}${Z}"
    cat ${f} | ./template-mysql-filter.py > "${f2}"
    mv -f "${f2}" "${f}"
 done
done
