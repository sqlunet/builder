#!/bin/bash
# 06/09/2023

set -Eeo pipefail
on_err() {
  local exit_code=$?
  local line_no=${BASH_LINENO[0]}
  echo "Error on line $line_no (exit code: $exit_code)."
}
trap on_err ERR

MERGED=db-all

mkdir "${MERGED}"

cp -al db/*      "${MERGED}/"
cp -al db-oewn/* "${MERGED}/"
cp -al db-wn31/* "${MERGED}/"
cp -al db-vn/*   "${MERGED}/"
cp -al db-sn/*   "${MERGED}/"
cp -al db-fn/*   "${MERGED}/"

rm ${MERGED}/*.sh 
rm ${MERGED}/*.db
