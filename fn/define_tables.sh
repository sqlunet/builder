#!/bin/bash

#
# Copyright (c) 2021. Bernard Bou.
#

export tables="
annosets
coretypes
corpuses
cxns
documents
fegrouprealizations
ferealizations
ferealizations_valenceunits
fes_excluded
fes_fegrouprealizations
fes_required
fes_semtypes
fes
fetypes
framerelations
frames_related
frames_semtypes
frames
gftypes
governors_annosets
governors
labelitypes
labels
labeltypes
layers
layertypes
lexemes
lexunits_governors
lexunits_semtypes
lexunits
grouppatterns_annosets
grouppatterns
grouppatterns_patterns
poses
pttypes
semtypes
semtypes_supers
sentences
subcorpuses_sentences
subcorpuses
valenceunits_annosets
valenceunits
words
"
echo ${tables}