set psexec=C:\Grid\PsExec.exe
"%psexec%" \\%1 -u TEST\user1 -p password -h schtasks /run /tn cleanup

:: PsExec seems to hang sometimes
taskkill /F /FI "IMAGENAME eq PsExec.exe"
