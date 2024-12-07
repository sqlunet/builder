#!/bin/bash

DIR='.'
if [ ! -z "$1" ]; then
  DIR="$1"
  fi

for f in ${DIR}/*; do sort $f -o $f; done
