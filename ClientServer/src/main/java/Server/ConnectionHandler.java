package Server;

import Filter.Image;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ScheduledFuture;

public class ConnectionHandler implements Runnable {

    private boolean done = false;

    private Socket socket;
    private Configuration configuration;
    private ScheduledFuture<?> future;

    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private Scanner in;
    private PrintWriter out;

    public ConnectionHandler(Socket socket, Configuration configuration) {
        this.socket = socket;
        this.configuration = configuration;

        try {
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());

            in = new Scanner(inputStream);
            out = new PrintWriter(outputStream);
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
                if (inputStream.available() > 0) {
                    String command = in.nextLine();

                    if (command.equals("FILTER")) {
                        int width = Integer.valueOf(in.nextLine());
                        int height = Integer.valueOf(in.nextLine());
                        String buffer = in.nextLine();
                        Image source = new Image(width, height, buffer);

                        // send new task
                    }
                }

                // Iterate through the list and find finished tasks
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setFuture(ScheduledFuture<?> future) {
        this.future = future;
    }

}
