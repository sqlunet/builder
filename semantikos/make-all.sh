#!/bin/bash
# 22/03/2022

tag="2022"
version="2.0.0"

./make-xn.sh "${tag}" "${version}"
./make-wn31.sh
./make-oewn.sh "${tag}" "${version}"
./make-vn.sh "${tag}" "${version}"
./make-fn.sh "${tag}" "${version}"
./make-sn.sh "${tag}" "${version}"
