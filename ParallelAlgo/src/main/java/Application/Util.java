package Application;

public class Util {

    public static boolean isPowerfTwo(int value) {
        return  (value > 0) && (((value - 1) & value) == 0);
    }

    public static int toPowerOfTwo(int value) {
        if (value > 1) {
            if (((value - 1) & value) == 0) {
                return value;
            } else {
                for(int i = 2; true; i *= 2) {
                    if (i <= value && value <= i * 2) {
                        return i * 2;
                    }
                }
            }
        }
        else {
            return 1;
        }
    }

    public static int align(int value, int alignment) {
        if (value % alignment == 0) {
            return value;
        }
        else {
            return value + (alignment - (value % alignment));
        }
    }

}
