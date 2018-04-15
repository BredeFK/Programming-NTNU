# file:   procmi.ps1
# name:   Brede Fritjof Klausen
# class:  16HBPROGA

function convert($byte){
  if ($byte -ge 1gb)     { $byte = "$([math]::Round($byte/1gb, 2))GB" }
  elseif ($byte -ge 1mb) { $byte = "$([math]::Round($byte/1mb, 2))MB" }
  else                   { $byte = "$([math]::Round($byte/1kb, 2))KB" }

  return $byte
}

function skriv($validPID){
  $vms = convert((Get-Process -Id $validPID | Select-Object -ExpandProperty VirtualMemorySize))
  $workingSet = convert((Get-Process -Id $validPID | Select-Object -ExpandProperty Workingset))
  $date =  (Get-Date -Format ddMMyyyy-HHmmss)
  $filename = "$validPID-$date.meminfo"

  $content += "******** Minne info om prosess med PID $validPID ********"
  $content += "`nTotal bruk av virtuelt minne: $vms"
  $content += "`nStoerrelse paa Working Set:   $workingSet"

  $content >> $filename
  if ($filename){
    Write-Output "Fil laget for PID:    $validPID"
  }
  else {
    Write-Output "Uventet feil for PID: $validPID"
  }
}

# If there are any arguments
if ($args.Count -gt 0){
  Foreach ($id in $args)
  {
    # If id is an int
    if ($id -is [int]){
      $check = (Get-Process -Id $id -ErrorAction SilentlyContinue)

      # If id is an actual PID
      if ($null -ne $check){
        skriv($id)
      } else {
        Write-Output "Finnes ingen PID:     $id "
      }
    }
  }
}
else{
  Write-Output "Kan ikke ha 0 argumenter"
}
