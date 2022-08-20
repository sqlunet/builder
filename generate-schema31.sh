#!/bin/bash

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
  echo "No module. You may use 'all'."
  exit 1
fi
modules="${module}"
if [ "${modules}" == "all" ]; then
  modules="bnc sn vn pb sl fn pm"
  echo "All modules: ${modules}"
fi
shift

# outdir
outbase="$1"
if [ "${outbase}" == "" ]; then
  outbase="sql31"
fi
shift

# inputs
indir="$1"
shift
inputs="$*"

for module in ${modules}; do
  echo -e "${Y}${module}${Z}"
  if [ "${outbase}" != "" ]; then
     outdir="${module}/${outbase}"
  else
    outdir="${outbase}"
  fi

  # inputs
  if [ "${inputs}" != "" ]; then
    # specified sqls
    for sql in ${inputs}; do
      base=$(basename ${sql})
      java -ea -cp generate-schema.jar org.sqlbuilder.common.SchemaGenerator ${compatswitch} "${module}" "${outdir}" "${indir}" "${sql}"
    done
  else
    # all sqls
    echo -e "${C}$(readlink -f ${outdir})${Z}"
    for db in mysql sqlite; do
      for type in create index reference anchor cleanup; do
        echo -e "${M}${db}/${type}${Z}"
        java -ea -cp generate-schema.jar org.sqlbuilder.common.SchemaGenerator ${compatswitch} "${module}" "${outdir}/${db}/${type}" "${db}/${type}" $*
      done
    done
  fi
done
