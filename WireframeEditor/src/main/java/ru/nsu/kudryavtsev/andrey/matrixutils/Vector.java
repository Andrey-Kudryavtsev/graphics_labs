package ru.nsu.kudryavtsev.andrey.matrixutils;

import lombok.Getter;

import java.util.Arrays;

public class Vector {
    private float[] v;
    @Getter
    private int size;

    public Vector(float[] v) {
        this.v = v;
        size = v.length;
    }

    public float getElem(int i) {
        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException();
        }

        return v[i];
    }

    @Override
    public String toString() {
        return Arrays.toString(v);
    }
}
