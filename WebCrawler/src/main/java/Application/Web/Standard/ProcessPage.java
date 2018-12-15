package Application.Web.Standard;

import java.io.IOException;
import Application.Util.ISaver;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class ProcessPage implements Runnable {

    private ExecutorService executorService;
    private ConcurrentMap<String,String> map;
    private String link;
    private Integer depth;
    private Integer maxDepth;
    private ISaver saver;
    private AtomicInteger counter;
    private int pageCount;

    public ProcessPage(ExecutorService executorService,
                       ConcurrentMap<String,String> map,
                       String link,
                       Integer depth,
                       Integer maxDepth,
                       ISaver saver,
                       AtomicInteger counter,
                       int pageCount) {

        this.executorService = executorService;
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
                    else if (map.containsKey(absUrl)) {
                        // skip it
                    }
                    else {
                        map.put(absUrl, absUrl);

                        executorService.execute(
                                new ProcessPage(
                                        executorService, map,
                                        absUrl, newDepth,
                                        maxDepth, saver,
                                        counter, pageCount
                                )
                        );
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
