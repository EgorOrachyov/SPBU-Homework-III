package Application.Web;

/**
 * Encapsulates link (sa string) behaviour
 */
public class Link {

    private String link;
    private int depth;

    Link(String link, int depth) {
        this.link = link;
        this.depth = depth;
    }

    public String getLink() {
        return link;
    }

    public int getDepth() {
        return depth;
    }
}
