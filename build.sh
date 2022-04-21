#!/bin/bash

# Copyright (c) 2021. Bernard Bou.

# Usage [all|wn|bnc|sn|vn|pb|sl|fn] (-resolve|-update)

source define_colors.sh

vmargs='-Xmx9G -ea'

# module
module="$1"
shift
if [ "${module}" == "" ]; then
  echo "No module. You may use 'all'."
  exit 1
fi
modules="${module}"
if [ "${modules}" == "all" ]; then
  modules="bnc sn vn pb fn sl pm"
  echo "All modules: ${modules}"
fi

# module
targets="$1"
shift
if [ "${targets}" == "" ]; then
  targets="-base -resolve -update"
  echo "All targets: ${targets}"
fi

for m in ${modules}; do
  echo -e "${Y}${m}${Z}"
  pushd "${m}" > /dev/null
  for t in ${targets}; do
    echo -e "${M}${m} ${t}${Z} ${m}.properties"
    if [ "${t}" = "-base" ]; then
      t=
    fi
    java ${vmargs} -cp ../sqlbuilder2.jar org.sqlbuilder.${m}.${m^}Module ${t} ${m}.properties
  done
  popd > /dev/null
done
