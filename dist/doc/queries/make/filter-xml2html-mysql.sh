#!/bin/bash

#MYSQL TO HTML

out=-
in=-
xsls="mysql2html.xsl"

for xsl in $xsls; do
	./xsl-transform.sh $in $out $xsl html
	read
done
