#!/bin/bash

source define_colors.sh

JAR=generate-legacy.jar

function generate()
{
	local what=$1
	local from=$2
	local to=$3
	echo -e "${M}${what} from=${from} to=${to}${Z}"
	java -cp "${JAR}" org.sqlbuilder2.legacy.LegacyModule legacy.properties ${what} from=${from} to=${to}
}

generate sensekeys 30
generate synsets 30 XX
