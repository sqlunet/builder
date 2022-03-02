#!/bin/bash
# 22/11/2021

# C O N S T S

thisdir=`dirname $(readlink -m "$0")`
sqldir="${thisdir}/sql"
dbtype=sqlite
modules="wn"
tables="
classes
members
members_senses
groupings
members_groupings
restrtypes
restrs
roletypes
roles
classes_frames
frames
framenames
framesubnames
examples
frames_examples
semantics
predicates
predicates_semantics
syntaxes
words
"

# I N C L U D E

source define_colors.sh

# M A I N

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

# D E L E T E (PARAM 1)

dbdelete=
if [ "$1" == "-d" ]; then
	dbdelete=true
	shift
fi

# D A T A B A S E (PARAM 2)

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
	rm "${db}"
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
	for op in create data index reference; do
		echo -e "${M}${op}${Z}"
		case ${op} in
			data) 
				dir="${sqldir}/${op}"
				suffix=
				;;
		 	create|index|cleanup|anchor|reference)
		 		dir="${sqldir}/${dbtype}/${op}"
				suffix="-${op}"
		 		;;
		esac
		for table in ${tables}; do
			f="${dir}/${table}${suffix}.sql"
			if [ ! -e "${f}" -a "${op}" == "reference" ]; then
			  continue
			fi
			echo -e "sql=${Y}${f}${Z}"
			process "${f}" "${op}"
		done
	done
done
