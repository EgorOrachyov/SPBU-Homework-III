package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Async Client working in the daemon thread
 * Allows to send task in some king of task queue,
 * Send images for filtering, get filtered images,
 * monitor current progress in filtering and so on...
 *
 * @note Should be independent for any king of UI
 */
public class AsyncClient {

    private final String host;
    private final int port;

    private Socket socket;

    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    public AsyncClient(String host, int port) {
        this.host = host;
        this.port = port;

        try {
            Socket socket = new Socket(host, port);

        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server is not available: try to connect later");
        }
    }

    private class ConnectionHandler implements Runnable {

        @Override
        public void run() {

        }
    }

}
