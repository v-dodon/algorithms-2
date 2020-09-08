package org.coursera.algorithms.week_1;

public class Outcast {
    private final WordNet wordNet;

    public Outcast(WordNet wordnet) {
        this.wordNet = wordnet;

    }         // constructor takes a WordNet object

    public String outcast(String[] nouns) {
        int dist = 0;
        String out = null;
        for (int i = 0; i < nouns.length; i++) {
            int curDist = 0;
            for (int j = 0; j < nouns.length; j++) {
                if (i == j) {
                    continue;
                }
                curDist += wordNet.distance(nouns[i], nouns[j]);
            }
            dist = Math.max(dist, curDist);
            if (dist == curDist) {
                out = nouns[i];
            }
        }
        return out;

    }   // given an array of WordNet nouns, return an outcast
}