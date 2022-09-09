#!/bin/bash
# 22/03/2022

source _define_build.sh

tag="${TAG}"
tag31="${TAG31}"
version="${BUILD}"

./make-xn.sh "${tag}" "${version}"
./make-wn31.sh "${tag31}" "${version}"
./make-oewn.sh "${tag}" "${version}"
./make-vn.sh "${tag}" "${version}"
./make-fn.sh "${tag}" "${version}"
./make-sn.sh "${tag}" "${version}"
