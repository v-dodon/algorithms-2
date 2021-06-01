package org.coursera.algorithms.week_5;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.StdOut;

public class BurrowsWheeler {

    private static final int R = 256;

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String word = "ABRACADABRA!";
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(word);
        StringBuilder stringBuilder = new StringBuilder();
        int original = 0;
        for (int i = 0; i < word.length(); i++) {
            int index = circularSuffixArray.index(i);
            int lastIndex = index == 0 ? word.length() - 1 : index - 1;
            char lastChar = word.charAt(lastIndex);
            stringBuilder.append(lastChar);
            if (index == 0) {
                original = i;
            }
        }

        String result = original + stringBuilder.toString();
        for (int i = 0; i <result.length(); i++) {

            StdOut.printf("%02x", result.charAt(i) & 0xff);
            System.out.print(" ");
        }

//        BinaryStdOut.flush();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int original = BinaryStdIn.readInt();
        String word = BinaryStdIn.readString();
        int[] next = sort(word);
        StringBuilder stringBuilder = new StringBuilder(word.length());
        int currentIndex = original;
        for (int i = 0; i < next.length; i++) {
            stringBuilder.append(word.charAt(next[currentIndex]));
            currentIndex = next[currentIndex];
        }
        BinaryStdOut.write(stringBuilder.toString());
        BinaryStdOut.flush();
    }

    private static int[] sort(String word) {
        int length = word.length();
        int[] count = new int[R + 1];
        int[] next = new int[word.length()];
        for (int i = 0; i < length; i++) {
            count[word.charAt(i) + 1]++;
        }
        for (int i = 0; i < R; i++) {
            count[i + 1] += count[i];
        }
        for (int i = 0; i < length; i++) {
            next[count[word.charAt(i)]++] = i;
        }
        return next;
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if ("-".equals(args[0])) {
            transform();
        } else if ("+".equals(args[0])) {
            inverseTransform();
        }
    }

}