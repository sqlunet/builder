#!/bin/bash

db=sqlunet2
sections="wn-basic wn-dict wn-cased wn-relations wn-misc wn-morph wn-pronunciation wn-legacy wn-vframe wn-cardinality bnc sn vn pb fn-frame fn-lu fn-semtype fn-realisation fn-annotation fn-fulltext su"

# P A R A M S

oneoutput=
if [ "-one" == "$1" ]; then
	oneoutput=true
	shift
fi
createviews=true
if [ "-noviews" == "$1" ]; then
	donotcreateviews=
	shift
fi

# C O L O R S

export R='\u001b[31m'
export G='\u001b[32m'
export B='\u001b[34m'
export Y='\u001b[33m'
export M='\u001b[35m'
export C='\u001b[36m'
export Z='\u001b[0m'

# I N I T

init="--init-command=\"SET sql_mode=(SELECT REPLACE (@@sql_mode,'ONLY_FULL_GROUP_BY',''));\""

# D I R S

thisdir=$(dirname $(readlink -m $0))
parentdir=$(readlink -m "${thisdir}/..")
pushd "${thisdir}" > /dev/null

# C R E D E N T I A L S

source define_user.sh
echo "credentials ${creds}"

# in
sqldir=${parentdir}

# out
xmldir=${parentdir}/xml
mkdir -p ${xmldir}
htmldir=${parentdir}/html
mkdir -p ${htmldir}

# V I E W S

if [ ! -z "${createviews}" ]; then
	echo -e "${M}create views${Z}"
	eval mysql ${creds} ${init} ${db} < ${sqldir}/mysql-views.sql
	echo "created"
fi

# D O C S

if [ ! -z "${oneoutput}" ]; then
	echo -e "${C}merged${Z}"
	for s in ${sections}; do
		f="${sqldir}/mysql-${s}-query.sql"
		if [ ! -e ${f} ]; then
			echo -e "${R}does not exist${Z} ${f}"
			continue
		fi
		cat $f
	done \
	| eval mysql ${creds} ${init} -X ${db} \
	| tee ${xmldir}/sql-all.xml \
	| ./filter-xml2html.sh - > ${htmldir}/sql-all.html
else
	for s in ${sections}; do
		echo -e "${C}section=${s}${Z}"
		f="${sqldir}/mysql-${s}-query.sql"
		echo -e "sql=${M}${f}${Z}"
		if [ ! -e ${f} ]; then
			echo -e "${R}does not exist${Z} ${f}"
			continue
		fi
		cat $f \
		| eval mysql ${creds} ${init} -X ${db} \
		| tee ${xmldir}/sql-${s}.xml \
		| ./filter-xml2html.sh - > ${htmldir}/sql-${s}.html
	done \
fi

rm -Rf ${xmldir}
popd > /dev/null

