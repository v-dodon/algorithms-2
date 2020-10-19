package org.coursera.algorithms.week_4;

import edu.princeton.cs.algs4.SET;

public class BoggleSolver {
    private final MyTrieST<String> dictionary;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        this.dictionary = new MyTrieST<>();
        for (String word : dictionary) {
            this.dictionary.put(word, word);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        StringBuilder stringBuilder = new StringBuilder();
        SET<String> words = new SET<>();
        boolean[][] marked = new boolean[board.rows()][board.cols()];
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                searchWord(i, j, marked, board, stringBuilder, words);
            }
        }

        return words;
    }

    private void searchWord(int row, int col, boolean[][] marked, BoggleBoard board, StringBuilder stringBuilder, SET<String> words) {
        if (!marked[row][col] && isValidPrefix(stringBuilder.toString() + board.getLetter(row, col))) {
            char letter = board.getLetter(row, col);
            appendLetter(stringBuilder, letter);
            marked[row][col] = true;
        }
        if (dictionary.get(stringBuilder.toString()) != null && stringBuilder.length() >= 3 && !words.contains(stringBuilder.toString())) {
            words.add(stringBuilder.toString());
        }
        if (col > 0 && !marked[row][col - 1] && isValidPrefix(stringBuilder.toString() + board.getLetter(row, col - 1))) {
            char righLetter = board.getLetter(row, col - 1);
            appendLetter(stringBuilder, righLetter);
            marked[row][col - 1] = true;
            searchWord(row, col - 1, marked, board, stringBuilder, words);
        }
        if (col < board.cols() - 1 && !marked[row][col + 1] && isValidPrefix(stringBuilder.toString() + board.getLetter(row, col + 1))) {
            char leftLetter = board.getLetter(row, col + 1);
            appendLetter(stringBuilder, leftLetter);
            marked[row][col + 1] = true;
            searchWord(row, col + 1, marked, board, stringBuilder, words);
        }
        if (row > 0 && !marked[row - 1][col] && isValidPrefix(stringBuilder.toString() + board.getLetter(row - 1, col))) {
            char aboveLetter = board.getLetter(row - 1, col);
            appendLetter(stringBuilder, aboveLetter);
            marked[row - 1][col] = true;
            searchWord(row - 1, col, marked, board, stringBuilder, words);
        }
        if (row < board.rows() - 1 && !marked[row + 1][col] && isValidPrefix(stringBuilder.toString() + board.getLetter(row + 1, col))) {
            char belowLetter = board.getLetter(row + 1, col);
            appendLetter(stringBuilder, belowLetter);
            marked[row + 1][col] = true;
            searchWord(row + 1, col, marked, board, stringBuilder, words);
        }
        if (row > 0 && col > 0 && !marked[row - 1][col - 1] && isValidPrefix(stringBuilder.toString() + board.getLetter(row - 1, col - 1))) {
            char leftUpCorner = board.getLetter(row - 1, col - 1);
            appendLetter(stringBuilder, leftUpCorner);
            marked[row - 1][col - 1] = true;
            searchWord(row - 1, col - 1, marked, board, stringBuilder, words);
        }
        if (row > 0 && col < board.cols() - 1 && !marked[row - 1][col + 1] && isValidPrefix(stringBuilder.toString() + board.getLetter(row - 1, col + 1))) {
            char rightUpCorner = board.getLetter(row - 1, col + 1);
            appendLetter(stringBuilder, rightUpCorner);
            marked[row - 1][col + 1] = true;
            searchWord(row - 1, col + 1, marked, board, stringBuilder, words);
        }
        if (row < board.rows() - 1 && col > 0 && !marked[row + 1][col - 1] && isValidPrefix(stringBuilder.toString() + board.getLetter(row + 1, col - 1))) {
            char leftDownCorner = board.getLetter(row + 1, col - 1);
            appendLetter(stringBuilder, leftDownCorner);
            marked[row + 1][col - 1] = true;
            searchWord(row + 1, col - 1, marked, board, stringBuilder, words);
        }
        if (row < board.rows() - 1 && col < board.cols() - 1 && !marked[row + 1][col + 1] && isValidPrefix(stringBuilder.toString() + board.getLetter(row + 1, col + 1))) {
            char rightDownCorner = board.getLetter(row + 1, col + 1);
            appendLetter(stringBuilder, rightDownCorner);
            marked[row + 1][col + 1] = true;
            searchWord(row + 1, col + 1, marked, board, stringBuilder, words);
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            if (stringBuilder.length() > 0 && stringBuilder.charAt(stringBuilder.length() - 1) == 'Q') {
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            }
            marked[row][col] = false;
        }
    }

    private void appendLetter(StringBuilder stringBuilder, char letter) {
        stringBuilder.append(letter);
        if (letter == 'Q') {
            stringBuilder.append('U');
        }
    }

    private boolean isValidPrefix(String prefix) {
        if (!prefix.isEmpty() && prefix.charAt(prefix.length() - 1) == 'Q') {
            prefix += 'U';
        }
        return dictionary.isAnyKeyWithPrefix(prefix);
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word.length() >= 3 && dictionary.contains(word)) {
            switch (word.length()) {
                case 3:
                case 4:
                    return 1;
                case 5:
                    return 2;
                case 6:
                    return 3;
                case 7:
                    return 5;
                default:
                    return 11;
            }
        }
        return 0;
    }
}
