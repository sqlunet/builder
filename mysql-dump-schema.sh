#!/bin/bash
# 1313ou@gmail.com
# [--schema] 30|31|31_snapshot|XX


mysqldump --no-data -u root -p "$1"
