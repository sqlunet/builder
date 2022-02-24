#!/bin/bash

# Copyright (c) 2021. Bernard Bou.

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
shift

for m in ${modules}; do
  echo -e "${Y}${m}${Z}"
  pushd "${m}" > /dev/null
  java ${vmargs} -cp ../sqlbuilder2.jar org.sqlbuilder.${m}.${m^}Module ${m}.properties $*
  popd > /dev/null
done