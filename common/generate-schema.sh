#!/bin/bash

#
# Copyright (c) 2021. Bernard Bou.
#
# -compat
# outdir
# indir
# files*: external sql template file names, if none they are taken from resources

source define_colors.sh

JAR=generate-schema.jar

if [ "$1" == "-compat" ]; then
  compatswitch="-compat"
  shift
  echo -e "${Y}compat mode${Z}"
else
  compatswitch=
fi

outdir="$1"
shift
if [ "${outdir}" == "" ]; then
  outdir="sql"
fi

if [ "$*" != "" ]; then
  indir="$1"
  shift
  for sql in $*; do
    base=$(basename ${sql})
    java -ea -cp "${JAR}" org.semantikos.common.SchemaGenerator ${compatswitch} "${outdir}" "${indir}" "${sql}"
  done
else
  echo -e "${C}$(readlink -f ${outdir})${Z}"
  for db in mysql sqlite; do
    for type in create index reference; do
      echo -e "${M}${db}/${type}${Z}"
      java -ea -cp "${JAR}" org.semantikos.common.SchemaGenerator ${compatswitch} "${outdir}/${db}/${type}" "${db}/${type}" $*
    done
  done
fi
