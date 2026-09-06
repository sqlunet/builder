#!/bin/bash

set -e

RELEASE="2026-1"
./_upload-sourceforge.sh
./_upload-github.sh flush "${RELEASE}"
./_upload-github.sh upload "${RELEASE}"

