package org.coursera.algorithms.week_2;

import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {

    private static final double BORDER_ENERGY = 1000.0;
    private Picture picture;
    private double[][] energyArr;
    private int[][] xTo;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException();
        }
        this.picture = picture;
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || y < 0 || x > width() - 1 || y > height() - 1) {
            throw new IllegalArgumentException();
        }
        if (x == 0 || y == 0 || x == width() - 1 || y == height() - 1) {
            return BORDER_ENERGY;
        }

        int deltax = getDelta(picture.get(x - 1, y), picture.get(x + 1, y));
        int deltaY = getDelta(picture.get(x, y - 1), picture.get(x, y + 1));
        double sum = deltax + deltaY;

        return Math.sqrt(sum);
    }

    private int getDelta(Color prevColor, Color nextColor) {
        int red = nextColor.getRed() - prevColor.getRed();
        int green = nextColor.getGreen() - prevColor.getGreen();
        int blue = nextColor.getBlue() - prevColor.getBlue();
        return red * red + green * green + blue * blue;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        Picture original = picture();
        transpose(original);
        int[] seam = findVerticalSeam();
        picture = original;
        return seam;
    }

    private void transpose(Picture original) {
        picture = new Picture(height(), width());
        for (int i = 0; i < picture.width(); i++) {
            for (int j = 0; j < picture.height(); j++) {
                picture.set(i, j, original.get(j, i));
            }
        }
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        energyArr = new double[width()][height()];
        xTo = new int[width()][height()];
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                energyArr[i][j] = Double.POSITIVE_INFINITY;
            }
        }

        for (int i = 0; i < energyArr.length; i++) {
            energyArr[i][0] = BORDER_ENERGY;
        }

        for (int i = 0; i < height() - 1; i++) {
            for (int j = 0; j < width(); j++) {
                if (j > 0) {
                    relax(j, i, j - 1, i + 1);
                }
                relax(j, i, j, i + 1);
                if (j < width() - 1) {
                    relax(j, i, j + 1, i + 1);
                }
            }
        }

        double minEnergy = Double.POSITIVE_INFINITY;
        int index = -1;
        for (int i = 0; i < width(); i++) {
            if (energyArr[i][height() - 1] < minEnergy) {
                minEnergy = energyArr[i][height() - 1];
                index = i;
            }
        }
        int[] seam = new int[height()];
        if (index == -1) {
            return seam;
        }

        int prev = xTo[index][height() - 1];
        seam[height() - 1] = index;

        for (int i = height() - 2; i > 0; i--) {
            seam[i] = prev;
            prev = xTo[prev][i];
        }
        seam[0] = prev;
        return seam;
    }

    private void relax(int x1, int y1, int x2, int y2) {
        if (energyArr[x2][y2] > energyArr[x1][y1] + energy(x2, y2)) {
            energyArr[x2][y2] = energyArr[x1][y1] + energy(x2, y2);
            xTo[x2][y2] = x1;
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null || height() <= 1 || seam.length != width() || checkSeamSequence(seam)) {
            throw new IllegalArgumentException();
        }

        Picture original = picture();
        transpose(original);
        removeVerticalSeam(seam);
        transpose(picture);
    }

    private boolean checkSeamSequence(int[] seam){
        for (int i = 0; i < seam.length; i++) {
            if(i < seam.length - 1 && (-1 > seam[i + 1] - seam[i] || 1 < seam[i + 1] - seam[i])) {
                return true;
            }
        }
        return false;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null || width() <= 1 || seam.length != height() || checkSeamSequence(seam)) {
            throw new IllegalArgumentException();
        }

        Picture original = picture();
        Picture resized = new Picture(original.width() - 1, original.height());
        for (int i = 0; i < resized.height(); i++) {
            for (int j = 0; j < seam[i]; j++) {
                resized.set(j, i, original.get(j, i));
            }
            for (int j = seam[i]; j < resized.width(); j++) {
                resized.set(j, i, original.get(j + 1, i));
            }
        }

        picture = resized;
    }
}