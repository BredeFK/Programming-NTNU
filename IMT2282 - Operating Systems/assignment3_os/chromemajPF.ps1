# file:   chromemajPF.ps1
# name:   Brede Fritjof Klausen
# class:  16HBPROGA
Foreach ($chromeID in $(Get-Process chrome | Select-Object -ExpandProperty Id)){
  Write-Output "chrome $chromeID $((Get-Process -Id $chromeID | Select-Object -ExpandProperty Threads).Count)"
}
