@echo off
rem bbou@ac-toulouse.fr
rem 03/12/2021 

set /P DB=Enter database name:

set DBTYPE=sqlite
set TABLES=annosets coretypes corpuses cxns documents fegrouprealizations ferealizations ferealizations_valenceunits fes_excluded fes_fegrouprealizations fes_required fes_semtypes fes fetypes framerelations frames_related frames_semtypes frames gftypes governors_annosets governors labelitypes labels labeltypes layers layertypes lexemes lexunits_governors lexunits_semtypes lexunits grouppatterns grouppatterns_annosets grouppatterns_patterns poses pttypes semtypes semtypes_supers sentences subcorpuses_sentences subcorpuses valenceunits_annosets valenceunits words

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
