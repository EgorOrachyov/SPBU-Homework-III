package Filter;

public class Color {

    private static final int BIT_OFFSET_24 = 24;
    private static final int BIT_OFFSET_16 = 16;
    private static final int BIT_OFFSET_8  = 8;

    private int r;
    private int g;
    private int b;
    private int a;

    public Color(int red, int green, int blue, int alpha) {
        r = red;
        g = green;
        b = blue;
        a = alpha;
    }

    public Color(int red, int green, int blue) {
        this(red, green, blue, 0xFF);
    }

    public int getABGRColor() {
        return  (a << BIT_OFFSET_24) |
                (b << BIT_OFFSET_16) |
                (g << BIT_OFFSET_8)  |
                (r);
    }

    public int getRGBAColor() {
        return  (r << BIT_OFFSET_24) |
                (g << BIT_OFFSET_16) |
                (b << BIT_OFFSET_8)  |
                (a);
    }

    public Color add(Color c) {
        return new Color(r + c.r, g + c.g, b + c.b, a + c.a);
    }

    public void addToThis(Color c) {
        r += c.r;
        g += c.g;
        b += c.b;
        a += c.a;
    }

    public Color multiply(Color c) {
        return new Color(r * c.r, g * c.g, b * c.b, a * c.a);
    }

    public void multiplyToThis(Color c) {
        r *= c.r;
        g *= c.g;
        b *= c.b;
        a *= c.a;
    }

    public Color multiply(float f) {
        return new Color((int)(f * r), (int)(f * g), (int)(f * b), (int)(f * a));
    }

    public void multiplyToThis(float f) {
        r = (int)(f * r);
        g = (int)(f * g);
        b = (int)(f * b);
        a = (int)(f * a);
    }

    public void assign(Color c) {
        r = c.r;
        g = c.g;
        b = c.b;
        a = c.a;
    }

}
