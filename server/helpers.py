import queue
import threading
import socket
import threads

def create_new_session(connections):
    q = queue.Queue(maxsize=0)
    for connection in connections:
        client_thread = threading.Thread(target=threads.endpoint, args=(connection, q))
        client_thread.start()

def recieve_message(conn):
    try:
        message_length = int.from_bytes(conn.recv(2), byteorder='big')
        message = conn.recv(message_length).decode('utf-8')
    except socket.timeout:
        return None

    return message

def send_message(conn, message):
    to_send = message.encode('utf-8')
    conn.send(len(to_send).to_bytes(2, byteorder='big'))
    conn.send(to_send)