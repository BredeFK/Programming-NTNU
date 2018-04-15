# OBLIG 3
## IMT2282 - Operativsystemer
- myprocinfo.ps1 **11.7 B**
- procmi.ps1 **12.4 D**
- chromemajPF.ps1 **13.9 B**

## OWNER
- Brede Fritjof Klausen
- bredefk@stud.ntnu.no

## Quality check
### What
```powershell
  Invoke-ScriptAnalyzer -Path .\myprocinfo.ps1
  Invoke-ScriptAnalyzer -Path .\procmi.ps1
  Invoke-ScriptAnalyzer -Path .\chromemajPF.ps1
```

### How
```powershell
  Install-Module -Name PSScriptAnalyzer # In powershell as admin
  .\myprocinfo.ps1
  .\procmi.ps1
  .\chromemajPF.ps1
```

For å teste filene trenger man PSScriptAnalyzer. Filene må kjøres i Powershell, også når de testes.
## SOURCES
- https://ss64.com/ps/
- https://www.ascii-codes.com/cp865.html
- https://kevinmarquette.github.io/2018-01-12-Powershell-switch-statement/
- https://stackoverflow.com/questions/11876901/how-to-break-lines-in-powershell
- http://www.computerperformance.co.uk/powershell/powershell_converttodatetime.htm
- http://use-powershell.blogspot.no/2015/08/converting-ascii-codes-to-characters.html
- https://stackoverflow.com/questions/49287595/how-to-easier-manipulate-timespan-in-powershell
- https://stackoverflow.com/questions/2085744/how-to-get-current-username-in-windows-powershell
- https://stackoverflow.com/questions/817198/how-can-i-get-the-current-powershell-executing-file
- https://mathieubuisson.github.io/cpu-time-threads-status-powershell/
- https://stackoverflow.com/questions/10928030/in-powershell-how-can-i-test-if-a-variable-holds-a-numeric-value
- http://babblecode.blogspot.no/2012/03/powershell-check-if-process-is-running.html
- https://www.petri.com/display-memory-usage-powershell
