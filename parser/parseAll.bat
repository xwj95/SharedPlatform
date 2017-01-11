set /a "x = 1"
:while1
	set directory=%1\task%x%
	if exist %directory% (
		parse %directory%
		set /a "x = x + 1"
		goto :while1
	)
