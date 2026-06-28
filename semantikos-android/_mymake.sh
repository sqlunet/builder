#!/bin/bash
# 06/09/2023

set -Eeo pipefail
on_err() {
  local exit_code=$?
  local line_no=${BASH_LINENO[0]}
  echo "Error on line $line_no (exit code: $exit_code)."
}
trap on_err ERR

source define_build.sh

./_prepare.sh
./make-all.sh
./_upload.sh
