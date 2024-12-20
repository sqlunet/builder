#!/bin/bash

set -e

./_upload-sourceforge.sh

./_upload-bitbucket.sh 		wn ewn sn vn
./_upload-reset-bitbucket.sh 	xn fn

