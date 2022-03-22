#!/bin/bash

DBTAG=XX

./_upload-semantikos.sh ${DBTAG}
./_upload-github.sh ${DBTAG}
./_upload-bitbucket.sh ${DBTAG}

