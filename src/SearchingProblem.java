import java.util.*;
import java.io.*;
/**
 * Created by seal on 12/3/14.
 */
public class SearchingProblem {
    public static void main(String[] args) throws IOException {
        Random r = new Random();
        PrintWriter out = new PrintWriter(new FileWriter("out.txt"));
        Task solver = new Task();
        GenericAlgorithm generic = new GenericAlgorithm();
        int n = 0;


        int[][] res = new int[10][7];

        for (int i = 0; i < 10; i++) {
            n += 4;
            for (int j = 0; j < res[i].length; j++) {
                if (j == 0 && solver.montiCarlo(n, r)) res[i][j]++;
                else if (j == 1 && solver.gibbs(n, r)) res[i][j]++;
                else if (j == 2 && solver.hillClimbing(n, r)) res[i][j]++;
                else if (j == 3 && solver.hillClimbingRestart(n, r)) res[i][j]++;
                else if (j == 4 && solver.hillClimbingBS(n, .7, r)) res[i][j]++;
                else if (j == 5 && solver.simulated(n, r)) res[i][j]++;
                else if (j == 6 && generic.solve(n, r, out)) res[i][j]++;
            }
            System.out.println(n);
        }

        ArrayUtils.printArray(res, out);
        out.println();
        out.close();
    }
}

class Task {
    public static final int N = 10000;
    private int indexForUndo = 0;
    private int valueForUndo = 0;

    public boolean montiCarlo(int n, Random r) {
        int[] s = new int[n + 1], f = new int[n + 1];

        for (int c = 0; c < N; c++) {
            ArrayUtils.fillArray(n, s, r);
            if (getQuality(s) == 0) return true;
        }

        return false;
    }

    public boolean gibbs(int n, Random r) {
        int[] s = new int[n + 1], f = new int[n + 1];
        ArrayUtils.fillArray(n, s, r);

        for (int c = 0; c < N; c++) {
            if (getQuality(s) == 0) return true;
            makeChange(s, r);
        }

        return false;
    }

    public boolean simulated(int n, Random r) {
        int[] s = new int[n + 1];
        ArrayUtils.fillArray(n, s, r);
        int q = Integer.MAX_VALUE;
        double T = 1000000.0;

        for (int i = 0; i < 1 << 25 && T > 0; i++) {
            int temp = getQuality(s);

            if (temp == 0) return true;

            makeChange(s, r);

            if (temp <= q) q = temp;
            else {
                double t = Math.random();
                int e = q - temp;
                double exp = Math.exp((double) e / T);

                if (t <= exp) {
                    q = temp;
                    T -= .5;
                }
            }
        }

        return false;
    }

    public boolean hillClimbingBS(int n, double m, Random r) {
        int[] s = new int[n + 1];
        ArrayUtils.fillArray(n, s, r);
        int q = Integer.MAX_VALUE;
        for (int i = 0; i < 1 << 25; i++) {
            int temp = getQuality(s);

            if (temp == 0) return true;

            makeChange(s, r);

            if (temp <= q) {
                q = temp;
            } else {
                double t = Math.random();
                if (t <= m) q = temp;
                else undo(s);
            }

        }
        return false;
    }

    public boolean hillClimbingRestart(int n, Random r) {
        int[] s = new int[n + 1];
        int cnt = 0;
        int q = Integer.MAX_VALUE;
        ArrayUtils.fillArray(n, s, r);
        for (int i = 0; i < 1 << 25; i++) {
            int temp = getQuality(s);

            if (temp == 0) return true;

            if (temp < q) cnt = 0;
            else cnt++;

            if (temp <= q) {
                q = temp;
            }

            makeChange(s, r);

            /*
            Restart at beginning
             */
            if (cnt > 4000) {
                cnt = 0;
                q = Integer.MAX_VALUE;
                ArrayUtils.fillArray(n, s, r);
            }

        }
        return false;
    }


    public boolean hillClimbing(int n, Random random) {
        int[] s = new int[n + 1];
        ArrayUtils.fillArray(n, s, random);
        int q = Integer.MAX_VALUE;

        for (int c = 0; c < N; c++) {
            int temp = getQuality(s);

            if (temp == 0) return true;

            if (temp <= q) q = temp;
            else undo(s);

            makeChange(s, random);
        }

        return false;
    }

    public void undo(int[] s) {
        s[indexForUndo] = valueForUndo;
    }

    public void makeChange(int[] s, Random random) {
        int val = random.nextInt(s.length - 1) + 1;
        int index = random.nextInt(s.length - 1) + 1;
        indexForUndo = index;
        valueForUndo = s[index];
        s[index] = val;
    }

    public int getQuality(int[] s) {
        int q = 0;

        for (int i = 1; i < s.length - 1; i++) {
            for (int j = i + 1; j < s.length; j++) {
                if (s[i] == s[j]) q++;
                if (Math.abs(i - j) == Math.abs(s[i] - s[j])) q++;
            }
        }

        return q;
    }
}
