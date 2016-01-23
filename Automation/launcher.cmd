::
:: Example setup for non-maven project
::

:: System Properties related to initial setup
set SETUP=-Denv.config=resources/config/config.xml -Denv.basePath=resources/data/ -Denv.log.prop=resources/config/logger.properties -Denv.translations=resources/config/translations.xml

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
::set LIBARY=target/classes;target/*
set LIBARY=Sample.jar

:: Database variables
set DB=-Ddatabase.server=dbServer -Ddatabase.name=master -Ddatabase.user=qa -Ddatabase.password=something -Ddatabase.encodedPassword=true

::
:: Disable SNI to bypass exception: javax.net.ssl.SSLProtocolException: handshake alert:  unrecognized_name
::
set SNI=-Djsse.enableSNIExtension=false

::
:: Execute Tests
:: Notes: 
:: 1)  When using a JAR file the classpath variable is ignored by Java. It also seems to ignore the other properties being set.
:: 2)  When creating the Runnable JAR file it was necessary to use the option 'Extract required libraries into generated JAR'
::
java -jar Sample.jar %SNI% %SETUP% %REMOTE% %TIMEOUTS% %URLS% %DB% %CONTEXT% com.automation.ui.common.utilities.Launcher

pause