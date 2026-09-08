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

# A R G S

PYTHON="$HOME"/.local/share/pipx/venvs/huggingface-hub/bin/python

from=$1
[ "$#" -eq 0 ] || shift
if [ -z "${from}" ]; then
  from=initial
fi

# M A I N

echo -e "${Y}O E W N${Z}"

case "$from" in
        prepare) echo -e "${bY}${K}prepare${Z}"
                ./_merge-data.sh
                ;;

        info) echo -e "${bY}${K}info${Z}"
                "$PYTHON" _huggingface-info.py
                ;;

        create) echo -e "${bY}${K}create${Z}"
                "$PYTHON" _huggingface-create.py
                ;;

        upload) echo -e "${bY}${K}upload${Z}"
                "$PYTHON" _huggingface-upload.py
                ;&
                
        initial) echo -e "${bY}${K}initial${Z}"
                confirm_or_exit "Start"
                ;&
                
        sync) echo -e "${bY}${K}sync${Z}"
                "$PYTHON" _huggingface-sync.py
                ;&

        end) echo -e "${bY}${K}end${Z}"
                ;;
esac

