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
            out = new PrintWriter(outputStream, true);

            out.println("Connected");
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

                    System.out.println("Read input");

                    if (command == 1) {

                        System.out.println("FILTER");

                        int width = inputStream.readInt();      System.out.println("width " + width);
                        int height = inputStream.readInt();     System.out.println("height " + height);

                        int[] data = new int[width * height]; System.out.println(width * height);
                        for (int i = 0; i < width * height; i++) {
                            data[i] = inputStream.readInt();
                            if (i % width == 0)
                                System.out.println("<- " + i);
                        }

                        Image source = new Image(width, height, data);
                        System.out.println("source");

                        source.saveImage("/Users/egororachyov/Desktop/Documents/Intellej Idea/SPBU-Homework-III/ClientServer/src/main/java/Debug/Images/server.png");
                        System.out.println("Get image");
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
