package Application.Util;

/**
 * Some logic for saving internet web pages on the disk
 * (may implement simple loading in some king of array of
 * load pages on hhd or ssd)
 */
public interface ISaver {

    boolean save(String document, String name);

}
