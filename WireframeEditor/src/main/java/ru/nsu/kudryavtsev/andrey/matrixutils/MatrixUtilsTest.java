package ru.nsu.kudryavtsev.andrey.matrixutils;

public class MatrixUtilsTest {
    public static void main(String[] args) {
        Matrix m = new Matrix(new float[][] {{1, 2, 3},
                                             {4, 5, 6}});
        Vector v = new Vector(new float[] {1, 1, 1});
        Vector vr = MatrixUtils.MVMul(m, v);
        System.out.println(vr);
    }
}
