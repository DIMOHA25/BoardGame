package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

/** 
 * Класс клиента, экземпляры которого подключаются к
 * сокет серверу и могут отпправлять и получать инфу 
 * от него.
 */
public class Client {
    // localhost будет заменён на ip контейнера после деплоя
    private static final String ADDRESS = "localhost", SEPERATOR = "; ";
    private static final int PORT = 8080;

    // Создаем экземпляры сокет-клиента и потоков получения и отправки инфы
    private Socket connection;
    private DataInputStream reader;
    private DataOutputStream sender;

    private String readMessage() throws IOException {
        try {
            return reader.readUTF();
        } catch (SocketTimeoutException e) {
            return null;
        }
    }
    
    public Client() throws IOException {
        connection = new Socket(ADDRESS, PORT);
        connection.setSoTimeout(500);

        reader = new DataInputStream(connection.getInputStream());
        sender = new DataOutputStream(connection.getOutputStream());
    }

    /**
     * @param message сообщение, которое отправляется на сокет
     * @throws IOException 
     */
    public void send(String message) throws IOException {
        sender.writeUTF(message);
        sender.flush();
    }

    /**
     * @return Возвращает список всех сообщений, отправленных другим клиентом; 
     * null, если их нет
     * @throws IOException
     */
    public ArrayList<String> recieve() throws IOException {
        String message = readMessage();        
        if (message == null) return null;

        ArrayList<String> result = new ArrayList<String>(
            Arrays.asList(
                message.split(SEPERATOR)
            )
        ); 

        return result;
    }

    public void close() throws IOException {
        reader.close();
        sender.close();
        connection.close();
    }
}