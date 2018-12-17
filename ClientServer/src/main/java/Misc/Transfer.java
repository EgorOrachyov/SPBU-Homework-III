package Misc;

import Filter.Image;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Transfer {

    /**
     * Sends image's width, height and internal data via output stream
     * @param stream To send data (primarily for sockets)
     * @param source Image to send
     * @throws IOException
     */
    public static void send(DataOutputStream stream, Image source) throws IOException {
        final int width = source.getWidth();
        final int height = source.getHeight();

        stream.writeInt(width);
        stream.writeInt(height);

        final int[] data = source.serialize();

        for(int i = 0; i < width * height; ++i) {
            stream.writeInt(data[i]);
        }
    }

    /**
     * Receive image's width, height and internal data via input stream
     * @param stream To send data (primarily for sockets)
     * @return Received image from stream
     * @throws IOException
     */
    public static Image receive(DataInputStream stream) throws IOException {
        final int width = stream.readInt();
        final int height = stream.readInt();

        final int[] data = new int[width * height];

        for (int i = 0; i < width * height; ++i) {
            data[i] = stream.readInt();
        }

        return new Image(width, height, data);
    }

}
