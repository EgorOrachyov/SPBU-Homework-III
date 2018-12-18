package Application.Web.Standard;

import Application.Concurrent.LinkedList;
import Application.Util.ListSaver;
import Application.Util.FileSaver;
import Application.Web.ICrawler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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
    public LinkedList<String> download(String page, int depth, long timeout) {

        ListSaver saver = new ListSaver();
        executor.execute(new ProcessPage(executor, map, page, 0, depth, saver, null, 0));

        try {
            System.out.println("Print any key to immediately stop downloading: ");
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
        executor.execute(new ProcessPage(executor, map, page, 0, depth, saver, null, 0));

        try {
            System.out.println("Print any key to immediately stop downloading: ");
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

    @Override
    public LinkedList<String> downloadCount(String page, int depth, int pageCount) {
        AtomicInteger counter = new AtomicInteger(0);
        ListSaver saver = new ListSaver();
        executor.execute(new ProcessPage(executor, map, page, 0, depth, saver, counter, pageCount));

        while (counter.get() < pageCount) {

        }

        executor.shutdown();

        return saver.getData();
    }

    public static void main(String ... args) {

        Crawler crawler = new Crawler(4);
        LinkedList<String> result = crawler.download("http://www.shaderx.com", 1, 1);
        //LinkedList<String> result = crawler.downloadCount("http://en.wikipedia.org/", 1, 264);
        System.out.println("Total: " + result.getElementsCount());


    }

}
