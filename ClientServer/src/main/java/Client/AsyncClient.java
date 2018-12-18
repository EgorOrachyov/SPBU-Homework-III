package Client;

import Filter.FilterInfo;
import Filter.Image;
import Misc.Message;
import Misc.Transfer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Async Client working in the daemon thread
 * Allows to sendFilters task in some king of task queue,
 * Send images for filtering, get filtered images,
 * monitor current progress in filtering and so on...
 *
 * @note Should be independent for any king of UI
 */
public class AsyncClient implements Runnable {

    private final String host;
    private final int port;
    private final boolean runAsync;

    private Socket socket;

    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    private ArrayList<FilterInfo> filters;
    private ConcurrentLinkedQueue<FilterTask> completedTasks;

    private final Object lock;
    private volatile FilterTask currentTask;
    private volatile int currentTaskProgress;

    private volatile boolean done = false;
    private volatile boolean newTask = false;
    private volatile boolean processTask = false;
    private volatile boolean cancelTask = false;

    public AsyncClient(String host, int port) throws IOException {
        this(host, port, true);
    }

    public AsyncClient(String host, int port, boolean runInSeparateThread) throws IOException {
        this.host = host;
        this.port = port;
        this.lock = new Object();
        this.completedTasks = new ConcurrentLinkedQueue<>();
        this.runAsync = runInSeparateThread;

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
            System.out.println("Server is not available: try to connect later...");
            throw new IOException("Server is not available: try to connect later...");
        }
    }

    private class ConnectionHandler implements Runnable {

        @Override
        public void run() {

            while (!done) {

                try {

                    if (inputStream.available() > 0) {

                        Message action = Transfer.receive(inputStream);

                        if (action == Message.PROGRESS) {
                            currentTaskProgress = inputStream.readInt();
                        }
                        else if (action == Message.RESULT) {
                            Image result = Transfer.receiveImage(inputStream);
                            FilterTask task;

                            synchronized (lock) {
                                task = currentTask;
                                newTask = false;
                                processTask = false;
                                cancelTask = false;
                                currentTask = null;
                                currentTaskProgress = 0;
                            }

                            completedTasks.add(new FilterTask(result, task.getFilterId()));
                        }
                        else if (action == Message.EXIT) {
                            done = true;
                        }
                        else if (action == Message.FILTERS) {
                            filters = Transfer.receiveFilters(inputStream);
                        }

                    }

                    synchronized (lock) {

                        if (cancelTask) {
                            Transfer.send(outputStream, Message.CANCEL);
                            cancelTask = false;
                            currentTaskProgress = 0;
                        }

                        if (newTask && currentTask != null && !processTask) {
                            processTask = true;
                            Transfer.send(outputStream, Message.FILTER);
                            outputStream.writeInt(currentTask.getFilterId());
                            Transfer.sendImage(outputStream, currentTask.getSource());
                        }

                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                    done = true;
                }

            }

            System.out.println("Close client side. Finish session...");

            try {
                Transfer.send(outputStream, Message.EXIT);

                if (socket != null) socket.close();

                inputStream.close();
                outputStream.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public int getCurrentTaskProgress() {
        return currentTaskProgress;
    }

    public boolean isProcessTask() {
        return processTask;
    }

    public boolean isClientAsync() {
        return runAsync;
    }

    public FilterTask getCurrentTask() {
        return currentTask;
    }

    public void done(boolean finishWork) {
        this.done = finishWork;
    }

    public ArrayList<FilterInfo> getFilters() {
        return filters;
    }

    public ConcurrentLinkedQueue<FilterTask> getCompletedTasks() {
        return completedTasks;
    }

    public void cancelTask() {
        synchronized (lock) {
            cancelTask = true;
        }
    }

    public void submitTask(FilterTask task) {
        synchronized (lock) {
            if (processTask) {
                cancelTask = true;
                processTask = false;
            }

            newTask = true;
            currentTask = task;
            currentTaskProgress = 0;
        }
    }

    @Override
    public void run() {
        if (!runAsync) {
            ConnectionHandler connectionHandler = new ConnectionHandler();
            connectionHandler.run();
        }
    }

    public static void main(String ... args) {

        try {
            AsyncClient client = new AsyncClient("localhost", 8813, true);
            client.submitTask(new FilterTask(new Image("src/main/java/Debug/Images/test2.jpg"), 1));

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //client.cancelTask();

            while (client.isProcessTask()) {
                //System.out.println(client.getCurrentTaskProgress());
            }

            ConcurrentLinkedQueue<FilterTask> result = client.getCompletedTasks();
            FilterTask task;

            do {
                task = result.poll();
            } while (task == null);

            if (task != null) {
                System.out.println("Task was done");
                //task.getSource().saveImage("src/main/java/Debug/Images/server3.png");
            }
            else {
                System.out.println("Task was not done");
            }

            client.done(true);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
