package Client;

import Misc.Message;
import Misc.Transfer;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import sun.jvm.hotspot.runtime.Threads;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

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

    // private ConcurrentLinkedQueue<>

    public AsyncClient(String host, int port) {
        this(host, port, true);
    }

    public AsyncClient(String host, int port, boolean runInSeparateThread) {
        this.host = host;
        this.port = port;

        try {
            Socket socket = new Socket(host, port);

            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());

            if (runInSeparateThread) {
                Thread thread = new Thread(new ConnectionHandler());
                thread.setDaemon(true);
                thread.start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server is not available: try to connect later");
        }
    }

    private class ConnectionHandler implements Runnable {

        @Override
        public void run() {

            boolean done = false;

            while (!done) {

                try {

                    if (inputStream.available() > 0) {

                        Message action = Transfer.answer(inputStream);

                        if (action == Message.PROGRESS) {

                        }
                        else if (action == Message.RESULT) {

                        }
                        else if (action == Message.EXIT) {
                            done = true;
                        }

                    }



                }
                catch (IOException e) {
                    e.printStackTrace();
                    done = true;
                }

            }

            try {
                socket.close();
                inputStream.close();
                outputStream.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
