package org.coursera.algorithms.week_5;

import java.util.Arrays;

public class CircularSuffixArray {

    private final String word;
    private final CircularSuffix[] sortedSuffix;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }
        word = s;
        sortedSuffix = new CircularSuffix[s.length()];
        for (int i = 0; i < s.length(); i++) {
            sortedSuffix[i] = new CircularSuffix(s, i);
        }
        Arrays.sort(sortedSuffix);
    }

    // length of s
    public int length() {
        return word.length();
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i > length() - 1) {
            throw new IllegalArgumentException();
        }
        CircularSuffix suffix = sortedSuffix[i];
        return suffix.offset;
    }

    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray("ABRACADABRA!");

        for (int i = 0; i < circularSuffixArray.sortedSuffix.length; i++) {
            System.out.println("index[" + i + "] = " + circularSuffixArray.index(i));
        }
    }


    private class CircularSuffix implements Comparable<CircularSuffix> {

        private final String suffix;
        private final int offset;

        public CircularSuffix(String suffix, int offset) {
            this.suffix = suffix;
            this.offset = offset;
        }

        public int charAtPosition(int pos) {
            return suffix.charAt((offset + pos) % suffix.length());
        }

        @Override
        public int compareTo(CircularSuffix otherSuffix) {
            if (this == otherSuffix) return 0;
            for (int i = 0; i < suffix.length(); i++) {
                if (this.charAtPosition(i) < otherSuffix.charAtPosition(i)) return -1;
                if (this.charAtPosition(i) > otherSuffix.charAtPosition(i)) return 1;
            }
            return 0;
        }
    }

}