package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ScheduledFuture;

public class ConnectionHandler implements Runnable {

    private boolean done = false;

    private Socket socket;
    private Configuration configuration;
    private ScheduledFuture<?> future;

    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    public ConnectionHandler(Socket socket, Configuration configuration) {
        this.socket = socket;
        this.configuration = configuration;

        try {
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
        }
        catch (IOException e) {
            e.printStackTrace();
            done = true;
        }
    }

    @Override
    public void run() {
        if (done || configuration.isServerShutDown()) {
            try {
                done = true;
                socket.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (future != null) {
                    future.cancel(false);
                }
                configuration.decrementClients();
                System.out.println("Disconnect");
            }
        }
        else {
            try {
                System.out.println("Work");

                if (inputStream.available() > 0) {
                    int command = inputStream.readInt();

                }

                // Iterate through the list and find finished tasks
            }
            catch (IOException e) {
                e.printStackTrace();
                done = true;
            }
        }
    }

    public void setFuture(ScheduledFuture<?> future) {
        this.future = future;
    }

}
