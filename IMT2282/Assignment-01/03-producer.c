// file:  03-producer.c
// name:  Brede Fritjof Klausen
// class: 16HBPROGA

// Include
#include <stdio.h>	// printf
#include <stdlib.h>	// atoi, rand, exit
#include <unistd.h>     // usleep
#include <pthread.h>	// pthread_create, pthread_join
#include <semaphore.h>	// sem_t, sem_init, sem_wait, sem_sem_post

// Define
#define SHARED 0        // process-sharing if !=0, thread-sharing if =0
#define BUF_SIZE 10
#define MAX_MOD 100000
#define NUM_ITER 200

// Functions
void *Producer(void *arg); // Producer thread
void *Consumer(void *arg); // Consumer thread

// Variables
sem_t empty;            // empty: How many empty buffer slots
sem_t full;             // full: How many full buffer slots
sem_t b;                // b: binary, used as a mutex
int g_data[BUF_SIZE];   // shared finite buffer
int g_idx;              /* index to next available slot in buffer,
                           remember that globals are set to zero
                           according to C standard, so no init needed  */

int main(int a, char* argv[]) {

	if(a  != 2) {
		printf("No number detected!\n");
		return 1;
	}
	// Takes user input to n
	int n = atoi(argv[1]);

	pthread_t pid[n], cid[n];

	// Initialie the semaphores
	sem_init(&empty, SHARED, BUF_SIZE);
	sem_init(&full, SHARED, 0);
	sem_init(&b, SHARED, 1);

	if(n > 0){
		// Message start
		printf("> main started\n");

		// Create n threads
		for(intptr_t i = 0; i < n; i++){
			pthread_create(&pid[i], NULL, Producer, (void *) i);
			pthread_create(&cid[i], NULL, Consumer, (void *) i);
		}

		// And wait for them to finish.
		for(int i = 0; i < n; i++){
			pthread_join(pid[i], NULL);
			pthread_join(cid[i], NULL);
		}

		// Message stop
		printf("> main finished\n");
	}
	else{
		printf("Number can not be 0 or under!\n");
	}
	return 0;
}


void *Producer(void *arg) {
	int number = (intptr_t) arg + 1;
	int i=0, j;

	while(i < NUM_ITER) {
		// pretend to generate an item by a random wait
		usleep(rand() % MAX_MOD);

		// Wait for at least one empty slot
		sem_wait(&empty);
		// Wait for exclusive access to the buffer
		sem_wait(&b);

		// Check if there is content there already. If so, print a warning and exit.
		if(g_data[g_idx] == 1) {
			printf("Producer(%d) overwrites!, idx er %d\n",number ,g_idx);
			exit(0);
		}

		// Fill buffer with "data" (ie: 1) and increase the index.
		g_data[g_idx]=1;
		g_idx++;

		// Print buffer status.
		j=0; printf("(Producer(%d), idx is %d): ",number ,g_idx);
		while(j < g_idx) { j++; printf("="); } printf("\n");

		// Leave region with exlusive access
		sem_post(&b);
		// Increase the counter of full bufferslots.
		sem_post(&full);

		i++;
	}

	return 0;
}


void *Consumer(void *arg) {
	int number = (intptr_t) arg + 1;
	int i=0, j;

	while(i < NUM_ITER) {
		// Wait a random amount of time, simulating consuming of an item.
		usleep(rand()%MAX_MOD);

		// Wait for at least one slot to be full
		sem_wait(&full);
		// Wait for exclusive access to the buffers
		sem_wait(&b);

		// Checkt that the buffer actually contains some data at the current slot.
		if(g_data[g_idx-1] == 0) {
			printf("Consumes(%d) nothing!, idx er %d\n",number, g_idx);
			exit(0);
		}

		// Remove the data from the buffer (ie: Set it to 0)
		g_data[g_idx-1]=0;
		g_idx--;

		// Print the current buffer status
		j=0; printf("(Consumer(%d), idx is %d): ",number ,g_idx);
		while(j < g_idx) { j++; printf("="); } printf("\n");

		// Leave region with exclusive access
		sem_post(&b);
		// Increase the counter of empty slots.
		sem_post(&empty);

		i++;
	}

	return 0;
}
