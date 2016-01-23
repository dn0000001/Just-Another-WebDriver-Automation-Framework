:: System Properties related to initial setup
set SETUP=-Denv.config=src/main/resources/config/config.xml -Denv.basePath=src/main/resources/data/ -Denv.log.prop=src/main/resources/config/logger.properties -Denv.translations=src/main/resources/config/translations.xml

:: System Properties for Grid use
set REMOTE=-Dgrid.hub=http://127.0.0.1:4444/wd/hub -Dgrid.browser.platform=WINDOWS -Dbrowser.name=FF

:: Change some times to use non-default values
set TIMEOUTS=-Ddelay.element.timeout=60 -Ddelay.max.timeout=15

:: System Properties for the primary URL
set URLS=-Durl=http://www.google.com

:: System Properties for the contexts
set CONTEXT=-Dcontext.2=1 -Dcontext.2.url=http://www.tsn.ca

:: Classpath to be used
:: NOTE: lib needs to contain all the JAR files need
set LIBARY=target/classes;../lib/*

:: Database variables
set DB=-Ddatabase.server=dbServer -Ddatabase.name=master -Ddatabase.user=qa -Ddatabase.password=something -Ddatabase.encodedPassword=true

::
:: Disable SNI to bypass exception: javax.net.ssl.SSLProtocolException: handshake alert:  unrecognized_name
::
set SNI=-Djsse.enableSNIExtension=false

::
:: Setup report folder, log folder and TestNG XML for each test
::
set REPORT001=../output/Report1
set LOG001=-Denv.log.folderFile=../output/Report1/
set TEST001=src/main/resources/test_suites/testng_Test1.xml

set REPORT002=../output/Report2
set LOG002=-Denv.log.folderFile=../output/Report2/
set TEST002=src/main/resources/test_suites/testng_Test2.xml

::
:: Rename output folder for this run of results
::
::ren ..\regression "regression_%time:~0,2%%time:~3,2%-%DATE:/=%"

::
:: Execute Tests
::
start "Test1" java -classpath "%LIBARY%" %SNI% %LOG001% %SETUP% %REMOTE% %TIMEOUTS% %URLS% %DB% %CONTEXT% org.testng.TestNG -d %REPORT001% "%TEST001%"
start "Test2" java -classpath "%LIBARY%" %SNI% %LOG002% %SETUP% %REMOTE% %TIMEOUTS% %URLS% %DB% %CONTEXT% org.testng.TestNG -d %REPORT002% "%TEST002%"
