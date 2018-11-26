#include <stdio.h>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>

int main(int argc, char **argv) {
  struct sockaddr_in myaddr;
  int fd;
  unsigned int alen;  // length of the address

  // Create the socket
  if ((fd = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
    perror("Failed to create a socket.");
    return 1;
  }

  printf("Created socket with descriptor=%d.\n", fd);

  // Bind socket to arbitrary (since this is the client) return address
  memset((char *)&myaddr, 0, sizeof(myaddr)); // Fill with 0s, since we don't care for client yet
  myaddr.sin_family = AF_INET;
  myaddr.sin_addr.s_addr = htonl(INADDR_ANY); // htonl converts a long integer (i.e. the address) from host- to network-byte order
  myaddr.sin_port = htons(0); // htons does the same as htonl but for hostshort

  if (bind(fd, (struct sockaddr *)&myaddr, sizeof(myaddr)) < 0) {
    perror("Failed to bind.");
    return 1;
  }

  alen = sizeof(myaddr);
  if (getsockname(fd, (struct sockaddr *)&myaddr, &alen) < 0) {
    perror("Failed to getsockname.");
    return 1;
  }

  printf("Bind complete. Port number = %d\n", ntohs(myaddr.sin_port));
  return 0;
}
