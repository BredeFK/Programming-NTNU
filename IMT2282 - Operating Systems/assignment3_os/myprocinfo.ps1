# file:   myprocinfo.ps1
# name:   Brede Fritjof Klausen
# class:  16HBPROGA

function menu {
  Write-Output "1 - Hvem er jeg og hva er navnet paa dette scriptet?"
  Write-Output "2 - Hvor lenge er det siden siste boot?"
  Write-Output "3 - Hvor mange prosesser og traader finnes?"
  Write-Output "4 - Hvor mange context switch'er fant sted siste sekund?"
  Write-Output "5 - Hvor stor andel av CPU-tiden ble benyttet i kernelmode og i usermode siste sekund?"
  Write-Output "6 - Hvor mange interrupts fant sted siste sekund?"
  Write-Output "9 - Avslutt dette scriptet"
}

# one: User and script name
function one {
  $script = $(split-path $MyInvocation.PSCommandPath -Leaf)
  Write-Output "Du er $($env:UserName), og kjoerer scriptet: $script"
}

# two: Time since last boot
function two {
  $bootDate = (Get-CimInstance Win32_OperatingSystem).LastBootUpTime
  $bootTime = $(Get-Date).Subtract($bootDate)
  $time = ""

  # -ge 1 instead of -ne 0, because 1 minute is 0.000694444444444444 days
  if($bootTime.TotalDays -ge 1)    { $time += "$($bootTime.Days) Days, "       }
  if($bootTime.TotalHours -ge 1)   { $time += "$($bootTime.Hours) Hours, "     }
  if($bootTime.TotalMinutes -ge 1) { $time += "$($bootTime.Minutes) Minutes, " }

  Write-Output "Tid siden siste boot: $time$($bootTime.Seconds) Seconds"
  Write-Output "Dato og klokkeslett:  $($bootDate.DateTime)"
}

# three: Number of proceses and threads that exists
function three {
  Write-Output "Antall prosesser: $((Get-Process | Select-Object -ExpandProperty Id).Count)"
  Write-Output "Antall traader:   $((Get-Process | Select-Object -ExpandProperty Threads).Count)"
}

# four: How many context switches was there, the last second
function four {
  Write-Output "Vent 1 sekund..."
  $contextSwitches=((Get-Counter "\System\Context Switches/sec").CounterSamples).CookedValue
  Clear-Host
  Write-Output "Antall context switches: $contextSwitches"
}

# five: How much cpu time in kernelmode and usermode, the last second
function five {
  Write-Output "Vent 1 skund..."

  $kernelMode = ((Get-Counter -counter "\Processor(_total)\% privileged time").CounterSamples).CookedValue
  $userMode = ((Get-Counter -counter "\Processor(_total)\% user time").CounterSamples).CookedValue
  Clear-Host
  Write-Output "Andel CPU tid brukt i Kernel mode $kernelMode og User mode $userMode"
}

# six: How many interrupts, the last second
function six {
  Write-Output "Vent 1 sekund..."
  $interrupts=((Get-Counter "\Processor(_total)\Interrupts/sec").CounterSamples).CookedValue
  Clear-Host
  Write-Output "Antall interrupts: $interrupts"
}

menu
$choice = read-host "Velg en funksjon"
while ($choice -ne 9){
  switch ($choice){
    1 { Clear-Host; one }
    2 { Clear-Host; two }
    3 { Clear-Host; three }
    4 { Clear-Host; four }
    5 { Clear-Host; five }
    6 { Clear-Host; six }
    default { Clear-Host; "Ikke gyldig nummer!" }
  }
  Write-Output ""; menu
  $choice = read-host "Velg en funksjon"
}
Clear-Host
Write-Output "Morna Jens!`n"
exit
