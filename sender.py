#----- A simple TCP client program in Python using send() function -----

import socket
import sys


# Create a client socket

clientSocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)


# Connect to the server

ip = sys.argv[1]
print("\n\nip : ", ip, "\n\n")
clientSocket.connect((ip, 8080))


# Send data to server

data = sys.argv[2]
print("\n\nargs : ", data,"\n\n")

clientSocket.send(data.encode())
