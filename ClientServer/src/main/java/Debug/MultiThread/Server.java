package Debug.MultiThread;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.*;

public class Server {

    public static void main(String ... args) {

        try (
                ServerSocket serverSocket = new ServerSocket(3345);
                PrintWriter out = new PrintWriter(System.out, true);
        ) {

            long numberOrClients = 0;
            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);

            while (true) {

                Socket incoming = serverSocket.accept();

                out.println("Connect client with id " + numberOrClients);

                ClientConnection handler = new ClientConnection(incoming, numberOrClients);
                ScheduledFuture<?> future = executorService.scheduleAtFixedRate(handler, 2000, 1000, TimeUnit.MILLISECONDS);
                handler.setFuture(future);

                numberOrClients += 1;

            }

            //executorService.shutdown();
            //executorService.shutdownNow();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

}


class ClientConnection implements Runnable {

    private Socket socket;
    private ScheduledFuture<?> future;
    private long connectionId;
    private boolean setup = true;

    private InputStream inputStream;
    private OutputStream outputStream;

    private Scanner in;
    private PrintWriter out;

    ClientConnection(Socket socket, long connectionId) {
        this.socket = socket;
        this.connectionId = connectionId;
    }

    public void setFuture(ScheduledFuture<?> future) {
        this.future = future;
    }

    @Override
    public void run() {

        if (setup) {
            try {
                try {
                    setup = false;

                    inputStream = socket.getInputStream();
                    outputStream = socket.getOutputStream();

                    in = new Scanner(inputStream);
                    out = new PrintWriter(outputStream, true);

                    out.println("Connection id: " + connectionId + "; Enter 'QUIT' to leave connection");
                }
                catch (IOException e) {
                    future.cancel(false);

                    e.printStackTrace();
                    socket.close();
                }
            }
            catch (IOException e) {
                future.cancel(false);

                e.printStackTrace();
            }

        }
        else {
            try {
                String line = "";

                DataInputStream dataInputStream = new DataInputStream(inputStream);

                if (dataInputStream.available() > 0) {
                    line = in.nextLine();
                    System.out.println("[" + connectionId + "]: " + line);
                    out.println("[" + connectionId + "]: " + line);
                }

                if (line.equals("QUIT")) {
                    socket.close();
                    future.cancel(false);
                    System.out.println("Disconnect client id [" + connectionId + "]");
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Finish run for connection id [" + connectionId + "]");
    }
}
