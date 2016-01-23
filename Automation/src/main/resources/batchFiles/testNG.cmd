:: Run from directory.
:: Needs to contain the file logger.properties for log4j
set working=C:\workspace\Common

:: libraries used
set log4j=C:\_Libraries\apache-log4j-1.2.16\log4j-1.2.16.jar
set cobra=C:\_Libraries\cobra-0.98.4\cobra.jar
set mail=C:\_Libraries\javamail-1.4.4\mail.jar
set opencsv=C:\_Libraries\opencsv\opencsv-2.3.jar
set selenium=C:\_Libraries\selenium2\selenium-server-standalone-2.23.1.jar
set sql=C:\_Libraries\sqljdbc_3.0\sqljdbc4.jar
set vtd_xml=C:\_Libraries\vtd_xml\vtd-xml.jar

:: File needed in run from directory for SQL access
:: Script will copy file to directory and delete after
set sql_dll=C:\_Libraries\sqljdbc_3.0\auth\x86\sqljdbc_auth.dll

:: Temp file to create in working directory to be read by the application
set TEMP_FILE=~temp.xml

:: class locations
set project1=C:\workspace\Common\bin
set project2=C:\workspace\Utilities\bin

:: call function to set env variables
call :setDateVariables

:: testNG report directory (with current date)
set reports=C:\workspace\Common\test-output\DEBUG\%mm%-%dd%-%yy%
rmdir /s /q "%reports%"
mkdir "%reports%"

:: testNG config xml
set configXML=C:\workspace\Common\data\xml_for_testng\testng_smokeTest.xml

:: Screenshots folder
set screenshots=C:\workspace\Common\test-output\DEBUG\%mm%-%dd%-%yy%\screenshots

:: Delete and Recreate screenshots folder
rmdir /s /q "%screenshots%"
mkdir "%screenshots%"

:: Go to the working directory and run testNG
c:
cd "%working%"

:: write REPORT_FILE to a temp file to be read later by the application to include in e-mail report
echo ^<TOKENS^>^<REPORT_FILE^>\\IN06\test-output\DEBUG\%mm%-%dd%-%yy%\emailable-report.html^</REPORT_FILE^>^<DATE^>%mm%-%dd%-%yy%^</DATE^>^<LOG_FILE^>\\IN06\test-output\DEBUG\%mm%-%dd%-%yy%\results.log^</LOG_FILE^>^</TOKENS^> > "%TEMP_FILE%"

:: Delete any previous failure file before executing tests
del ~fail.txt
del results.log
copy /Y "%sql_dll%" "%working%"
java -classpath "%log4j%;%cobra%;%mail%;%opencsv%;%selenium%;%sql%;%vtd_xml%;%project1%;%project2%" org.testng.TestNG -d "%reports%" "%configXML%"
del sqljdbc_auth.dll
del "%TEMP_FILE%"
copy /Y results.log "%reports%"

:: ~fail.txt needs be created by a Listener class attached to TestNG
IF EXIST ~fail.txt EXIT /B 1

:: *****
:: Functions START
:: *****

:setDateVariables
:: This will return the date into environment variables (dd, mm & yy)
:: 2002-03-20 : Works on any NT/2K/XP machine independent of regional date settings

FOR /f "tokens=1-4 delims=/-. " %%G IN ('date /t') DO (call :s_fixdate %%G %%H %%I %%J)
goto :s_print_the_date

:s_fixdate
if "%1:~0,1%" GTR "9" shift
FOR /f "skip=1 tokens=2-4 delims=(-)" %%G IN ('echo.^|date') DO (
    Set %%G=%1&set %%H=%2&Set %%I=%3)
goto :eof

:s_print_the_date
Echo  Year:[%yy%]  Month:[%mm%]  Day:[%dd%]
SET yy=%yy%&SET mm=%mm%&SET dd=%dd%
goto :eof

:setTimeVariables
:: This will return the time (24hr) into environment variables (hour, minutes & seconds)

for /f "tokens=4 delims=: " %%h in ('now') do set hour=%%h
for /f "tokens=5 delims=: " %%m in ('now') do set minutes=%%m
for /f "tokens=6 delims=: " %%a in ('now') do set seconds=%%a
goto :eof

:s_print_the_time
echo  Hour:[%hour%]  Minutes:[%minutes%]  Seconds:[%seconds%]
goto :eof

:: *****
:: Functions END
:: *****