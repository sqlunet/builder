#!/bin/bash
# 06/09/2023

# C O N S T S

modules="fn"
tables="
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
grouppatterns
grouppatterns_annosets
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
dbtype=sqlite
ops="create index cleanup reference"

# C O L O R S

export R='\u001b[31m'
export G='\u001b[32m'
export B='\u001b[34m'
export Y='\u001b[33m'
export M='\u001b[35m'
export C='\u001b[36m'
export Z='\u001b[0m'

# D I R

thisdir=`dirname $(readlink -m "$0")`
sqldir="${thisdir}/sql"

# M A I N

if [ "$1" == "-y" ]; then
	silent=true
	shift
else
  echo -e "${Y}Restore utility for ${dbtype}${Z}"
  echo -e "${R}-the -d switch will delete an existing database with this name${Z}"
  read -r -p "Are you sure? [y/N] " response
  case "$response" in
      [yY][eE][sS]|[yY])
          ;;
      *)
          exit 1
          ;;
  esac
fi

# D E L E T E (PARAM 1)

dbdelete=
if [ "$1" == "-d" ]; then
	dbdelete=true
	shift
fi

# D A T A (PARAM 1)

dbdata=
if [ "$1" == "-r" -o -e "${sqldir}/data_resolved" ]; then
	dbdata=_resolved
	shift
fi

if [ "$1" == "-u" ]; then
	dbdata=_updated
  ops="update"
	shift
fi

# D A T A B A S E (PARAM)

db="$1"
if [ -z "${db}" ]; then
	read -p "Enter ${dbtype} database name: " db
fi
if [[ "${db}" != *.sqlite ]]; then
	db="${db}.sqlite"
fi
export db

# F U N C T I O N S

pragmas_quick="PRAGMA synchronous=OFF;
PRAGMA count_changes=OFF;
PRAGMA journal_mode=MEMORY;
PRAGMA temp_store=MEMORY;
PRAGMA auto_vacuum=NONE;
PRAGMA automatic_index=OFF;"

pragmas_default="PRAGMA synchronous=FULL;
PRAGMA count_changes=OFF;
PRAGMA journal_mode=DELETE;
PRAGMA temp_store=OFF;
PRAGMA auto_vacuum=NONE;
PRAGMA automatic_index=OFF;"

begin="BEGIN TRANSACTION;"

commit="COMMIT TRANSACTION;"

tempdir=$(mktemp -d /tmp/sqlite.XXXXXXXXX)

function to_temp()
{
	local sqlfile="$1"
	local base="$(basename "${sqlfile}")"
	echo "${tempdir}/${base}"
}

function fast()
{
	local sqlfile="$1" # can be or include *
	local base="$(basename "${sqlfile}")"
	local sqlfile2="${tempdir}/${base}"
	printf '%s\n%s\n%s\n%s\n%s' "${pragmas_quick}" "${begin}" "$(cat ${sqlfile})" "${commit}" "${pragmas_default}"
}

function fast_to_temp()
{
	local sqlfile="$1" # can be or include *
	tempfile=$(to_temp "${sqlfile}")
	fast "${sqlfile}" > "${tempfile}"
	echo "${tempfile}"
}

function process()
{
	local sqlfile="$1"
	local op="$2"
	if [ ! -e "${sqlfile}" ];then
		echo -e "${R}${sqlfile} does not exist${Z}"
		return
	fi
	local base="$(basename "${sqlfile}")"
	#echo "${base}"
	case ${op} in
	create|index|cleanup|anchor|reference|data)
		sqlite3 -init "${sqlfile}" "${db}" .quit
		;;
	other|*)
		local sqlfile2=$(fast_to_temp "${sqlfile}")
		sqlite3 -init "${sqlfile2}" "${db}" .quit
		;;
	esac
}

function dbexists()
{
	test -e "${db}"
	return $? 
}

function deletedb()
{
	echo -e "${M}delete ${db}${Z}"
	rm -f "${db}"
}

function createdb()
{
	echo -e "${M}create ${db}${Z}"
	touch "${db}"
}

# R U N

echo -e "${M}restoring ${db}${Z}"

#database
if [ ! -z "${dbdelete}" ]; then
	deletedb
fi
if ! dbexists; then
	createdb
fi

# modules
for m in ${modules}; do
	echo -e "${C}${m}${Z}"
	for op in ${ops}; do
		echo -e "${M}${op}${Z}"
		case ${op} in
			data) 
				dir="${sqldir}/data${dbdata}"
				suffix=
				prefix=
				;;
		 	create|index|cleanup|anchor|reference)
		 		dir="${sqldir}/${dbtype}/${op}"
				suffix="-${op}"
				prefix=
		 		;;
		 	update)
		 		dir="${sqldir}/data${dbdata}"
				suffix=
				prefix="update_"
		 		;;
		esac
		#echo -e "dir=${M}${dir}${Z}"
		if [ ! -d "${dir}" ]; then
			  continue
			fi
		for table in ${tables}; do
			f="${dir}/${prefix}${table}${suffix}.sql"
			if [ ! -e "${f}" ]; then
			  continue
			fi
			echo -e "sql=${Y}$(basename ${f})${Z}"
			process "${f}" "${op}"
		done
	done
done
