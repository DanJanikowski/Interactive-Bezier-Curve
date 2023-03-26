import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Created by Dan on 7/11/2017.
 */
public class Space {

    public ArrayList<Vector> v;

    public Space() {
        v = new ArrayList<>();
    }

    public void reset() {
        v = new ArrayList<>();
    }

    public void addPoint(double x, double y) {
        v.add(new Vector(x, y));
    }

    public int sumsum(int x) {
        if (x == 1) return x;
        else return x + sumsum(x - 1);
    }
    public Vector getPt(Vector a, Vector b, float t) {
        Vector c = b.clone().subtract(a);
        c.multiply(t);
        c.add(a);
        return c;
    }
    public void strokeBezier(GraphicsContext g) {
        //DRAW CONNECTION LINES
        g.setStroke(Color.GRAY);
        for (int i = 0; i < v.size() - 1; i++)
            g.strokeLine(v.get(i).x, v.get(i).y, v.get(i + 1).x, v.get(i + 1).y);
        //CALCULATE CURVE
        Vector last = new Vector(v.get(0));
        Vector[] points = new Vector[sumsum(v.size())];
        for (int i = 0; i < v.size(); i++)
            points[i] = v.get(i);
        g.setStroke(Color.RED);
        g.setLineWidth(3);
        for (float i = 0; i <= 1; i += 0.01) {
            int iters = v.size() - 1, index = 0, index2 = 0;
            while (iters > 0) {
                for (int j = 0; j < iters; j++) {
                    points[v.size() + index] = getPt(points[index + index2], points[index + 1 + index2], i);
                    index++;
                }
                iters--;
                index2++;
            }
            g.strokeLine(last.x, last.y, points[points.length-1].x, points[points.length-1].y);
            last = new Vector(points[points.length-1]);
        }
    }

    public void draw(GraphicsContext g) {
        if (v.size() > 1) strokeBezier(g);
        g.setFill(Color.WHITE);
        g.setLineWidth(1);
        for (Vector vector: v)
            g.fillOval(vector.x - 2, vector.y - 2, 4, 4);
    }
}
