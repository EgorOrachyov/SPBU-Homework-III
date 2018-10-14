package Application.TurtleMovement;

public class Transform {

    /**
     * Compute final movement from point (0,0) for object via transformations pairs
     * (a,d), where:
     *          a - counterclockwise rotation angle
     *          b - directional offset
     * @param movements (a,b) pairs
     * @return position in the 2d space
     */
    public static Vector2d move(Vector2d[] movements) {

        double posX = 0;
        double posY = 0;
        double angle = 0;

        for(int i = 0; i < movements.length; i++) {
            double rot = Math.toRadians(movements[i].x());
            double offset = movements[i].y();

            angle += rot;
            posX += offset * Math.cos(angle);
            posY += offset * Math.sin(angle);

        }

        return new Vector2d(posX, posY);
    }

    public static void main(String ... args) {

        Vector2d[] movements = new Vector2d[]{
                new Vector2d(0, 1.0),
                new Vector2d(90, 1.0),
                new Vector2d(135.0, Math.sqrt(2))
        };

        System.out.println(Transform.move(movements));

    }

}
