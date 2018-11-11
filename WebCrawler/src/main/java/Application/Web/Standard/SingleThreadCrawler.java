package Application.Web.Standard;

import Application.Web.ICrawler;
import Application.Util.FileSaver;
import Application.Util.Pair;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class SingleThreadCrawler implements ICrawler {

    public static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) " +
            "AppleWebKit/535.1 (KHTML, like Gecko) " +
            "Chrome/13.0.782.112 Safari/535.1";

    @Override
    public Application.Concurrent.LinkedList<String> download(String page, int depth, long timeout) {

        Application.Concurrent.LinkedList<String> data = new Application.Concurrent.LinkedList<>();
        List<Pair<String,Integer>> queue = new LinkedList<>();
        HashMap<String,String> map = new HashMap<>();

        queue.add(new Pair<>(page, 0));

        while (!queue.isEmpty()) {
            Pair<String,Integer> next = queue.remove(0);

            final Integer newDepth = next.getValue() + 1;

            try {
                final Connection connection = Jsoup.connect(next.getKey()).userAgent(USER_AGENT);
                final Document document = connection.get();

                if (newDepth <= depth) {
                    Elements links = document.getElementsByTag("a");
                    for (Element link : links) {
                        final String absUrl = link.absUrl("href");

                        if (absUrl.length() == 0) {
                            // skip
                        }
                        else if (map.containsKey(absUrl)) {
                            // skip
                        }
                        else {
                            map.put(absUrl, absUrl);
                            queue.add(new Pair<>(absUrl, newDepth));
                        }
                    }
                }

                data.add(document.outerHtml());
            }
            catch (IOException e) {
                System.err.println("JSoup: Cannot get document via link " + next.getKey());
                e.printStackTrace();
            }
        }

        return data;
    }

    @Override
    public void download(String page, int depth, String save, long timeout) {

        FileSaver saver = new FileSaver(save);
        List<Pair<String,Integer>> queue = new LinkedList<>();
        HashMap<String,String> map = new HashMap<>();

        queue.add(new Pair<>(page, 0));

        while (!queue.isEmpty()) {
            Pair<String,Integer> next = queue.remove(0);

            final Integer newDepth = next.getValue() + 1;

            try {
                final Connection connection = Jsoup.connect(next.getKey()).userAgent(USER_AGENT);
                final Document document = connection.get();

                if (newDepth <= depth) {
                    Elements links = document.getElementsByTag("a");
                    for (Element link : links) {
                        final String absUrl = link.absUrl("href");

                        if (absUrl.length() == 0) {
                            // skip
                        }
                        else if (map.containsKey(absUrl)) {
                            // skip
                        }
                        else {
                            map.put(absUrl, absUrl);
                            queue.add(new Pair<>(absUrl, newDepth));
                        }
                    }
                }

                saver.save(document.outerHtml(), next.getKey());
            }
            catch (IOException e) {
                System.err.println("JSoup: Cannot get document via link " + next.getKey());
                e.printStackTrace();
            }
        }

    }

    public static void main(String ... args) {

        SingleThreadCrawler crawler = new SingleThreadCrawler();
        Application.Concurrent.LinkedList<String> result = crawler.download("http://en.wikipedia.org/", 1, 1);
        System.out.println("Total: " + result.getElementsCount());
        //crawler.download("http://www.shaderx.com", 1, "/Users/egororachyov/Desktop/Documents/Intellej Idea/SPBU-Homework-III/WebCrawler/src/main/Test",0);

    }


}
