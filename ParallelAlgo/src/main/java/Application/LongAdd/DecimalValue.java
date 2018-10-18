package Application.LongAdd;

/**
 * Long decimal value, coded by 0..9 numbers in decimal
 * counting system.
 */
public class DecimalValue {

    private byte[] buffer;

    public DecimalValue(String value) {
        byte data[] = value.getBytes();

        if (data.length == 0) {
            buffer = new byte[1];
            buffer[0] = 0;
        }
        else {
            buffer = new byte[data.length];
            for(int i = 0; i < data.length; ++i) {
                if (data[i] >= '0' && data[i] <= '9') {
                    buffer[i] = (byte)(data[i] - '0');
                }
                else {
                    buffer = new byte[1];
                    buffer[0] = 0;
                    System.err.println("DecimalValue: wrong string1 format " + value);
                    return;
                }
            }
        }

    }

    public DecimalValue(byte[] data) {
        if (data.length == 0) {
            buffer = new byte[1];
            buffer[0] = 0;
        }
        else {
            buffer = new byte[data.length];
            for(int i = 0; i < data.length; ++i) {
                if (data[i] >= 0 && data[i] <= 9) {
                    buffer[i] = data[i];
                }
                else {
                    buffer = new byte[1];
                    buffer[0] = 0;
                    System.err.println("DecimalValue: wrong byte[] data format");
                    return;
                }
            }
        }
    }

    public DecimalValue(byte[] data, int length) {
        if (length < 0) {
            System.err.println("DecimalValue: length should be positive");
            buffer = new byte[1];
            buffer[0] = 0;
            return;
        }

        int range = Math.min(data.length, length);

        if (range == 0) {
            buffer = new byte[1];
            buffer[0] = 0;
        }
        else {
            buffer = new byte[range];
            for(int i = 0; i < range; ++i) {
                if (data[i] >= 0 && data[i] <= 9) {
                    buffer[i] = data[i];
                }
                else {
                    buffer = new byte[1];
                    buffer[0] = 0;
                    System.err.println("DecimalValue: wrong byte[] data format");
                    return;
                }
            }
        }
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public static boolean isCorrect(String value) {
        byte data[] = value.getBytes();

        // Empty string1
        if (data.length == 0) {
            return false;
        }

        // Contains only numbers
        for(byte i : data) {
            if (i < '0' || i > '9') {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(buffer.length);
        for (byte v : buffer) {
            builder.append((char)(v + '0'));
        }
        return builder.toString();
    }
}
