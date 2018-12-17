package Misc;

/**
 * Information exchange between client and server
 * Connection interface
 */
public enum Message {

    /** Disconnect client and server */
    EXIT    (0),

    /** Say serves to receive image and blur it */
    FILTER  (1),

    /** Send client progress of current filter applying */
    PROGRESS(2),

    /** Send client to get result of filter applying from server */
    RESULT  (3);

    private int id;

    Message(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static Message fromId(int id) {
        switch (id) {

            case 1: return  FILTER;
            case 2: return  PROGRESS;
            case 3: return  RESULT;

            default: return EXIT;

        }
    }
    
}
