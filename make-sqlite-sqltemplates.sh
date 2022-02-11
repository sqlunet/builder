#!/bin/bash

R='\u001b[31m'
G='\u001b[32m'
B='\u001b[34m'
Y='\u001b[33m'
M='\u001b[35m'
C='\u001b[36m'
Z='\u001b[0m'

modules="bnc vn pb fn sn"

for m in ${modules}; do
  echo -e "${Y}${m}${Z}"
  templates_home="src/main/resources/${m}/sqltemplates"

  for op in create index; do
    for f in ${m}/${templates_home}/mysql/${op}/*-${op}.sql; do
      b="`basename ${f}`"
      f2="${m}/${templates_home}/sqlite/${op}/${b}"
      echo -e "${M}${b}${Z}"
      if [ ! -e "${f}" ]; then
       echo -e "${R}${f}${Z}"
      #else
      #  echo -e "${C}${f} -> ${f2}${Z}"
      fi

      cat ${f} | ./filter-flat.py | ./filter-mysql2sqlite.py > "${f2}"
     done
  done
done
exit

# DIRECT TEST TO A TEST DB
for m in ${modules}; do
  echo -e "${Y}${m}${Z}"
  templates_home="sql/"

  for op in create index; do
    for f in ${m}/${templates_home}/mysql/${op}/*-${op}.sql; do
      b="`basename ${f}`"
      f2="${m}/${templates_home}/sqlite/${op}/${b}"
      echo -e "${M}${b}${Z}"
      if [ ! -e "${f}" ]; then
       echo -e "${R}${f}${Z}"
      fi

      if ! cat ${f} | ./filter-flat.py | ./filter-mysql2sqlite.py | sqlite3 test.db; then
        echo -en "${R}"
        cat ${f} | ./filter-flat.py | ./filter-mysql2sqlite.py
        echo -en "${Z}"
        fi

     done
  done
done
