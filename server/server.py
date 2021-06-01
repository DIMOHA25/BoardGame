import socket
import helpers

# localhost будет изменен на адрес контейнера после деплоя
ADDRESS, PORT = 'localhost', 8080

with socket.create_server((ADDRESS, PORT)) as server:
    server.listen()

    waitroom = []
    while True:
        connection, address = server.accept()
        connection.settimeout(0.5)
        waitroom.append(connection)

        if len(waitroom) >= 2:
            helpers.create_new_session(waitroom[:2])
            waitroom = waitroom[2:]