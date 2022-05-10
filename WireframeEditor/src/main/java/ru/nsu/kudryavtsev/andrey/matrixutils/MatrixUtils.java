package ru.nsu.kudryavtsev.andrey.matrixutils;

public class MatrixUtils {
    public enum Axis {
        X,
        Y,
        Z
    }

    public static Vector VMMul(Vector v, Matrix m) {
        if (v == null || m == null) {
            throw new IllegalArgumentException();
        }
        if (v.getSize() != m.getHeight()) {
            throw new RuntimeException("Wrong size");
        }

        float[] vm = new float[m.getWidth()];
        for (int j = 0; j < m.getWidth(); j++) {
            vm[j] = 0;
            for (int i = 0; i < m.getHeight(); i++) {
                vm[j] += v.getElem(i) * m.getElem(i, j);
            }
        }

        return new Vector(vm);
    }

    public static Vector MVMul(Matrix m, Vector v) {
        if (v == null || m == null) {
            throw new IllegalArgumentException();
        }
        if (v.getSize() != m.getWidth()) {
            throw new RuntimeException("Wrong size");
        }

        float[] mv = new float[m.getHeight()];
        for (int i = 0; i < m.getHeight(); i++) {
            mv[i] = 0;
            for (int j = 0; j < m.getWidth(); j++) {
                mv[i] += m.getElem(i, j) * v.getElem(j);
            }
        }

        return new Vector(mv);
    }

    public static float VVMul(Vector v1, Vector v2) {
        if (v1 == null || v2 == null) {
            throw new IllegalArgumentException();
        }
        if (v1.getSize() != v2.getSize()) {
            throw new RuntimeException("Wrong size");
        }

        float acc = 0;
        for (int i = 0; i < v1.getSize(); i++) {
            acc += v1.getElem(i) * v2.getElem(i);
        }

        return acc;
    }

    public static Matrix MMMul(Matrix m1, Matrix m2) {
        if (m1 == null || m2 == null) {
            throw new IllegalArgumentException();
        }
        if (m1.getWidth() != m2.getHeight()) {
            throw new RuntimeException("Wrong size");
        }

        float[][] m = new float[m1.getHeight()][m2.getWidth()];
        for (int i = 0; i < m1.getHeight(); i++) {
            for (int j = 0; j < m2.getWidth(); j++) {
                m[i][j] = 0;
                for (int k = 0; k < m1.getWidth(); k++) {
                    m[i][j] += m1.getElem(i, k) * m2.getElem(k, j);
                }
            }
        }

        return new Matrix(m);
    }

    public static Matrix createPerspectiveProjectionMatrix(int xf, int xb, int width, int height) {
        return new Matrix(new float[][] {{((float) xb+xf)/(xb-xf),           0,            0, (2f*xb*xf)/(xf-xb)},
                                         {                      0, 2f*xf/width,            0,                  0},
                                         {                      0,           0, 2f*xf/height,                  0},
                                         {                      1,           0,            0,                  0}});
    }

    public static Matrix createTranslateMatrix(float tx, float ty, float tz) {
        return new Matrix(new float[][] {{1, 0, 0, tx},
                                         {0, 1, 0, ty},
                                         {0, 0, 1, tz},
                                         {0, 0, 0,  1}});
    }

    public static Matrix createScaleMatrix(float sx, float sy, float sz) {
        return new Matrix(new float[][] {{sx, 0, 0, 0},
                                         {0, sy, 0, 0},
                                         {0, 0, sz, 0},
                                         {0, 0, 0,  1}});
    }

    public static Matrix createRotateMatrix(double angle, Axis axis, int dir) {
        return switch (axis) {
            case X -> createRotateMatrixX(angle, dir);
            case Y -> createRotateMatrixY(angle, dir);
            case Z -> createRotateMatrixZ(angle, dir);
        };
    }

    private static Matrix createRotateMatrixX(double angle, int dir) {
        return new Matrix(new float[][]{{1,                              0,                               0, 0},
                                        {0,        (float) Math.cos(angle), (float) -Math.sin(angle) * dir, 0},
                                        {0, (float) Math.sin(angle) * dir,         (float) Math.cos(angle), 0},
                                        {0,                              0,                               0, 1}});
    }

    private static Matrix createRotateMatrixY(double angle, int dir) {
        return new Matrix(new float[][]{{       (float) Math.cos(angle), 0,(float) Math.sin(angle) * dir, 0},
                                        {                             0, 1,                            0, 0},
                                        {(float) -Math.sin(angle) * dir, 0,      (float) Math.cos(angle), 0},
                                        {                             0, 0,                            0, 1}});
    }

    private static Matrix createRotateMatrixZ(double angle, int dir) {
        return  new Matrix(new float[][] {{      (float) Math.cos(angle), (float) -Math.sin(angle) * dir, 0, 0},
                                          {(float) Math.sin(angle) * dir,        (float) Math.cos(angle), 0, 0},
                                          {                            0,                              0, 1, 0},
                                          {                            0,                              0, 0, 1}});
    }
}
