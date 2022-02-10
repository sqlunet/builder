#!/bin/bash

#
# Copyright (c) 2021. Bernard Bou.
#
# -compat
# outdir
# indir
# files*: external sql template file names, if none they are taken from resources

source define_colors.sh

# -compat
if [ "$1" == "-compat" ]; then
  compatswitch="-compat"
  shift
  echo -e "${Y}compat mode${Z}"
else
  compatswitch=
fi

# module
module="$1"
shift
if [ "${module}" == "" ]; then
  echo "No module"
  exit 1
fi

# outdir
outdir="$1"
shift
if [ "${outdir}" == "" ]; then
  outdir="${module}/sql"
fi

# inputs
if [ "$*" != "" ]; then
  # limited
  indir="$1"
  shift
  for sql in $*; do
    base=$(basename ${sql})
    java -ea -cp generate-schema.jar org.sqlbuilder.common.SchemaGenerator ${compatswitch} "${module}" "${outdir}" "${indir}" "${sql}"
  done
else
  # all
  echo -e "${C}$(readlink -f ${outdir})${Z}"
  for db in mysql sqlite; do
    for type in create index reference; do
      echo -e "${M}${db}/${type}${Z}"
      java -ea -cp generate-schema.jar org.sqlbuilder.common.SchemaGenerator ${compatswitch} "${module}" "${outdir}/${db}/${type}" "${db}/${type}" $*
    done
  done
fi
