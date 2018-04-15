// 5.6, Task A
//
// Brede Fritjof Klausen
// bredefk@stud.ntnu.no
// 16HBPROGA

// Include
#include <stdio.h>	// printf
#include <stdlib.h>	// exit
#include <unistd.h>	// fork
#include <sys/wait.h> 	// waitpid
#include <sys/types.h> 	// pid_t
#include <pthread.h>	// pthread_create, pthread_join
#include <semaphore.h>	// sem_t, sem_init

// Define
#define SHARED 0
#define SIZE 6

// Variable
sem_t sem[SIZE];

// En datastruktur til å gi info til trådene
struct threadargs {
	int id;         // Trådens ID
	int sec;        // Hvor lenge skal tråden kjøres
	int signal[SIZE];  // Hvem skal tråden gi signal til når den er ferdig?
};

void* tfunc(void *arg) {
	struct threadargs *targs=arg;

	sem_wait(&sem[targs->id]);

	// Print at tråden starter
	printf("Thread %d kjører\n", targs->id);
	sleep(targs->sec);
	printf("Thread %d kjørte i %d sekunder\n", targs->id, targs->sec);

	for(int i = 0; i < SIZE; i++){
		if(targs->signal[i] == 1)
			sem_post(&sem[i]);
	}

	// No warnings with this here :D
	pthread_exit(NULL);
}

int main(void) {
	int i, j, value;                    // Tellere til løkker
	struct threadargs* targs[SIZE]; // Pekere til argumentene vi gir til trådene.
	pthread_t tid[SIZE];            // Variabler til å holde trådinformasjon

	// Initialiser argumentene vi gir til trådene.
	for(j = 0; j < SIZE; j++) {
		targs[j] = malloc(sizeof(struct threadargs)); // Sett av minne, og
		targs[j]->id = 0;                             // sett alle variablene
		targs[j]->sec = 0;                            // til 0.
		for(i = 0; i < SIZE; i++)
			targs[j]->signal[i] = 0;
	}


	// Set value that can't be in for-loop
	// T0
	targs[0]->sec = 1;
	targs[0]->signal[1] = 1;
	targs[0]->signal[4] = 1;

	// T1
	targs[1]->sec = 2;
	targs[1]->signal[3] = 1;

	// T2
	targs[2]->sec = 3;
	targs[2]->signal[3] = 1;

	// T3
	targs[3]->sec = 2;

	// T4
	targs[4]->sec = 3;
	targs[4]->signal[5] = 1;

	// T5
	targs[5]->sec = 3;

	printf("Starting\n");
	// Set value, initalise and start threads
	for(int i = 0; i < SIZE; i++){

		// Give id to thread
		targs[i]->id = i;

		// Value is 0 if i is not 0 or 2
		value = 0;

		// Only T0 and T2 have 1 as value
		if(i == 0 || i == 2) value = 1;

		// Initialise semaphore Ti
		sem_init(&sem[i], SHARED, value);

		// Start Ti
		pthread_create(&tid[i], NULL, tfunc, targs[i]);
	}

	// Wait for all threads
	for(int i = 0; i < SIZE; i++){
		pthread_join(tid[i], NULL);
	}

	printf("Finished!\n");

	return 0;

}
