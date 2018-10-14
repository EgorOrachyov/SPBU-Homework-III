package Application.TurtleMovement;

public class Vector2d {

    protected final double x;
    protected final double y;

    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2d(Vector2d another) {
        this.x = another.x;
        this.y = another.y;
    }

    public Vector2d add(Vector2d v) {
        return new Vector2d(x + v.x, y + v.y);
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    @Override
    public String toString() {
        return String.format("(X=%f Y=%f)", x, y);
    }
}
