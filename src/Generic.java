import java.util.*;
import java.io.*;
/**
 * Created by seal on 12/10/14.
 */
public class Generic {
    public static void main(String[] args) throws IOException {
        PrintWriter out = new PrintWriter(new FileWriter("GenericTestOut.txt"));
        Random r = new Random();
        GenericAlgorithm solver = new GenericAlgorithm();

            solver.solve(40, r, out);


        out.close();
    }
}

class GenericAlgorithm {
    public static final int P = 10;
    public static final int N = 1 << 20;
    PriorityQueue<Node> queue;

    public boolean solve(int n, Random r, PrintWriter out) {
        queue = new PriorityQueue<>();
        Node[] p = new Node[n];


        for (int i = 0; i < p.length; i++) {
            p[i] = new Node();
        }

        for (int i = 0; i < p.length; i++) {
            p[i].setArray(ArrayUtils.init(n + 1, r));
        }

        for (int C = 0; C < N; C++) {
            Node[] c = new Node[n];
            queue.clear();
            /*
            crossover from the parent array p
             */
            for (int c1Pos = 0, c2Pos = 1; ; c1Pos += 2, c2Pos += 2) {
                int[] c1 = p[r.nextInt(p.length)].getArray();
                int[] c2 = p[r.nextInt(p.length)].getArray();

                crossOver(n, c1, c2, r);

                if (c1Pos < p.length) c[c1Pos] = new Node(c1); else break;
                if (c2Pos < p.length) c[c2Pos] = new Node(c2); else break;

            }

            /*
            Make change to the crossover array / or make all the crossover array as mutant. And add p, c, and mutant to
            the Priority Queue for.
             */
            for (int i = 0; i < p.length; i++) {
                p[i].getQuality();
                queue.add(p[i]);

                c[i].getQuality();
                queue.add(c[i]);

                Node mutant = new Node (c[i].getArray());
                mutant.makeChange(r);
                queue.add(mutant);
            }

            /*
            Get the first 10 elements that has quality minimum as parent for the next iteration. If one of these element
             quality '0' then it return true otherwise go for the next iteration if the 'C' is not out of its limit.
             */
            for (int i = 0; i < p.length; i++) {
                p[i] = queue.poll();
                if (p[i].getQ() == 0) return true;
            }

        }

        out.print(" "+queue.poll().getQ() + " ");
        return false;
    }

    public void crossOver(int n, int[] c1, int[] c2, Random r) {
        int t = r.nextInt(n) + 1;

        if (t == 1) t += 2; else if (t == n) t -= 2;

        int[] p = new int[c1.length], q = new int[c2.length];

        ArrayUtils.copyArray(c1, p, 1, 1, t + 1);
        ArrayUtils.copyArray(c2, p, t + 1, t + 1, n - t);

        ArrayUtils.copyArray(c2, q, 1, 1, t + 1);
        ArrayUtils.copyArray(c1, q, t + 1, t + 1, n - t);

        ArrayUtils.copyArray(p, c1);
        ArrayUtils.copyArray(q, c2);
    }
}

class Node implements Comparable<Node>{
    private int[] s = null;
    private int q = 0;

    public Node() {}
    public Node(int n) {
        this.s = new int[n + 1];
    }
    public Node(int[] s) {this.s = s;}

    public int getQuality() {
        this.q = 0;

        for (int i = 1; i < s.length - 1; i++) {
            for (int j = i + 1; j < s.length; j++) {
                if (s[i] == s[j]) q++;
                if (Math.abs(i - j) == Math.abs(s[i] - s[j])) q++;
            }
        }

        return this.q;
    }

    public int getQ() { return this.q; }

    public void setArray(int[] s) {
        this.s = s;
    }

    public int[] getArray() {
        int[] t = new int[s.length];
        ArrayUtils.copyArray(s, t);
        return t;
    }

    public void makeChange(Random r) {
        int index = r.nextInt(s.length -1) + 1;
        int val = r.nextInt(s.length -1) + 1;

        s[index] = val;
        getQuality();
    }

    @Override
    public int compareTo(Node o) {
        return Integer.compare(this.q, o.getQ());
    }
}