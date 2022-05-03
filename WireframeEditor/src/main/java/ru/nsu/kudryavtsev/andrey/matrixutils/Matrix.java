package ru.nsu.kudryavtsev.andrey.matrixutils;

import lombok.Getter;

import java.util.Arrays;

public class Matrix {
    private final float m[][];
    @Getter
    private final int width;
    @Getter
    private final int height;

    public Matrix(float[][] m) {
        this.m = m;
        width = m[0].length;
        height = m.length;
    }

    public float getElem(int i, int j) {
        if (i < 0 || i >= height || j < 0 || j >= width) {
            throw new IndexOutOfBoundsException();
        }

        return m[i][j];
    }

    @Override
    public String toString() {
        StringBuilder matrix = new StringBuilder();
        int i;
        matrix.append("[").append(Arrays.toString(m[0])).append(",").append("\n");
        for (i = 1; i < height-1; i++) {
            matrix.append(" ").append(Arrays.toString(m[i])).append(",").append("\n");
        }
        matrix.append(" ").append(Arrays.toString(m[i])).append("]").append("\n");
        return matrix.toString();
    }
}
