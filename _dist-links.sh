#!/bin/bash

source define_build.sh

v=${BUILD}

n=oewn${TAG}
rm -f dist/${n}/*
for m in bnc sn vn pb fn sl pm su; do
  for o in data data_resolved data_updated; do
      for d in mysql sqlite; do
        pushd dist/${n} > /dev/null
        ln -s ../../${m}/sql/${m}-${o}-${n}-${d}-${v}.zip
        popd > /dev/null
        done
    done
  done


n=wn${TAG31}
rm -f dist/${n}/*
for m in bnc; do
  for o in data data_resolved data_updated; do
      for d in mysql sqlite; do
       pushd dist/${n} > /dev/null
        ln -s ../../${m}/sql/${m}-${o}-${n}-${d}-${v}.zip
        popd > /dev/null
        done
    done
  done

