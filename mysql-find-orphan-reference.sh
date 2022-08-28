#!/bin/bash

db="$1"
table1="$2"
col1="$3"
table2="$4"
col2="$5"
sel1="$6"

dbprofile="sqlbuilder"
creds="--login-path=${dbprofile}"

#cat <<EOF
mysql ${creds} "${db}" <<EOF
SET @table1='${table1}';
SET @col1='${col1}';
SET @table2='${table2}';
SET @col2='${col2}';
SET @sel1='${sel1}';

-- count only
-- SET @sql = CONCAT("SELECT COUNT(*) FROM ",@table1," LEFT JOIN ",@table2," ON ",@table1,".",@col1,"=",@table2,".",@col2," WHERE ",@table1,".",@col1," IS NOT NULL AND ",@table2,".",@col2," IS NULL;");

-- full row
-- SET @sql = CONCAT("SELECT * FROM ",@table1," LEFT JOIN ",@table2," ON ",@table1,".",@col1,"=",@table2,".",@col2," WHERE ",@table1,".",@col1," IS NOT NULL AND ",@table2,".",@col2," IS NULL ORDER BY ",@table1,".",@col1,";");

-- row limited to selection
SET @sql = CONCAT("SELECT ",@sel1," FROM ",@table1," LEFT JOIN ",@table2," ON ",@table1,".",@col1,"=",@table2,".",@col2," WHERE ",@table1,".",@col1," IS NOT NULL AND ",@table2,".",@col2," IS NULL ORDER BY ",@table1,".",@col1,";");

SELECT @sql;

PREPARE stmt FROM  @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
EOF