#!/bin/bash
# file:   myprocinfo.bash
# name:   Brede Fritjof Klausen
# class:  16HBPROGA

# Check syntax
bash -n "$(basename "$0")"

# Static code analyze
shellcheck "$(basename "$0")"

# Added this for more user friendly text :)
BLUE='\033[0;34m'
RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m'

# Write menu
function menu {
	echo "1 - Hvem er jeg og hva er navnet på dette scriptet?"
	echo "2 - Hvor lenge er det siden siste boot?"
	echo "3 - Hvor mange prosesser og tråder finnes?"
	echo "4 - Hvor mange context switch'er fant sted siste sekund?"
	echo "5 - Hvor stor andel av CPU-tiden ble benyttet i kernelmode og i usermode siste sekund?"
	echo "6 - Hvor mange interrupts fant sted siste sekund?"
	echo "9 - Avslutt dette scriptet"
	read -p "Velg en funksjon: " choice
}

function one {
        echo -e "${BLUE}Du er $USER, og kjører scriptet: $(basename "$0")${NC}"
}

function two {
	echo -e "${BLUE}Tid siden siste boot:$(uptime -p | awk '{ $1=""; print $0 }')"
	echo -e "Dato og klokkeslett:  $(who -b | awk '{ print $3, $4 }')${NC}"
}

function three {
	# Process status, (e)all processes, (L)threads, (f)full format listing
	# Word count of newlines
	echo -e "${BLUE}Antall prosesser: $(ps -ef --noheader | wc -l)"
	echo -e "Antall tråder:    $(ps -eLf --noheader | wc -l)${NC}"
}

function four {
        echo -e "${BLUE}Vent..."
	first=$(grep ctxt /proc/stat | awk '{ print $2 }')
	sleep 1
	second=$(grep ctxt /proc/stat | awk '{ print $2 }')
	let diff=second-first;
	echo -e "Antall context switch'er siste sekund: $diff${NC}"
}

function five {
        echo -e "${BLUE}Vent..."
	usrArr=$(grep cpu /proc/stat | awk 'NR==1 { print $2, $3 }')
	krl=$(grep cpu /proc/stat | awk 'NR==1 { print $4 }')
	sleep 1
	usrArr2=$(grep cpu /proc/stat | awk 'NR==1 { print $2, $3 }')
	krl2=$(grep cpu /proc/stat | awk 'NR==1 { print $4 }')

	first=second=0

	for i in ${usrArr[*]}; do
	let first+=i
	done

	for j in ${usrArr2[*]}; do
	let second+=j
	done

	let usrAfter=second-first
	let krlAfter=krl2-krl

	echo -e "Andel CPU tid i usermode:   $usrAfter"
	echo -e "Andel CPU tid i kernelmode: $krlAfter${NC}"
}

function six {
        echo -e "${BLUE}Vent..."
	first="$(grep intr /proc/stat | awk ' {print $2} ')"
	sleep 1
	second="$(grep intr /proc/stat | awk ' {print $2} ')"
	let var=second-first
	echo -e "Antall interrupts siste sekund: $var${NC}"
}



menu
while (("$choice" != "9")); do
	case "$choice" in
		"1") one   ;;
		"2") two   ;;
		"3") three ;;
		"4") four  ;;
		"5") five  ;;
		"6") six   ;;
		*) echo -e  "${RED}Ikke gyldig nummer!${NC}";;
	esac
	printf "\n"; menu
done

echo -e "${GREEN}Hadet!\n${NC}"
