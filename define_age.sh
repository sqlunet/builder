#!/bin/bash

# C O L O R S

R='\u001b[31m'
G='\u001b[32m'
B='\u001b[34m'
Y='\u001b[33m'
M='\u001b[35m'
C='\u001b[36m'
Z='\u001b[0m'

function hr() {
  local difference=$1
  if [ $difference -lt 60 ]; then
      echo "less than a minute ago."
  elif [ $difference -lt 3600 ]; then
      minutes=$((difference / 60))
      echo "$minutes minute(s) ago."
  elif [ $difference -lt 86400 ]; then
      hours=$((difference / 3600))
      echo "$hours hour(s) ago."
  elif [ $difference -lt 604800 ]; then
      days=$((difference / 86400))
      echo "$days day(s) ago."
  elif [ $difference -lt 2419200 ]; then
      weeks=$((difference / 604800))
      echo "$weeks week(s) ago."
  elif [ $difference -lt 29030400 ]; then
      months=$((difference / 2419200))
      echo "$months month(s) ago."
  else
      years=$((difference / 29030400))
      echo "$years year(s) ago."
  fi
}

function age() {
  local f="$1"
  current_date=$(date +%s)
  mod_date=$(stat -c %Y "${f}")  # Get modification time in seconds since epoch
  mod_age=$((current_date - mod_date))
  mod_date=$(stat -c %y "${f}"| awk '{print $1}')
  printf "${C}%-32s${Z} %s ${B}%s${Z}\n" "${f}" "${mod_date}"  "$(hr ${mod_age})"
}
