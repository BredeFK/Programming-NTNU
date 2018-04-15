#!/bin/bash
# file:   procmi.bash
# name:   Brede Fritjof Klausen
# class:  16HBPROGA

# Check syntax
bash -n "$(basename "$0")"

# Static code analyze
shellcheck "$(basename "$0")"

# Added this for more user friendly text :)
RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m'

# Writes info
# Parameters: id, vmSize, VmData+VmStk+VmExe, VmLib, VmRSS, VmPTE, file
# Parameters: $1, $2    , $3                , $4   , $5   , $6   , $7
function writeInfo() {
	{
		echo "******** Minne info om prosess med PID $1 ********"
		echo "Total bruk av virtuelt minne (VmSize): $2KB"
		echo "Mengde privat virtuelt minne (VmData+VmStk+VmExe): $3KB"
		echo "Mengde shared virtuelt minne (VmLib): $4KB"
		echo "Total bruk av fysisk minne (VmRSS): $5KB"
		echo "Mengde fysisk minne som benyttes til page table (VmPTE): $6KB"
	} >> "$7"
}

# Grep things from /proc/<id>/status
# Parameters: whatToGrep, id
# Parameters: $1        , $2
function grepStuff() {
	(grep "$1" /proc/"$2"/status | awk '{ printf $2 }')
}

# Check if $1 is not empty
if [ ! -z "$1" ]; then
	# for every pid
	for i in "$@"; do
		# Check if pid is valid
		if [ -f /proc/"$i"/status ]; then

			# Generate filename
			file="$i-$(date +"%Y%m%d-%H:%M:%S").meminfo"

			# Get VmSize
			vmSize=$(grepStuff "VmSize" "$i")

			# Get VmData+VmStk+VmExe
			vmData=$(grepStuff "VmData" "$1")
			vmStk=$(grepStuff "VmStk" "$1")
			vmExe=$(grepStuff "VmExe" "$1")
			let result=$vmData+$vmStk+$vmExe

			# Get VmLib
			vmLib=$(grepStuff "VmLib" "$i")

			# Get VmRSS
			vmRSS=$(grepStuff "VmRSS" "$i")

			# VmPTE
			vmPTE=$(grepStuff "VmPTE" "$i")

			# Put info in new file
			writeInfo "$i" "$vmSize" "$result" "$vmLib" "$vmRSS" "$vmPTE" "$file"

			# Confirm that file is made
			echo -e "${GREEN}Fil med navn '$file' oprettet!${NC}"
		else
			# Error
			echo -e "${RED}PID '$i' eksisterer ikke!${NC}"
		fi
	done
else
	# Error
	echo -e "${RED}Må ha parameter!${NC}"
fi
