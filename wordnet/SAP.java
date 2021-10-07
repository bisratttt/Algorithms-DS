/* *****************************************************************************
 *  Name:       Bisrat Zerihun
 *  Date:       07/27/2021
 *  Description:    Calculates a common ancestor and shortest
 *                  length between two vertex
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

public class SAP {

    private Digraph G;

    /**
     * constructor that takes a digraph to
     * implement the ds
     *
     * @param G digraph used to compute distance and ancestor
     */
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException();
        this.G = new Digraph(G);
    }

    /**
     * length of the shortest ancestral path between v and w
     *
     * @param v first index in graph
     * @param w second index in graph
     * @return shortest ancestral path, -1 if no such path
     */
    public int length(int v, int w) {
        if (v > G.V() || w > G.V()) throw new IllegalArgumentException();
        BreadthFirstDirectedPaths forV = new BreadthFirstDirectedPaths(this.G, v);
        BreadthFirstDirectedPaths forW = new BreadthFirstDirectedPaths(this.G, w);
        int minLength = Integer.MAX_VALUE;
        for (int i = 0; i < this.G.V(); i++) {
            if (forV.hasPathTo(i) && forW.hasPathTo(i) && (forV.distTo(i) + forW.distTo(i)
                    < minLength)) {
                minLength = forV.distTo(i) + forW.distTo(i);
            }
        }
        if (minLength != Integer.MAX_VALUE) return minLength;
        return -1;
    }

    /**
     * common ancestor of v and w that participates in a shortest ancestral
     * path
     *
     * @param v first index of graph
     * @param w second index of graph
     * @return common ancestor, -1 if no such path
     */
    public int ancestor(int v, int w) {
        if (v > G.V() || w > G.V()) throw new IllegalArgumentException();
        BreadthFirstDirectedPaths forV = new BreadthFirstDirectedPaths(this.G, v);
        BreadthFirstDirectedPaths forW = new BreadthFirstDirectedPaths(this.G, w);
        int minLength = Integer.MAX_VALUE;
        int ancestor = -1;
        for (int i = 0; i < this.G.V(); i++) {
            if (forV.hasPathTo(i) && forW.hasPathTo(i) && (forV.distTo(i) + forW.distTo(i)
                    < minLength)) {
                minLength = forV.distTo(i) + forW.distTo(i);
                ancestor = i;
            }
        }
        if (ancestor > -1) return ancestor;
        return -1;
    }

    /**
     * length of the shortest ancestral path between any vertex in
     * v and any vertex in w
     *
     * @param v iterable of synsets
     * @param w iterable of synsets
     * @return shortest ancestral path, -1 if no such path
     */
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        checkIterable(v, w);
        BreadthFirstDirectedPaths forV = new BreadthFirstDirectedPaths(this.G, v);
        BreadthFirstDirectedPaths forW = new BreadthFirstDirectedPaths(this.G, w);
        int minLength = Integer.MAX_VALUE;
        for (int i = 0; i < this.G.V(); i++) {
            if (forV.hasPathTo(i) && forW.hasPathTo(i) && (forV.distTo(i) + forW.distTo(i)
                    < minLength)) {
                minLength = forV.distTo(i) + forW.distTo(i);
            }
        }
        if (minLength != Integer.MAX_VALUE) return minLength;
        return -1;
    }

    /**
     * common ancestor that participates in shortest ancestral path
     *
     * @param v iterable of synsets
     * @param w iterable of synsets
     * @return common ancestor, -1 if no such path
     */
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException();
        checkIterable(v, w);
        BreadthFirstDirectedPaths forV = new BreadthFirstDirectedPaths(this.G, v);
        BreadthFirstDirectedPaths forW = new BreadthFirstDirectedPaths(this.G, w);
        int minLength = Integer.MAX_VALUE;
        int ancestor = -1;
        for (int i = 0; i < this.G.V(); i++) {
            if (forV.hasPathTo(i) && forW.hasPathTo(i) && (forV.distTo(i) + forW.distTo(i)
                    < minLength)) {
                minLength = forV.distTo(i) + forW.distTo(i);
                ancestor = i;
            }
        }
        if (ancestor > -1) return ancestor;
        return -1;
    }

    /**
     * checks if any iterable object contains null elements
     * or is outside the vertex range of the graph
     *
     * @param v first iterable element
     * @param w second iterable element
     */
    private void checkIterable(Iterable<Integer> v, Iterable<Integer> w) {
        for (Integer iter : v) {
            if (iter == null) throw new IllegalArgumentException();
            if (iter > G.V()) throw new IllegalArgumentException();
        }
        for (Integer iter : w) {
            if (iter == null) throw new IllegalArgumentException();
            if (iter > G.V()) throw new IllegalArgumentException();
        }
    }

    public static void main(String[] args) {
        // In in = new In(args[0]);
        // Digraph G = new Digraph(in);
        // SAP sap = new SAP(G);
        // int v = 2, w = 0;
        // LinkedList<Integer> v = new LinkedList<>();
        // LinkedList<Integer> w = new LinkedList<>();
        // v.add(13);
        // v.add(23);
        // v.add(24);
        // w.add(6);
        // w.add(16);
        // w.add(17);
        // int length = sap.length(v, w);
        // int ancestor = sap.ancestor(v, w);
        // StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    }
}
