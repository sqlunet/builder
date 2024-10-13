#!/bin/bash
# 06/09/2023

set -e

source define_build.sh

./_prepare.sh
./make-all.sh
