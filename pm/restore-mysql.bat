@echo off
rem bbou@ac-toulouse.fr
rem 03/12/2021 

set /P DB=Enter database name:

set DBTYPE=mysql
set DBUSER=root
set /P DBPWD=Enter %DBUSER% password:
set TABLES=pms roles predicates

if "%1"=="-d" call :deletedb
call :dbexists
if not %DBEXISTS%==0 call :createdb

set DBDATA=
if "%1"=="-r" goto :resolve
if "%1"=="-u" goto :update
goto :main
:resolve
set DBDATA=_resolved
goto :main
:update
set DBDATA=_updated
goto :main
:main

for %%T in (%TABLES%) do call :process sql/%DBTYPE%/create/%%T-create.sql "create %%T" %DB%
for %%T in (%TABLES%) do call :process sql/data%DBDATA%/%%T.sql "data %%T" %DB%
for %%T in (%TABLES%) do call :process sql/%DBTYPE%/index/%%T-index.sql "index %%T" %DB% --force
for %%T in (%TABLES%) do call :process sql/%DBTYPE%/cleanup/%%T-cleanup.sql "reference %%T" %DB% --force
for %%T in (%TABLES%) do call :process sql/%DBTYPE%/reference/%%T-reference.sql "reference %%T" %DB% --force
goto :eof

:process
setlocal
echo process %2
if not exist %1 goto :processerror
echo 	sql %1
mysql -u %DBUSER% --password=%DBPWD% %4 %3 < %1
goto :processend
:processerror
echo	sql file %1 does not exist
:processend
endlocal
goto :eof

:dbexists
setlocal
mysql -u %DBUSER% --password=%DBPWD% -e "\q" %DB% > NUL 2> NUL
endlocal & set DBEXISTS=%ERRORLEVEL% 
goto :eof

:deletedb
setlocal
echo delete %DB%
mysql --max_allowed_packet=100M -u %DBUSER% --password=%DBPWD% -e "DROP DATABASE %DB%;"
endlocal
goto :eof

:createdb
setlocal
echo create %DB%
mysql -u %DBUSER% --password=%DBPWD% -e "CREATE DATABASE %DB% DEFAULT CHARACTER SET UTF8;"
endlocal
goto :eof

:eof
