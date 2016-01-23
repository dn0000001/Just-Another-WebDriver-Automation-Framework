OPTION Explicit

Dim startTime, batchFileToRun, applicationToLaunch, arrayStartTime, arrayCurrentTime, startHour, startMinutes, _
	currentHour, currentMinutes, bStop, objApp, checkInterval, startNextDay, nextDay

' Check Interval
checkInterval = 60000

' Start the Next Day at specified time?
startNextDay = False

' Enter start time in 24 hr format
startTime = "17:00"

' Batch file to run
batchFileToRun = "C:\Test\test.cmd"

' Command to run a batch file
applicationToLaunch = "cmd /C " & Chr(34) & batchFileToRun & Chr(34)

' Split Start Time into pieces for comparison
arrayStartTime = Split(startTime, ":", -1, vbTextCompare)
startHour = arrayStartTime(0)
startMinutes = arrayStartTime(1)

' Wait for the next day if option set
If startNextDay Then
	bStop = False
	nextDay = DateAdd("d", 1, Now)
	Do
		If DateDiff("d", Now, nextDay) <= 0 Then
			bStop = True
		Else
			WScript.Sleep checkInterval
		End If
	Loop Until bStop
End If

' Check every 1 minute until time to start
bStop = False
Do
	arrayCurrentTime = Split(FormatDateTime(Now, vbShortTime), ":", -1, vbTextCompare)
	currentHour = arrayCurrentTime(0)
	currentMinutes = arrayCurrentTime(1)
	
	If CInt(currentHour) > CInt(startHour) Then
		' Stop loop and start application
		bStop = True
	ElseIf CInt(currentHour) = CInt(startHour) Then
		If CInt(currentMinutes) >= CInt(startMinutes) Then
			' Stop loop and start application
			bStop = True
		Else
			' Wait for 1 minute
			WScript.Sleep checkInterval
		End If
	Else
		' Wait for 1 minute
		WScript.Sleep checkInterval
	End If
Loop Until bStop

' Launch the application
Set objApp = CreateObject("WScript.Shell")
objApp.Run applicationToLaunch 