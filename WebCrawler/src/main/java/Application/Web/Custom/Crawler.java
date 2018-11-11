package Application.Web.Custom;

import Application.Concurrent.HashMap;
import Application.Concurrent.LinkedList;
import Application.Concurrent.ThreadPool;
import Application.Util.ListSaver;
import Application.Util.FileSaver;
import Application.Web.ICrawler;

public class Crawler implements ICrawler {

    public static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) " +
            "AppleWebKit/535.1 (KHTML, like Gecko) " +
            "Chrome/13.0.782.112 Safari/535.1";

    private HashMap<String,String> map;
    private ThreadPool threadPool;

    public Crawler(int threadsCount) {
        map = new HashMap<>();
        threadPool = new ThreadPool(threadsCount);
    }

    @Override
    public LinkedList<String> download(String page, int depth, long timeout) {

        ListSaver saver = new ListSaver();
        threadPool.execute(new ProcessPage(threadPool, map, page, 0, depth, saver));

        threadPool.finish();
        threadPool.waitAndJoin();

        return saver.getData();
    }

    @Override
    public void download(String page, int depth, String save, long timeout) {

        FileSaver saver = new FileSaver(save);
        threadPool.execute(new ProcessPage(threadPool, map, page, 0, depth, saver));

        threadPool.finish();
        threadPool.waitAndJoin();
    }

    public static void main(String ... args) {

        Crawler crawler = new Crawler(4);
        //LinkedList<String> result = crawler.download("http://en.wikipedia.org/", 1, 1);
        //System.out.println("Total: " + result.getElementsCount());
        crawler.download("http://www.shaderx.com", 1, "/Users/egororachyov/Desktop/Documents/Intellej Idea/SPBU-Homework-III/WebCrawler/src/main/Test",0);
    }

}
