package Application.Web;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public class Crawler implements ICrawler {

    private ConcurrentLinkedQueue<Link> queue;
    private ConcurrentHashMap<String,String> map;
    private ThreadPoolExecutor executor;
    private AtomicInteger finishedThreads;

    public Crawler() {
        queue = new ConcurrentLinkedQueue<>();
        map = new ConcurrentHashMap<>();
    }

    /**
     * Downloads recursively by links URL html pages of web site from the Internet and
     * saves its raw text data in array list of strings
     * @param page Path to download first page
     * @param depth How far we should go from the first downloaded page ( = 0 if we
     *              want to download only the first page)
     * @return Array of pages raw data
     */
    public ArrayList<String> download(String page, int depth) {

        return null;
    }

    /**
     * Downloads recursively by links URL html pages of web site from the Internet and
     * saves its raw text in the folder with path 'save'
     * @param page Path to download first page
     * @param depth How far we should go from the first downloaded page (= 0 if we
     *              want to download only the first page)
     * @param save Path to save the pages
     */
    public void download(String page, int depth, String save) {

    }

}
