echo off
del exitCodeJobsCheck.txt
java -jar noJobsRunning.jar http://172.26.156.203:8080/view/Automation/api/json 1 >> runningJobsCheck.txt
if %errorlevel% NEQ 0 (echo %errorlevel% > exitCodeJobsCheck.txt)
if %errorlevel% EQU 0 (SessionClient.cmd < cleanSessionInput.txt > cleanSessionOutput.txt)
