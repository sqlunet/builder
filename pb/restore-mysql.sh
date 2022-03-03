#!/bin/bash
# 03/03/2022

# C O N S T S

modules="pb"
tables="
argns
args
aspects
examples
forms
funcs
persons
rels
roles
members
rolesets
tenses
thetas
voices
words
pbrolesets_fnframes
pbrolesets_vnclasses
pbroles_vnroles
"
dbtype=mysql
ops="create data index cleanup reference"

# D I R

thisdir=`dirname $(readlink -m "$0")`
sqldir="${thisdir}/sql"

# I N C L U D E

source define_colors.sh

# M A I N

echo -e "${Y}Restore utility for ${dbtype}${Z}"
echo -e "${M}-the ${dbtype} user needs CREATE/DELETE permission${Z}"
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

# D A T A (PARAM 1)

dbdata=
if [ "$1" == "-r" ]; then
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
export db

# F U N C T I O N S

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
	#echo "mysql ${creds} --max_allowed_packet=100M \"${db}\" < \"${sqlfile}\""
	mysql ${creds} --max_allowed_packet=100M "${db}" < "${sqlfile}"
}

function dbexists()
{
	mysql ${creds} -e "\q" ${db} > /dev/null 2> /dev/null
	return $? 
}

function deletedb()
{
	echo -e "${M}delete ${db}${Z}"
	mysql ${creds} -e "DROP DATABASE ${db};"
}

function createdb()
{
	echo -e "${M}create ${db}${Z}"
	mysql ${creds} -e "CREATE DATABASE ${db} DEFAULT CHARACTER SET UTF8;"
}

function getcredentialslegacy()
{
  # read user
	read -p "Enter database user: " dbuser
	if [ -z "${dbuser}" ]; then
		echo "Define ${dbtype} user"
		exit 1
	fi

  # read password unless et in en variable
	if [ -z "$MYSQLPASSWORD" ]; then
		read -s -p "Enter ${dbuser}'s password (type '?' if you want to be asked each time, because it's unsafe): " dbpasswd
	else
	  dbpasswd="$MYSQLPASSWORD"
	fi

  # output as commandline switches
  echo -n "-u ${dbuser} "
	if [ ! -z "${dbpasswd}" ]; then
		if [ "${dbpasswd}" == "?" ]; then
			echo "--password"
		else
			echo "--password=${dbpasswd}"
		fi
	fi
}

function getcredentials()
{
  >&2 echo "This requires mysql_config_editor."
  profiles=`mysql_config_editor print --all | grep '\[.*\]'`
  if [ ! -z "${profiles}" ]; then
    >&2 echo "Existing profiles recorded by mysql_config_editor:"
    >&2 echo "${profiles}"
  fi

  # read profile
  read -p "Enter database user profile: " dbprofile
  if [ -z "${dbprofile}" ]; then
    echo "Define ${dbtype} user profile"
    exit 1
  fi

  if ! echo "${profiles}" | grep -q "\[${dbprofile}\]"; then

    # read user
    read -p "Enter database user: " dbuser
    if [ -z "${dbuser}" ]; then
      echo "Define ${dbtype} user"
      exit 1
    fi

    # editor
    >&2 echo "Passing data to mysql_config_editor (password will be obfuscated ~/.mylogin.cnf)"
    mysql_config_editor set --login-path=${dbprofile} --host=localhost --user=${dbuser} --password

  fi

	# output as commandline switches
	echo "--login-path=${dbprofile}"
}

# R U N

echo -e "${M}restoring ${db}${Z}"

#credentials
#export lcreds=`getcredentialslegacy`
#echo "OLD ${lcreds}"
export creds=`getcredentials`
#echo "credentials ${lcreds}"

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
		echo -e "dir=${M}${dir}${Z}"
		if [ ! -d "${dir}" ]; then
		  continue
		fi
		for table in ${tables}; do
			f="${dir}/${prefix}${table}${suffix}.sql"
			echo -e "sql=${Y}${f}${Z}"
			if [ ! -e "${f}" ]; then
			  continue
			fi
			process "${f}" "${op}"
		done
	done
done
