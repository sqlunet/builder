#!/bin/bash

set -Eeo pipefail
on_err() {
  local exit_code=$?
  local line_no=${BASH_LINENO[0]}
  echo "Error on line $line_no (exit code: $exit_code)."
}
trap on_err ERR

source define_colors.sh
source define_nids.sh


# A R G S

from=$1
[ "$#" -eq 0 ] || shift
if [ -z "${from}" ]; then
  from=initial
fi

# M A I N

echo -e "${Y}O E W N${Z}"

case "$from" in
        oewn)     for m in $OEWN_KEYS; do
                    v=${OEWN_BY_MODULE[${m}]}
                    echo -e "${Y}$m${Z} $v"
                    pushd $m >/dev/null
                    [ -e "nids" ] || mkdir "nids"
                    pushd "nids" >/dev/null
                    [ -e "oewn" ] || mkdir "oewn"
                    pushd "oewn" >/dev/null
                    for nid in $v; do
                      #echo "${nid}"
                      target="../../../nids/oewn/${nid}.ser"
                      [ -e "${target}" ] || echo -e "${R}${target}${Z}"
                      ln -sf "${target}" .
                    done
                    popd >/dev/null
                    popd >/dev/null
                    popd >/dev/null
                  done
                  ;&

        legacy)   for m in $LEGACY_KEYS; do
                    v=${LEGACY_BY_MODULE[${m}]}
                    echo -e "${Y}$m${Z} $v"
                    pushd $m >/dev/null
                    [ -e "nids" ] || mkdir "nids"
                    pushd "nids" >/dev/null
                    [ -e "legacy" ] || mkdir "legacy"
                    pushd "legacy" >/dev/null
                    for nid in $v; do
                      #echo "${nid}"
                      target="../../../nids/legacy/${nid}.ser"
                      [ -e "${target}" ] || echo -e "${R}${target}${Z}"
                      ln -sf "${target}" .
                    done
                    popd >/dev/null
                    popd >/dev/null
                    popd >/dev/null
                  done
                  ;&

        other)    for m in $OTHER_KEYS; do
                    v=${OTHER_BY_MODULE[${m}]}
                    echo -e "${Y}$m${Z} $v"
                    pushd $m >/dev/null
                    [ -e "nids" ] || mkdir "nids"
                    pushd "nids" >/dev/null
                    for m2 in $v; do
                      echo $m2
                      target="../../${m2}/nids_out"
                      [ -e "${target}" ] || echo -e "${R}${target}${Z}"
                      ln -sf "${target}" "${m2}"

                    done
                    popd >/dev/null
                    popd >/dev/null
                  done
                  ;;
esac