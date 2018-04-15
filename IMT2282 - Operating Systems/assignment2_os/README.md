# OBLIG 2 #
## IMT2282 - Operativsystemer ##
- myprocinfo.bash  **7.5 A**
- chromemajPF.bash **8.6 C**
- procmi.bash      **8.6 D**

## OWNER ##
- Brede Fritjof Klausen
- bredefk@stud.ntnu.no

## Quality check ##
### What ###
```bash
bash -n "$(basename "$0")"    # Syntax
shellcheck "$(basename "$0")" # Static Analysis Tool
```
### How ###
```
sudo apt install shellcheck

./myprocinfo.bash
./chromemaJPF.bash
./procmi.bash 1
```
Kun shellcheck må installeres for å gjøre for å utføre samme kvalitessikring som jeg har gjort.
Sjekkene må også gjøres på linux i terminalen. Begge sjekkene er i koden i oppgavene, så man trenger kun å script'a.

## SOURCES ##
- https://stackoverflow.com/questions/2961635/using-awk-to-print-all-columns-from-the-nth-to-the-last/2961994
- https://stackoverflow.com/questions/638975/how-do-i-tell-if-a-regular-file-does-not-exist-in-bash
- https://stackoverflow.com/questions/3236871/how-to-return-a-string-value-from-a-bash-function
- https://stackoverflow.com/questions/5947742/how-to-change-the-output-color-of-echo-in-linux
- https://serverfault.com/questions/7503/how-to-determine-if-a-bash-variable-is-empty
- http://www.linuxhowtos.org/System/procstat.htm
