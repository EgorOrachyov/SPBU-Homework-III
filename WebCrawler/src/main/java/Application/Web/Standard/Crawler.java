package Application.Web.Standard;

import Application.Util.ArraySaver;
import Application.Util.FileSaver;
import Application.Web.ICrawler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Crawler implements ICrawler {

    public static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) " +
            "AppleWebKit/535.1 (KHTML, like Gecko) " +
            "Chrome/13.0.782.112 Safari/535.1";

    private ConcurrentHashMap<String,String> map;
    private ExecutorService executor;

    public Crawler(int threadsCount) {
        map = new ConcurrentHashMap<>();
        executor = Executors.newFixedThreadPool(threadsCount);
    }

    @Override
    public ArrayList<String> download(String page, int depth, long timeout) {

        ArraySaver saver = new ArraySaver();
        executor.execute(new ProcessPage(executor, map, page, 0, depth, saver));

        try {
            System.out.print("Print any key to immediately stop downloading: ");
            System.in.read();
        }
        catch (IOException e) {
            // Ignore any input exceptions (does not matter)
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(timeout, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

        return saver.getData();
    }

    @Override
    public void download(String page, int depth, String save, long timeout) {

        FileSaver saver = new FileSaver(save);
        executor.execute(new ProcessPage(executor, map, page, 0, depth, saver));

        try {
            System.out.print("Print any key to immediately stop downloading: ");
            System.in.read();
        }
        catch (IOException e) {
            // Ignore any input exceptions (does not matter)
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(timeout, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

    }

    public static void main(String ... args) {

        Crawler crawler = new Crawler(4);
        ArrayList<String> result = crawler.download("http://en.wikipedia.org/", 0, 1);
        System.out.println(result.size());

    }

}
