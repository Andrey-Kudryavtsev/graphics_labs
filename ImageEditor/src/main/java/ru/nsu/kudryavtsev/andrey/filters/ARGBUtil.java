package ru.nsu.kudryavtsev.andrey.filters;

public class ARGBUtil {
    public static int getA(int ARGB) {
        return (ARGB >> 24) & 0xFF;
    }

    public static int getR(int ARGB) {
        return (ARGB >> 16) & 0xFF;
    }

    public static int getG(int ARGB) {
        return (ARGB >> 8) & 0xFF;
    }

    public static int getB(int ARGB) {
        return ARGB & 0xFF;
    }

    public static int getARGB(int R, int G, int B) {
        return getARGB(0xFF, R, G, B);
    }

    public static int getARGB(int A, int R, int G, int B) {
        return (A << 24) | (R << 16) | (G << 8) | B;
    }
}
