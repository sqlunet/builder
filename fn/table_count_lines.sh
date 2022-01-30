#!/bin/bash

source define_tables.sh

outdir="$1"
if [ "${outdir}" == "" ]; then
  outdir="fn/"
fi
datadir=""

for table in ${tables}; do
  file=${outdir}${datadir}${table}.sql
  c=$(grep -c '^(' ${file})
  echo "${table} ${c}"
done