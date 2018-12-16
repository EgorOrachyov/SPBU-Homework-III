package Debug.MultiThread;

import Filter.Image;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String ... args) {

        System.out.println("=== Run client ===");

        boolean flag = false;

        try (
                Socket socket = new Socket("localhost", 8813);

                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

                Scanner in = new Scanner(inputStream);
                PrintWriter out = new PrintWriter(outputStream, true);

                Scanner consoleIn = new Scanner(System.in);
                PrintWriter consoleOut = new PrintWriter(System.out, true);
        ) {

            consoleOut.println("Setup connection to server");

            boolean done = false;
            while (!done) {

                //String line = in.nextLine();
                //consoleOut.println("Server: " + line);

                if (!flag) {
                    flag = true;
                    Image image = new Image("/Users/egororachyov/Desktop/Documents/Intellej Idea/SPBU-Homework-III/ClientServer/src/main/java/Debug/Images/test2.jpg");
                    outputStream.writeInt(1);
                    outputStream.writeInt(image.getWidth());               System.out.println("Width " + image.getWidth());
                    outputStream.writeInt(image.getHeight());              System.out.println("Height " + image.getHeight());

                    int[] data = image.serialize();
                    for (int i = 0; i < image.getHeight() * image.getWidth(); i++) {
                        outputStream.writeInt(data[i]);
                        if (i % image.getWidth() == 0)
                            System.out.println("-> " + i);
                    }

                    System.out.println("Send " + (image.getWidth() * image.getHeight()));
                }

                //consoleOut.println("Enter Message: ");
                //String input = consoleIn.nextLine();

                //out.println(input);

                //if (input.equals("QUIT")) done = true;

            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("=== Shut down client ===");
    }

}
