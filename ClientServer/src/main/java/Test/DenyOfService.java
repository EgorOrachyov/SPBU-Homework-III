package Test;

import Client.AsyncClient;
import Client.FilterTask;
import Filter.Image;

import java.io.IOException;
import java.util.ArrayList;

public class DenyOfService {

    public static void main(String ... args) {

        final int FILTER_ID = 2;
        final int port = 40000;
        final String host = "localhost";
        //final String imagePath = "src/main/java/Debug/Images/test3.jpg";
        final String imagePath = "src/main/java/Debug/Images/test2.jpg";

        boolean done = false;
        int numberOfConnectedClients = 0;
        int limitation = 20000;

        Image image;
        ArrayList<AsyncClient> clients = new ArrayList<>(limitation);

        try {
            image = new Image(imagePath);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

        while (numberOfConnectedClients < limitation && !done) {

            try {

                AsyncClient client = new AsyncClient(host, port);
                //client.submitTask(new FilterTask(image, FILTER_ID));
                clients.add(client);

                numberOfConnectedClients += 1;

                System.out.println("Connect [" + numberOfConnectedClients + "]");

            } catch (IOException e) {
                e.printStackTrace();
                done = true;
            }

        }

        if (!done) {
            for (AsyncClient client : clients) {
                FilterTask task;
                do {
                    task = client.getCompletedTasks().poll();
                } while (task == null && !client.isDone());

                client.done(true);
            }
        }

        System.out.println("Max connected clients: " + numberOfConnectedClients);
    }

}
