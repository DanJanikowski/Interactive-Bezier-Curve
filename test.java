import java.util.Random;

/**
 * Created by Dan on 7/11/2017.
 */
public class test {
    public static void main(String[] args) {
        int[] curve = new int[100];
        for (int i = 0; i < 1000000; i++)
            curve[new Random().nextInt(100)]++;
        for (int i = 0; i < curve.length; i++) {
            System.out.println(curve[i]);
        }
    }
}
