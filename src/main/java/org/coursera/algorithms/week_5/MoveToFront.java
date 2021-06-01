package org.coursera.algorithms.week_5;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.ArrayList;
import java.util.List;

public class MoveToFront {
    private static final int R = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] characters = new char[R];
        char[] charPos = new char[R];
        for (int i = 0; i < R; i++) {
            characters[i] = (char) i;
            charPos[i] = (char) i;
        }
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int currentIndex = 0;
            for (int i = 0; i < R; i++) {
                if (charPos[i] == c) {
                    currentIndex = i;
                    break;
                }
            }
            char[] temp = new char[currentIndex];
            char current = charPos[currentIndex];
            BinaryStdOut.write(characters[currentIndex]);
            System.arraycopy(charPos, 0, temp, 0, currentIndex);
            System.arraycopy(temp, 0, charPos, 1, currentIndex);
            charPos[0] = current;
        }
        BinaryStdOut.flush();

    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        List<Character> characters = getAsciiCharacters();
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            char readChar = characters.remove(255 - c);
            BinaryStdOut.write(readChar);
            characters.add(readChar);
        }
        BinaryStdOut.flush();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if ("-".equals(args[0])) {
            encode();
        } else if ("+".equals(args[0])) {
            decode();
        }
    }

    private static List<Character> getAsciiCharacters() {
        List<Character> characters = new ArrayList<>(R);
        for (int i = 0; i < R; i++) {
            characters.add((char) (255 - i));
        }
        return characters;
    }
}