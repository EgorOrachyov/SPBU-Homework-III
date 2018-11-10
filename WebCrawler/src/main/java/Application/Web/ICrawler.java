package Application.Web;

import java.util.ArrayList;

public interface ICrawler {

    /**
     * Downloads recursively by links URL html pages of web site from the Internet and
     * saves its raw text data in array list of strings
     * @param page Path to download first page
     * @param depth How far we should go from the first downloaded page ( = 0 if we
     *              want to download only the first page)
     * @return Array of pages raw data
     */
    ArrayList<String> download(String page, int depth);

    /**
     * Downloads recursively by links URL html pages of web site from the Internet and
     * saves its raw text in the folder with path 'save'
     * @param page Path to download first page
     * @param depth How far we should go from the first downloaded page (= 0 if we
     *              want to download only the first page)
     * @param save Path to save the pages
     */
    void download(String page, int depth, String save);

}
