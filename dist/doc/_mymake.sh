#!/bin/bash

./make-pdfs.sh
pushd queries/make > /dev/null
./make.sh
popd > /dev/null
