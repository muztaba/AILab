import java.io.PrintWriter;
import java.util.Random;

/**
 * Created by seal on 12/10/14.
 */
public class ArrayUtils {
    public static int[] init(int n, Random r) {
        int[] s = new int[n];
        for (int i = 1; i < s.length; i++) {
            s[i] = r.nextInt(n) + 1;
        }
        return s;
    }

    public static void copyArray(int[] src, int[] dest, int from, int to, int length) {
        System.arraycopy(src, from, dest, to, length);
    }

    public static void fillArray(int n, int[] array, Random r) {
        for (int i = 1; i < array.length; i++) {
            int val = r.nextInt(n) + 1;
            array[i] = val;
        }
    }

    public static void printArray(int[][] array, PrintWriter out) {
        for (int[] row : array) {
            for (int i : row) out.print(i + " ");
            out.println();
        }
    }

    public static void copyArray(int[] src, int[] dest) {
        System.arraycopy(src, 0, dest, 0, src.length);
    }
}
