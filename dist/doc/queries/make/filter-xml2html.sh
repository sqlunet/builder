#!/bin/bash

echo "<?xml version=\"1.0\"?><mysql>`
sed '
/<?xml version="1.0"?>/d
/[0-9]* rows.*$/d
/^mysql>.*$/d
/^$/d
' $1 \
`</mysql>" \
|./filter-xml2html-mysql.sh
