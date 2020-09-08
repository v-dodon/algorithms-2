package org.coursera.algorithms.week_1;

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

public class SAP {

    private final Digraph digraph;
    private BreadthFirstDirectedPaths firstDirectedPaths;
    private BreadthFirstDirectedPaths secondDirectedPaths;
    private int commonAncest;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException();
        }
        digraph = new Digraph(G);

    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        return getlength(v, w);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        int legth = getlength(v, w);
        return legth == -1 ? -1 : commonAncest;
    }

    private int getlength(int v, int w) {
        firstDirectedPaths = new BreadthFirstDirectedPaths(digraph, v);
        secondDirectedPaths = new BreadthFirstDirectedPaths(digraph, w);
        int length = -1;
        for (int i = 0; i < digraph.V(); i++) {
            if (firstDirectedPaths.hasPathTo(i) && secondDirectedPaths.hasPathTo(i)) {
                int distSum = firstDirectedPaths.distTo(i) + secondDirectedPaths.distTo(i);
                if (length == -1) {
                    length = distSum;
                } else {
                    length = Math.min(length, distSum);
                }
                commonAncest = length == distSum ? i : commonAncest;
            }
        }
        return length;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }
        firstDirectedPaths = new BreadthFirstDirectedPaths(digraph, v);
        secondDirectedPaths = new BreadthFirstDirectedPaths(digraph, w);
        int length = -1;
        for (int i = 0; i < digraph.V(); i++) {
            if (firstDirectedPaths.hasPathTo(i) && secondDirectedPaths.hasPathTo(i)) {
                int distSum = firstDirectedPaths.distTo(i) + secondDirectedPaths.distTo(i);
                if (length == -1) {
                    length = distSum;
                } else {
                    length = Math.min(length, distSum);
                }
                commonAncest = length == distSum ? i : commonAncest;
            }
        }
        return length;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }
        int legth = length(v, w);
        return legth == -1 ? -1 : commonAncest;
    }
}
