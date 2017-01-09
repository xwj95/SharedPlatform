set userName=%1
set directory=sdcard/ExpData
adb pull %directory%/%userName%
if not exist "data" (
	mkdir data
)
if not exist "data\%userName%" (
	mkdir data\%userName%
)
xcopy /E /Y %userName% data\%userName%
rd /S /Q %userName%
