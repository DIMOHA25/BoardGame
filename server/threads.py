import queue
import time
import helpers

SEPERATOR = '; '

def endpoint(conn, q):
    with conn:
        while True:
            try:
                mess, ident = q.get(block=False)

                if ident == id(conn):
                    q.put((mess, ident))
                    time.sleep(0.1)
                    continue

                if mess == '__close_connection':
                    return
        
                try:
                    helpers.send_message(conn, mess + SEPERATOR)
                except ConnectionAbortedError as err:
                    return
            except queue.Empty:
                pass

            try:
                message = helpers.recieve_message(conn)
            except ConnectionResetError as err:
                return

            if message is not None:
                q.put((message, id(conn)))