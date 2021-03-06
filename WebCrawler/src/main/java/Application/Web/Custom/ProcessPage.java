package Application.Web.Custom;

import Application.Concurrent.IMap;
import Application.Concurrent.ThreadPool;
import Application.Util.ISaver;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class ProcessPage implements Runnable {

    private ThreadPool threadPool;
    private IMap<String,String> map;
    private String link;
    private Integer depth;
    private Integer maxDepth;
    private ISaver saver;
    private AtomicInteger counter;
    private int pageCount;

    public ProcessPage(ThreadPool threadPool,
                       IMap<String,String> map,
                       String link,
                       Integer depth,
                       Integer maxDepth,
                       ISaver saver,
                       AtomicInteger counter,
                       int pageCount) {

        this.threadPool = threadPool;
        this.map = map;
        this.link = link;
        this.depth = depth;
        this.maxDepth = maxDepth;
        this.saver = saver;
        this.counter = counter;
        this.pageCount = pageCount;
    }

    @Override
    public void run() {
        if (counter != null) {
            if (counter.getAndIncrement() > pageCount) {
                return;
            }
        }

        try {
            final Connection connection = Jsoup.connect(link).userAgent(Crawler.USER_AGENT);
            final Document document = connection.get();
            final Elements links = document.getElementsByTag("a");

            final Integer newDepth = depth + 1;

            // Check out links on the current document
            if (newDepth <= maxDepth) {
                for(Element l : links) {
                    final String absUrl = l.absUrl("href");

                    if (absUrl.length() == 0) {
                        // empty links are not valid
                    }
                    else {
                        if (map.add(absUrl, absUrl) == null) {
                            threadPool.execute(
                                    new ProcessPage(
                                            threadPool, map,
                                            absUrl, newDepth,
                                            maxDepth, saver,
                                            counter, pageCount
                                    )
                            );
                        }
                    }
                }
            }

            // Retrieve to save out document in the specific way
            saver.save(document.outerHtml(), link);
        }
        catch (IOException e) {
            System.err.println("JSoup: Cannot get document via link " + link);
            e.printStackTrace();
        }
    }
}
