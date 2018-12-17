package Application.TurtleMovement;

public class Matrix2x2d {

    protected final double a00; protected final double a01;
    protected final double a10; protected final double a11;

    public Matrix2x2d(double a00, double a01,
                      double a10, double a11) {
        this.a00 = a00;
        this.a01 = a01;
        this.a10 = a10;
        this.a11 = a11;
    }

    public Vector2d multiply(Vector2d v) {
        return new Vector2d(
                a00 * v.x + a01 * v.y,
                a10 * v.x + a11 * v.y);
    }

    public static Vector2d multiply(Matrix2x2d m, Vector2d v) {
        return m.multiply(v);
    }

}
