echo off
del exitCodeJobsCheck.txt
java -jar noJobsRunning.jar http://172.26.156.203:8080/view/Automation/api/json 1
echo %errorlevel% > exitCodeJobsCheck.txt