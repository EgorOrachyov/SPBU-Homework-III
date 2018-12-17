package Debug.MultiThread;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String ... args) {

        System.out.println("=== Run client ===");

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
            while (!done && in.hasNextLine()) {

                String line = in.nextLine();

                consoleOut.println("Server: " + line);
                consoleOut.println("Enter Message: ");

                String input = consoleIn.nextLine();

                out.println(input);

                if (input.equals("QUIT")) done = true;

            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("=== Shut down client ===");
    }

}
