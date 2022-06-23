@echo off
rem bbou@ac-toulouse.fr
rem 03/12/2021 

set /P DB=Enter database name:

set DBTYPE=sqlite
set TABLES=synsets words casedwords pronunciations morphs poses relations domains samples vframes vtemplates adjpositions lexes senses lexes_morphs lexes_pronunciations senses_adjpositions lexrelations senses_vframes senses_vtemplates semrelations

if "%1"=="-d" call :deletedb
call :dbexists
if not %DBEXISTS%==0 call :createdb

for %%T in (%TABLES%) do call :process sql/%DBTYPE%/create/%%T-create.sql "create %%T" %DB%
for %%T in (%TABLES%) do call :process sql/data/%%T.sql "data %%T" %DB%
for %%T in (%TABLES%) do call :process sql/%DBTYPE%/index/%%T-index.sql "index %%T" %DB% --force
goto :eof

:process
setlocal
echo process %2
if not exist %1 goto :processerror
echo 	sql %1
sqlite3 -init %1 %DB% .quit
goto :processend
:processerror
echo	sql file %1 does not exist
:processend
endlocal
goto :eof

:dbexists
setlocal
set RESULT=1
if exist %DB% set RESULT=0
endlocal & set DBEXISTS=%RESULT% 
goto :eof

:deletedb
setlocal
echo delete %DB%
if exist %DB% (del /f %DB%)
endlocal
goto :eof

:createdb
setlocal
echo create %DB%
copy NUL %DB%
endlocal
goto :eof

:eof
