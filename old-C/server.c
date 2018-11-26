#include <stdio.h>
//#include <stdlib.h>
#include <sys/socket.h> 

#define BUFLEN  1024  // Length of individual message per datagram
#define NUMPACK 2     // How many packets will be sent
#define PORTNO  50000 // Port number to listen to



int main(int argc, char **argv) {
  struct sockaddr_in myaddr;
  
  int s;
  if ((s = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
    perror("Socket cannot be made.");
    return 1;
  }
  printf("Socket made with descriptor=%d\n", s);
  return 0;    
}
