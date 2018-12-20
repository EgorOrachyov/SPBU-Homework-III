package Application;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Test {

    public static void main(String ... args) {

        final String USER_AGENT =
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";

        try {
            Document doc = Jsoup.connect("http://en.wikipedia.org/").userAgent(USER_AGENT).get();
            System.out.print(doc.title());

            Elements links = doc.getElementsByTag("a");
            for (Element link : links) {
                System.out.printf(" | %s \n",
                        //link.attr("href"),
                        //link.text(),
                        link.absUrl("href"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("----------------------------------------------------------------");

        try {
            Document doc = Jsoup.connect("https://en.wikipedia.org/wiki/Beowulf_(hero)").get();
            System.out.print(doc.title());
            System.out.println(doc.outerHtml());

            Elements links = doc.getElementsByTag("a");
            for (Element link : links) {
                System.out.printf(" | %s \n",
                        //link.attr("href"),
                        //link.text(),
                        link.absUrl("href"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
