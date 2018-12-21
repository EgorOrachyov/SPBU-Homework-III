package Misc;

import Filter.FilterBehavior;
import Filter.FilterInfo;
import Filter.Image;

import java.io.*;
import java.util.ArrayList;

public class Transfer {

    /**
     * Send command (action) to server / client
     * @param stream To send data (primarily for sockets)
     * @param action Message action id
     * @throws IOException
     */
    public static void send(DataOutputStream stream, Message action) throws IOException {
        stream.writeInt(action.getId());
    }

    /**
     * Receive command (action) from stream
     * @param stream To get data (primarily for sockets)
     * @return Message read command from stream
     * @throws IOException
     */
    public static Message receive(DataInputStream stream) throws IOException {
        return Message.fromId(stream.readInt());
    }

    /**
     * Send available filter to client
     * @param stream To send data (primarily for sockets)
     * @param filters List of available on server side filters
     * @throws IOException
     */
    public static void sendFilters(DataOutputStream stream, ArrayList<Class> filters) throws IOException {
        stream.writeInt(filters.size());

        for (int i = 0; i < filters.size(); ++i) {
            try {
                FilterBehavior filter = (FilterBehavior) filters.get(i).newInstance();

                stream.writeInt(i);
                stream.writeUTF(filter.getName());
                stream.writeUTF(filter.getDescription());
            }
            catch (InstantiationException e1) {
                // suppress
            }
            catch (IllegalAccessException e2) {
                // suppress
            }
        }

    }

    /**
     * Receive available filters from server
     * @param stream To get data (primarily for sockets)
     * @return List of filters
     * @throws IOException
     */
    public static ArrayList<FilterInfo> receiveFilters(DataInputStream stream) throws IOException {
        final int size = stream.readInt();

        ArrayList<FilterInfo> filters = new ArrayList<>(size);

        for (int i = 0; i < size; ++i) {
            final int id = stream.readInt();
            final String name = stream.readUTF();
            final String description = stream.readUTF();

            filters.add(new FilterInfo(name, description, id));
        }

        return filters;
    }

    /**
     * Sends image's width, height and internal data via output stream
     * @param stream To send data (primarily for sockets)
     * @param source Image to sendFilters
     * @throws IOException
     */
    public static void sendImage(DataOutputStream stream, Image source) throws IOException {
        final int width = source.getWidth();
        final int height = source.getHeight();

        stream.writeInt(width);
        stream.writeInt(height);

        final int[] data = source.serialize();

        byte[] m = new byte[width * height * 4];

        int k = 0;
        for (int i = 0; i < width * height; ++i) {
            m[k    ] = (byte)((data[i] & 0xFF000000) >>> 24);
            m[k + 1] = (byte)((data[i] & 0x00FF0000) >>> 16);
            m[k + 2] = (byte)((data[i] & 0x0000FF00) >>>  8);
            m[k + 3] = (byte)((data[i]             )       );

            k += 4;
        }

        stream.write(m, 0, width * height * 4);
    }

    /**
     * Receive image's width, height and internal data via input stream
     * @param stream To get data (primarily for sockets)
     * @return Received image from stream
     * @throws IOException
     */
    public static Image receiveImage(DataInputStream stream) throws IOException {
        final int width = stream.readInt();
        final int height = stream.readInt();

        final int[] data = new int[width * height];
        byte[] m = new byte[width * height * 4];
        stream.readFully(m, 0, width * height * 4);

        int k = 0;
        for (int i = 0; i < width * height; ++i) {

            data[i] = (m[k    ] << 24) |
                      (m[k + 1] << 16) |
                      (m[k + 2] << 8 ) |
                      (m[k + 3]      ) ;

            k += 4;

        }

        return new Image(width, height, data);
    }

}
