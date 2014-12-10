import java.util.*;
import java.io.*;
/**
 * Created by seal on 12/10/14.
 */
public class Generic {
    public static void main(String[] args) {
    }
}

class GenericAlgorithm {
    public static final int P = 10;
    public static final int N = 1 << 10;

    public boolean solve(int n, Random r) {
        Node[] p = new Node[n];
        for (int i = 0; i < p.length; i++) {
            p[i] = new Node(n);
        }

        for (int i = 0; i < p.length; i++) {
            ArrayUtils.init(n, p[i].s, r);
            if (p[i].getQuality() == 0) return true;
        }

        for (int C = 0; C < N; C++) {
            int c1Pos = 0, c2Pos = 1;
            for (int i = 0; i < p.length; i++) {
                int[] c1 = p[r.nextInt(p.length)].getArray();
                int[] c2 = p[r.nextInt(p.length)].getArray();

            }
        }

        return false;
    }

    public void crossOver(int n, int[] c1, int[] c2, Random r) {
        int t = r.nextInt(n) + 1;
        int[] p = new int[c1.length], q = new int[c2.length];
        ArrayUtils.copyArray(c1, p, 1, t, t);
        ArrayUtils.copyArray(c2, q, 1, t, t);
        ArrayUtils.copyArray(c1, p, t + 1, c1.length, c1.length - t);
    }
}

class Node {
    int[] s = null;

    public Node(int n) {
        this.s = new int[n + 1];
    }
    public Node(int[] s) {this.s = s;}

    public int getQuality() {
        int q = 0;

        for (int i = 1; i < s.length - 1; i++) {
            for (int j = i + 1; j < s.length; j++) {
                if (s[i] == s[j]) q++;
                if (Math.abs(i - j) == Math.abs(s[i] - s[j])) q++;
            }
        }

        return q;
    }

    public int[] getArray() {
        return s;
    }

}

