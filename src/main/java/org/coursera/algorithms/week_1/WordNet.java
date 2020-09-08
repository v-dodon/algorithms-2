package org.coursera.algorithms.week_1;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WordNet {

    private final Digraph digraph;
    private final Map<String, Set<Integer>> synsetsMap;
    private final Map<Integer, String> intToStr;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }
        synsetsMap = new HashMap<String, Set<Integer>>();
        intToStr = new HashMap<Integer, String>();
        In in = new In(synsets);
        int vertexCount = 0;
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] split = line.split(",");
            int synId = Integer.parseInt(split[0]);
            String[] words = split[1].split("\\s+");
            intToStr.put(synId, split[1]);
            for (String w : words) {
                if (synsetsMap.get(w) == null) {
                    Set<Integer> set = new HashSet<Integer>();
                    set.add(synId);
                    synsetsMap.put(w, set);
                } else {
                    synsetsMap.get(w).add(synId);
                }
            }
            vertexCount++;
        }
        digraph = new Digraph(vertexCount);
        in = new In(hypernyms);
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] split = line.split(",");
            if (split.length < 2) {
                continue;
            }
            String rootSyn = split[0];
            for (String synId : split) {
                if (!rootSyn.equals(synId)) {
                    digraph.addEdge(Integer.parseInt(rootSyn), Integer.parseInt(synId));
                }
            }
        }
        isDAG();
        sap = new SAP(digraph);
    }

    private void isDAG() {
        DirectedCycle directedCycle = new DirectedCycle(digraph);
        int root = Integer.MIN_VALUE;
        boolean isDag = true;
        for (int i = 0; i < digraph.V(); i++) {
            if (digraph.outdegree(i) == 0) {
                if (root != Integer.MIN_VALUE) {
                    isDag = false;
                    break;
                }
                root = i;
            }
        }
        if (directedCycle.hasCycle() || !isDag) {
            throw new IllegalArgumentException();
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return synsetsMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }
        return synsetsMap.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null || !synsetsMap.containsKey(nounA) || !synsetsMap.containsKey(nounB)) {
            throw new IllegalArgumentException();
        }
        return sap.length(synsetsMap.get(nounA), synsetsMap.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null || !synsetsMap.containsKey(nounA) || !synsetsMap.containsKey(nounB)) {
            throw new IllegalArgumentException();
        }
        int anc = sap.ancestor(synsetsMap.get(nounA), synsetsMap.get(nounB));
        return intToStr.get(anc);
    }
}