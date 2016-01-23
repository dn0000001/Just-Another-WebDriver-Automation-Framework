echo off

taskkill /F /FI "IMAGENAME eq IEDriverServer*"
taskkill /F /FI "IMAGENAME eq iexplore*"

taskkill /F /FI "IMAGENAME eq chromedriver*"
taskkill /F /FI "IMAGENAME eq chrome.exe"

taskkill /F /FI "IMAGENAME eq firefox.exe"

"C:\Program Files (x86)\Java\jre7\bin\java.exe" -jar C:\Grid\nodeCheck.jar http://127.0.0.1:5555/wd/hub/sessions http://127.0.0.1:5555/wd/hub/session 1 5 >> C:\logs\deleteSessions.log
