/******************************************************************************
 *  Name: Bisrat Zerihun
 *  Date: 10/01/2021
 *
 *
 *  finds a seam from a picture then deletes it
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    private Picture pic;
    private int width;
    private int height;
    private double[][] seamMap;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException("null parameter");
        this.pic = new Picture(picture);
        this.seamMap = new double[this.pic.width()][this.pic.height()];
        this.width = seamMap.length;
        this.height = seamMap[0].length;
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                seamMap[i][j] = energy(i, j);
            }
        }

    }

    // current picture
    public Picture picture() {
        return new Picture(this.pic);
    }

    // width of current picture
    public int width() {
        return this.width;
    }

    // height of current picture
    public int height() {
        return this.height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x >= width() || x < 0 || y >= height() || y < 0) {
            throw new IllegalArgumentException("not prescribed width or height");
        }
        if (x >= width() - 1 || x <= 0 || y <= 0 || y >= height() - 1) {
            return 1000;
        }
        if (this.seamMap[x][y] != 0) return this.seamMap[x][y];
        Color left = this.pic.get(x + 1, y);
        Color right = this.pic.get(x - 1, y);
        Color top = this.pic.get(x, y + 1);
        Color bottom = this.pic.get(x, y - 1);
        int xRed = left.getRed() - right.getRed();
        int xGreen = left.getGreen() - right.getGreen();
        int xBlue = left.getBlue() - right.getBlue();
        int yRed = top.getRed() - bottom.getRed();
        int yGreen = top.getGreen() - bottom.getGreen();
        int yBlue = top.getBlue() - bottom.getBlue();
        return Math.sqrt(Math.pow(xRed, 2) + Math.pow(xGreen, 2) + Math.pow(xBlue, 2)
                                 + Math.pow(yRed, 2) + Math.pow(yGreen, 2) + Math.pow(yBlue, 2));

    }


    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[] seam = new int[height()];
        double[] distTo = new double[height() * width()];
        int[] edgeTo = new int[height() * width()];
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                if (j == 0) distTo[numerateMatrix(i, j)] = 0;
                else distTo[numerateMatrix(i, j)] = Double.POSITIVE_INFINITY;
                edgeTo[numerateMatrix(i, j)] = numerateMatrix(i, j);
            }

        }
        for (int j = 0; j < height() - 1; j++) {
            for (int i = 0; i < width(); i++) {

                if (i < width() - 1)
                    relaxEdge(numerateMatrix(i, j), numerateMatrix(i + 1, j + 1), distTo, edgeTo,
                              this.seamMap[i + 1][j + 1]);
                relaxEdge(numerateMatrix(i, j), numerateMatrix(i, j + 1), distTo, edgeTo,
                          this.seamMap[i][j + 1]);
                if (i > 0)
                    relaxEdge(numerateMatrix(i, j), numerateMatrix(i - 1, j + 1), distTo, edgeTo,
                              this.seamMap[i - 1][j + 1]);

            }
        }
        Double min = Double.POSITIVE_INFINITY;
        int index = -1;
        for (int i = 0; i < width(); i++) {
            double currentDist = distTo[numerateMatrix(i, height() - 1)];
            if (min > currentDist) {
                min = currentDist;
                index = i;

            }
        }
        int i = seam.length - 1;
        int p = -1;
        for (p = numerateMatrix(index, height() - 1); p != edgeTo[p];
             p = edgeTo[p]) {
            seam[i--] = p % width();
        }
        seam[0] = edgeTo[p];
        return seam;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        double[][] temp = this.seamMap;
        transpose();
        int[] seams = findVerticalSeam();
        transpose();
        return seams;

    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        transpose();
        removeVerticalSeam(seam);
        transpose();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException("illegal parameter");
        if (seam.length != height())
            throw new IllegalArgumentException("illegal number of inputs");
        if (width() <= 1) throw new IllegalArgumentException("invalid picture");
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1)
                throw new IllegalArgumentException("not valid seam");
        }
        Picture updatedPicture = new Picture(width() - 1, height());
        double[][] updatedSeamMap = new double[width() - 1][height()];
        for (int j = 0; j < seam.length; j++) {
            for (int i = 0; i < seam[j]; i++) {
                updatedPicture.set(i, j, this.pic.get(i, j));
                updatedSeamMap[i][j] = this.seamMap[i][j];

            }
        }

        for (int j = 0; j < seam.length; j++) {
            for (int i = seam[j]; i < updatedPicture.width(); i++) {
                updatedPicture.set(i, j, this.pic.get(i + 1, j));
                updatedSeamMap[i][j] = this.seamMap[i + 1][j];
            }
        }

        this.pic = updatedPicture;
        this.width--;
        this.seamMap = updatedSeamMap;
    }

    private int numerateMatrix(int x, int y) {
        return ((y) * width()) + x;
    }

    private void relaxEdge(int from, int to, double[] distTo, int[] edgeTo, double toPixel) {
        if (distTo[to] > distTo[from] + toPixel) {
            distTo[to] = distTo[from] + toPixel;
            edgeTo[to] = from;
        }
    }

    // transposes the picture
    private void transpose() {
        double[][] temp = new double[height()][width()];
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[0].length; j++) {
                temp[i][j] = this.seamMap[j][i];
            }
        }
        Picture transPicture = new Picture(height(), width());
        for (int i = 0; i < this.pic.width(); i++) {
            for (int j = 0; j < this.pic.height(); j++) {
                transPicture.set(j, i, this.pic.get(i, j));
            }
        }
        this.pic = transPicture;
        this.seamMap = temp;
        this.width = seamMap.length;
        this.height = seamMap[0].length;
    }


    //  unit testing (optional)
    public static void main(String[] args) {
        // Picture cham = new Picture(new File("chameleon.png"));
        // SeamCarver seam = new SeamCarver(cham);
        // int[] seams = seam.findHorizontalSeam();
        // seam.removeHorizontalSeam(seams);
        // seam.picture().show();

    }


}
