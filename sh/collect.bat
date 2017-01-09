userName=%1
set directory=sdcard\ExpData
adb pull %directory%\%userName%
if not exist "data\%userName%" (
	mkdir data\${userName}
)
xcopy /e %userName%\ data\%userName%
rd /r /q %userName%\
