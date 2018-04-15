#!/bin/bash
# file:   chromemaJPF.bash
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

# Max errors before notification
max="1000"

# Get all Id's
array=$(pgrep chrome)

# For every id, check number of faults (Green if under 1000, red if not)
for id in $array; do
	fault=$(ps --no-headers -o maj_flt "$id")
	if (( fault > max )); then
		echo -e "${RED}Chrome $id har forårsaket $fault major page faults (mer enn 1000!)${NC}"
	else
		echo -e "${GREEN}Chrome $id har forårsaket $fault major page faults${NC}"
	fi
done
