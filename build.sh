#!/bin/bash

# Copyright (c) 2021. Bernard Bou.

# Usage [all|wn|bnc|sn|vn|pb|sl|fn] (-resolve|-update)

set -Eeo pipefail
on_err() {
  local exit_code=$?
  local line_no=${BASH_LINENO[0]}
  echo "Error on line $line_no (exit code: $exit_code)."
}
trap on_err ERR

source define_colors.sh

JAR=semantikos-builder.jar

vmargs='-Xmx9G -ea'

# module
module="$1"
if [ "${module}" == "" ]; then
  echo "No module. You may use 'all'."
  exit 1
fi
shift
modules="${module}"
if [ "${modules}" == "all" ]; then
  modules="bnc sn vn pb pb31 fn sl su pm"
  echo -e "${K}All modules: ${modules}${Z}"
fi

# targets
targets="$1"
[ "$#" -eq 0 ] || shift
if [ "${targets}" == "" ]; then
  targets="-base -resolve -update"
  echo "All targets: ${targets}"
fi

# M A I N

for m in ${modules}; do
  echo -e "${Y}${m}${Z}"
  pushd "${m}" > /dev/null
  for t in ${targets}; do
    echo -e "${M}${m} ${t}${Z} ${K}${m}.properties${Z}"
    if [ "${t}" = "-base" ]; then
      t=
    fi
    java ${vmargs} -cp "../${JAR}" org.semantikos.${m}.${m^}Module ${t} ${m}.properties
  done
  popd > /dev/null
done
