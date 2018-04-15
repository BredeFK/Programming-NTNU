// 4.5, Task B
//
// Brede Fritjof Klausen
// bredefk@stud.ntnu.no
// 16HBPROGA

#include <stdio.h>	// printf
#include <unistd.h>	// fork
#include <sys/wait.h> 	// waitpid
#include <sys/types.h> 	// pid_t

void process(int number, int time) { //Funksjon hentet fra oppgave tekst
	printf("Prosess %d kjører\n", number);
	sleep(time);
	printf("Prosess %d kjørte i %d sekunder\n", number, time);
}

int main(){

	pid_t pid = fork();

	if(pid == 0){
		process(0, 1);	// pid == 0, Start prosess 0 (1s)
		pid = fork();	// fork: pid = 0
		if(pid == 0){
			process(1, 2); // prosess 0 er ferdig, Start prosess 1 (2s)
		}
		else{
			process(4, 3); // prosess 0 er ferdig og pid != 0, Start prosess 4 (3s)
			process(5, 3); // prosess 4 er ferdig, Start prosess 5 (3s)
			waitpid(pid, NULL, 0);
		}
	}
	else{
		process(2, 3);	// pid != 0, Start prosess 2 (3s)
		process(3, 2);	// prosess 2 (og 1) er ferdig, Start prosess 3 (2s)
		waitpid(pid, NULL, 0);
	}

	return 0;
}
