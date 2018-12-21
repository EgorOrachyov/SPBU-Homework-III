package Test;

import Client.AsyncClient;
import Client.FilterTask;
import Filter.Image;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class ServiceTimeMeasurement {

    public static void main(String ... args) {

        final int FILTER_ID = 2;
        final int port = 40000;
        final String host = "localhost";
        final String imagePath = "src/main/java/Debug/Images/test4.jpg";

        final Image image;
        final int[] clientsCount = {1,20,40,80};
        ArrayList<Long> clientsTime;
        ArrayList<AsyncClient> clients;

        try {
            image = new Image(imagePath);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

        for (int i = 0; i < clientsCount.length; i++) {

            clients = new ArrayList<>(clientsCount[i]);
            clientsTime = new ArrayList<>(clientsCount[i]);

            for (int j = 0; j < clientsCount[i]; j++) {
                try {
                    clients.add(new AsyncClient(host, port));
                    clients.get(j).submitTask(new FilterTask(image, FILTER_ID));
                    clientsTime.add(System.nanoTime());
                }
                catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }

            int done = 0;
            while (done < clientsCount[i]) {
                for (int j = 0; j < clientsCount[i]; j++) {
                    AsyncClient client = clients.get(j);
                    FilterTask task = client.getCompletedTasks().poll();

                    if (task != null) {
                        clientsTime.set(j, System.nanoTime() - clientsTime.get(j));
                        client.done(true);
                        done += 1;
                    }
                }
            }

            double averageTime;
            long p_averageTime = 0;
            for (Long v : clientsTime) {
                p_averageTime += v;
            }
            averageTime = (double) p_averageTime / clientsCount[i] / 1000000000;

            clientsTime.sort(new Comparator<Long>() {
                @Override
                public int compare(Long o1, Long o2) {
                    return (o1 < o2 ? -1 : (o1 > o2 ? 1 : 0));
                }
            });

            double medianTime;
            medianTime = (double) clientsTime.get(clientsCount[i] / 2) / 1000000000;

            System.out.println("Stat for " + clientsCount[i] + " clients");
            System.out.println("Average time: " + averageTime);
            System.out.println("Median time: " + medianTime);
            System.out.println("");

        }


    }

}
