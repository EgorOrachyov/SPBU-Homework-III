package Debug.MultiThread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    public static void main(String ... args) {

        try (
                ServerSocket serverSocket = new ServerSocket(3345);
                PrintWriter out = new PrintWriter(System.out, true);
        ) {

            long numberOrClients = 0;

            while (true) {

                Socket incoming = serverSocket.accept();

                out.println("Connect client with id " + numberOrClients);

                Thread thread = new Thread(new ClientConnection(incoming, numberOrClients));
                thread.setDaemon(true);
                thread.start();

                numberOrClients += 1;

            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

}


class ClientConnection implements Runnable {

    private Socket socket;
    private long connectionId;

    ClientConnection(Socket socket, long connectionId) {
        this.socket = socket;
        this.connectionId = connectionId;
    }

    @Override
    public void run() {

        try {
            try (
                    InputStream inputStream = socket.getInputStream();
                    OutputStream outputStream = socket.getOutputStream();

                    Scanner in = new Scanner(inputStream);
                    PrintWriter out = new PrintWriter(outputStream, true);
            ) {
                out.println("Connection id: " + connectionId + "; Enter 'QUIT' to leave connection");

                boolean done = false;
                while (!done && in.hasNextLine()) {

                    String line = in.nextLine();
                    System.out.println("[" + connectionId + "]:" + line);
                    out.println("[" + connectionId + "]:" + line);
                    if (line.equals("QUIT")) done = true;

                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                socket.close();
                System.out.println("Disconnect client id [" + connectionId + "]");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
