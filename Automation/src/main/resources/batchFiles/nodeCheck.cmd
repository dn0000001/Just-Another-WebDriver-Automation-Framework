echo off
del exitcode.txt
del processcomplete.txt

:: Note:  For console output use java -jar

:: 
:: Only check for sessions
:: nodeCheck.jar http://127.0.0.1:5555/wd/hub/sessions
::

::
:: Check for and remove any sessions with no window handles
::
nodeCheck.jar http://127.0.0.1:5555/wd/hub/sessions http://127.0.0.1:5555/wd/hub/session

if %errorlevel% EQU 0 (killProcess.cmd)
if %errorlevel% NEQ 0 (echo %errorlevel% > exitcode.txt)
