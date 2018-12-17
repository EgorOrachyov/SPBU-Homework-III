package Misc;

/**
 * Information exchange between client and server
 * Connection interface
 */
public class Message {

    /** Disconnect client and server */
    public static final int EXIT     = 0;

    /** Say serves to receive image and blur it */
    public static final int FILTER   = 1;

    /** Send client progress of current filter applying */
    public static final int PROGRESS = 2;

    /** Send client to get result of filter applying from server */
    public static final int RESULT   = 3;

}
