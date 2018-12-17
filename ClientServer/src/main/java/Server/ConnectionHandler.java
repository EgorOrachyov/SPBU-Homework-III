package Server;

import Filter.FilterBehavior;
import Filter.Image;
import Misc.Message;
import Misc.Transfer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ScheduledFuture;

public class ConnectionHandler implements Runnable {

    private boolean done = false;
    private int time = 0;
    private TaskHandler task;

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

            Transfer.send(outputStream, Message.FILTERS);
            Transfer.sendFilters(outputStream, configuration.getFilters());
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
                Transfer.send(outputStream, Message.EXIT);
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
                System.out.println("Work...");

                if (inputStream.available() > 0) {

                    time = 0;
                    Message action = Transfer.receive(inputStream);

                    if (action == Message.EXIT) {
                        done = true;
                    }
                    else if (action == Message.FILTER) {
                        final int filterId = inputStream.readInt();
                        final Image source = Transfer.receiveImage(inputStream);

                        System.out.println("Receive image | size: " + (source.getWidth() * source.getHeight()) + " | filterId: " + filterId);

                        if (filterId < configuration.getFilters().size()) {
                            try {
                                task = new TaskHandler(source, (FilterBehavior) configuration.getFilters().get(filterId).newInstance());
                                configuration.getTaskPool().submitTask(task);
                            }
                            catch (InstantiationException e1) {
                                // suppress
                            }
                            catch (IllegalAccessException e2) {
                                // suppress
                            }

                            System.out.println("Start filtering");

                        }

                    }
                    else if (action == Message.CANCEL) {
                        task = null;
                        System.out.println("Cancel task");
                    }

                }

                if (task != null) {
                    if (task.getProcessDone().get()) {
                        Transfer.send(outputStream, Message.RESULT);
                        Transfer.sendImage(outputStream, task.getResult());

                        Image result = task.getResult();
                        System.out.println("Send image | size: " + (result.getWidth() * result.getHeight()));

                        task = null;
                    }
                    else {
                        Transfer.send(outputStream, Message.PROGRESS);
                        outputStream.writeInt(task.getProgress().get());
                    }
                }

                if (time++ > configuration.getTimeOut()) {
                    done = true;
                }
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
