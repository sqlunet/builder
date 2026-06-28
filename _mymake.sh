#!/bin/bash

set -Eeo pipefail
on_err() {
  local exit_code=$?
  local line_no=${BASH_LINENO[0]}
  echo "Error on line $line_no (exit code: $exit_code)."
}
trap on_err ERR

# S O U R C E

source define_colors.sh
source define_confirm.sh
source define_age.sh

# A R G S

from=$1
[ "$#" -eq 0 ] || shift
if [ -z "${from}" ]; then
  from=initial
fi

# M A I N

echo -e "${Y}O E W N${Z}"

case "$from" in
        status) echo -e "${bY}${K}status${Z}"
                pushd sers > /dev/null
                for f in *.ser; do
                    age "${f}"
                done
                popd  > /dev/null
                ;;

        initial) echo -e "${bY}${K}initial${Z}"
                confirm_or_exit "Start"
                ;&
                
        legacy) echo -e "${bY}${K}legacy${Z}"
               echo -e "${Y}L E G A C Y${Z}"
               ./generate-legacy.sh all
               ;&
                
        legacy) echo -e "${bY}${K}schema${Z}"
               echo -e "${Y}S C H E M A${Z}"
               ./generate-schema.sh all
               ;&
                
        build) echo -e "${bY}${K}build${Z}"
                echo -e "${Y}B U I L D${Z}"
                ./build.sh all
                ;&

        pack) echo -e "${bY}${K}pack${Z}"
                echo -e "${Y}P A C K${Z}"
                ./pack.sh
                ./pack31.sh
                ;&

        end) echo -e "${bY}${K}end${Z}"
                ;;
esac

