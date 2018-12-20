package Filter;

public class Color {

    public static final int BIT_OFFSET_24 = 24;
    public static final int BIT_OFFSET_16 = 16;
    public static final int BIT_OFFSET_8  = 8;

    private int r;
    private int g;
    private int b;
    private int a;

    public Color() {
        r = g = b = a = 0;
    }

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

    public static Color fromABGR(int ABGR) {
        int r, g, b, a;

        r = (ABGR & 0xFF)                         ;
        g = (ABGR & 0xFF00)     >>> BIT_OFFSET_8  ;
        b = (ABGR & 0xFF0000)   >>> BIT_OFFSET_16 ;
        a = (ABGR & 0xFF000000) >>> BIT_OFFSET_24 ;

        return new Color(r, g, b, a);
    }

    public static Color fromRGBA(int RGBA) {
        int a, b, g, r;

        a = (RGBA & 0xFF)                         ;
        b = (RGBA & 0xFF00)     >>> BIT_OFFSET_8  ;
        g = (RGBA & 0xFF0000)   >>> BIT_OFFSET_16 ;
        r = (RGBA & 0xFF000000) >>> BIT_OFFSET_24 ;

        return new Color(r, g, b, a);
    }

    public Color add(Color c) {
        return new Color(r + c.r, g + c.g, b + c.b, 0xFF);
    }

    public void addToThis(Color c) {
        r += c.r;
        g += c.g;
        b += c.b;
        a += c.a;
    }

    public Color multiply(Color c) {
        return new Color(r * c.r, g * c.g, b * c.b, 0xFF);
    }

    public void multiplyToThis(Color c) {
        r *= c.r;
        g *= c.g;
        b *= c.b;
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

    public float lengthRGB() {
        return (float) Math.sqrt(r * r + g * g + b * b);
    }

    public float lenghtRGBA() {
        return (float) Math.sqrt(r * r + g * g + b * b + a * a);
    }

}
