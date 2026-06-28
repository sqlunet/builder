#!/bin/bash

#find . -name "sers" -type d | xargs ls -1R

declare -A OEWN_BY_MODULE
export OEWN_BY_MODULE=(
 [bnc]="nid_words"
 [fn]="nid_words"
 [pb]="nid_words sensekeys_words_synsets"
 [pb31]="nid_words sensekeys_words_synsets"
 [pm]="nid_words sensekeys_words_synsets"
 [sn]="nid_words sensekeys_words_synsets"
 [su]="nid_synsets nid_words"
 [vn]="nid_words sensekeys_words_synsets"
 )
export OEWN_KEYS="${!OEWN_BY_MODULE[@]}"
export OEWN_KEYS="bnc fn pb pb31 pm sn su vn"

declare -A LEGACY_BY_MODULE
export LEGACY_BY_MODULE=(
[sn]="senses30_sensekeys"
[su]="synsets30_synsets31"
)
export LEGACY_KEYS="${!LEGACY_BY_MODULE[@]}"
export LEGACY_KEYS="sn su"

declare -A OTHER_BY_MODULE
export OTHER_BY_MODULE=(
[pb]="fn vn"
[pb31]="fn vn"
[pm]="fn pb vn"
[sl]="pb vn"
)
export OTHER_KEYS="${!OTHER_BY_MODULE[@]}"
export OTHER_KEYS="pb pb31 sl pm"