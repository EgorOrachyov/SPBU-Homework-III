package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ScheduledFuture;

public class ConnectionFactory implements Runnable {

    private ConnectionPool connectionPool;
    private TaskPool taskPool;
    private Configuration configuration;
    private ServerSocket serverSocket;

    public ConnectionFactory(Configuration configuration) {
        this.configuration = configuration;
        this.connectionPool = new ConnectionPool(
                configuration.getConnectionPoolInitialDelay(),
                configuration.getConnectionPoolRatePeriod(),
                configuration.getConnectionPoolThreadsCount()
        );

        try {
            serverSocket = new ServerSocket(configuration.getPort());

            System.out.println("----------- Server Initialize -----------");
            System.out.println("Create Server Socket");
        }
        catch (IOException e) {
            e.printStackTrace();
            configuration.setServerShutDown(true);
        }
    }

    @Override
    public void run() {

        // Send runnable to handle input from console to
        // have control on Server
        connectionPool.submitTask(new InputHandler());

        try {
            System.out.println("-------------- Server Works -------------");

            boolean done = false;

            while (!configuration.isServerShutDown()) {

                System.out.println("Waiting for connection...");
                Socket connection = serverSocket.accept();

                if (!configuration.isServerShutDown() &&
                        (
                            !configuration.isNumOfClientsLimited() ||
                            configuration.getNumOfClients() < configuration.getMaxNumOfClients()
                        )
                ) {
                    System.out.println("Accept connection [" + configuration.getNumOfClients() + "]");

                    configuration.incrementClients();
                    ConnectionHandler connectionHandler = new ConnectionHandler(connection, configuration);
                    ScheduledFuture<?> future = connectionPool.submitConnection(connectionHandler);
                    connectionHandler.setFuture(future);
                }
                else {
                    System.out.println("Reject connection " + connection.toString());
                }
            }
        }
        catch (IOException e) {
            // Suppress exception if server is shutting down
            if (!configuration.isServerShutDown()) {
                e.printStackTrace();
            }
        }

        System.out.println("---------- Initialize Shutdown ----------");

        configuration.setServerShutDown(true);
        System.out.println("Server shutdown");

        while (configuration.getNumOfClients() > 0) {
            Thread.yield();
        }
        System.out.println("All connections closed");

        connectionPool.shutdown();
        System.out.println("Connection pool shutdown");

    }

    /**
     * Handles admin's input from console to provide work
     * with server (server configuration, shutdown)
     */
    protected class InputHandler implements Runnable {

        @Override
        public void run() {

            try (Scanner in = new Scanner(System.in)) {

                if (in.hasNext()) {
                    String input = in.next();
                    if (input.equals("EXIT")) {
                        configuration.setServerShutDown(true);
                        serverSocket.close();
                    }
                }

            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
