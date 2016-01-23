:: System Properties related to initial setup
set SETUP=-Denv.config=src/main/resources/config/config.xml -Denv.basePath=src/main/resources/data/ -Denv.log.prop=src/main/resources/config/logger.properties -Denv.translations=src/main/resources/config/translations.xml

:: System Properties for Grid use
set REMOTE=-Dgrid.hub=http://test:4444/wd/hub -Dgrid.browser.platform=WINDOWS -Dbrowser.name=FF

:: System Properties for Local use
set LOCAL=-Dbrowser.name=FF -Dcontext.2.env.driverPath=C:/_Libraries/WebDriver/chromedriver.exe

:: Change some times to use non-default values in the primary context
set TIMEOUTS=-Ddelay.element.timeout=60 -Ddelay.max.timeout=15

:: System Properties for the primary URL
set URLS=-Durl=https://test.com

:: System Properties for the contexts
set CONTEXT=-Dcontext.2=1 -Dcontext.2.url=http://www.tsn.ca -Dcontext.2.browser.name=C

:: Classpath to be used
:: NOTE: LIBARY needs to contain all the JAR files need
set LIBARY=target/classes;target/*

:: Database variables
set DB=-Ddatabase.server=dbServer -Ddatabase.name=master -Ddatabase.user=qa -Ddatabase.password=something -Ddatabase.encodedPassword=true

::
:: Disable SNI to bypass exception: javax.net.ssl.SSLProtocolException: handshake alert:  unrecognized_name
::
set SNI=-Djsse.enableSNIExtension=false

::
:: Execute Tests
::
java -classpath "%LIBARY%" %SNI% -Dprocesses.classpath="%LIBARY%" -Dprocesses.testng.output=../output/  -Denv.log.folderFile=../output/ -Dprocesses.config=src/main/resources/config/executeTests.xml -Dprocesses.max=5 -Dprocesses.start.delay=1000 -Dprocesses.poll.interval=30000 %SETUP% %REMOTE% %TIMEOUTS% %URLS% %DB% %CONTEXT% com.automation.ui.common.utilities.Launcher

pause