package ru.nsu.kudryavtsev.andrey.editdialog;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.kudryavtsev.andrey.matrixutils.Matrix;
import ru.nsu.kudryavtsev.andrey.matrixutils.MatrixUtils;
import ru.nsu.kudryavtsev.andrey.matrixutils.Vector;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class BSpline {
    public static final int DEFAULT_N = 12;
    public static final int DEFAULT_K = 10;
    public static final int SECTION_CONTROL_POINTS = 4;
    private final Matrix M = new Matrix(new float[][] {{-1f/6, 0.5f, -0.5f, 1f/6},
                                                       { 0.5f,   -1,  0.5f,    0},
                                                       {-0.5f,    0,  0.5f,    0},
                                                       { 1f/6, 2f/3,  1f/6,    0}});

    @Getter
    private int K;
    @Getter @Setter
    private int N;
    @Getter
    private final ArrayList<Point> controlPoints;
    @Getter
    private Point[] bSplinePoints;
    private int maxCoord = -1;
    private Float centerX = null;

    public BSpline(ArrayList<Point> controlPoints, int N) {
        this.K = controlPoints.size();
        this.N = N;
        this.controlPoints = controlPoints;
        recalculate();
    }

    public BSpline(Point[] bSplinePoints, ArrayList<Point> controlPoints, int N) {
        this.K = controlPoints.size();
        this.controlPoints = new ArrayList<>();
        this.controlPoints.addAll(controlPoints);

        this.bSplinePoints = Arrays.copyOf(bSplinePoints, bSplinePoints.length);
        this.N = N;
    }

    public BSpline copy() {
        return new BSpline(this.bSplinePoints, this.controlPoints, this.N);
    }

    public static BSpline createDefaultBSpline() {
        ArrayList<Point> controlPoints = new ArrayList<>();
        controlPoints.add(new Point(-461,  192));
        controlPoints.add(new Point(-433,  162));
        controlPoints.add(new Point(-413,  51));
        controlPoints.add(new Point(-364,  16));
        controlPoints.add(new Point(-65,  13));
        controlPoints.add(new Point(162,  40));
        controlPoints.add(new Point(186,  274));
        controlPoints.add(new Point(472,  262));
        controlPoints.add(new Point(544,  244));
        controlPoints.add(new Point(581,  204));
        return new BSpline(controlPoints, DEFAULT_N);
    }

    public void addControlPoints(int pointsN) {
        Point lastControlPoint = controlPoints.get(controlPoints.size()-1);
        for (int i = 0; i < pointsN; i++) {
            Point newControlPoint = new Point(lastControlPoint.x+50*(i+1), lastControlPoint.y);
            controlPoints.add(newControlPoint);
        }
        this.K += pointsN;
    }

    public void removeControlPoints(int pointsN) {
        for (int i = 0; i < pointsN; i++) {
            controlPoints.remove(controlPoints.size()-1);
        }
        this.K -= pointsN;
    }

    public void setK(int K) {
        if (K > this.K) {
            addControlPoints(K - this.K);
        } else if (K < this.K) {
            removeControlPoints(this.K - K);
        }
    }

    public void recalculate() {
        bSplinePoints = new Point[N * (K-3) + 1];
        float[] arrGu = new float[SECTION_CONTROL_POINTS];
        float[] arrGv = new float[SECTION_CONTROL_POINTS];
        for (int sectionNum = 0; sectionNum < K-3; sectionNum++) {
            for (int i = 0; i < SECTION_CONTROL_POINTS; i++) {
                arrGu[i] = (float) controlPoints.get(i+sectionNum).getX();
                arrGv[i] = (float) controlPoints.get(i+sectionNum).getY();
            }
            Vector Gu = new Vector(arrGu);
            Vector Gv = new Vector(arrGv);

            int i;
            Vector T;
            Vector TM;
            for (i = 0; i <= N; i += 1) {
                float j = (float) i/N;
                T = new Vector(new float[] {j*j*j, j*j, j, 1});
                TM = MatrixUtils.VMMul(T, M);
                int u = (int) MatrixUtils.VVMul(TM, Gu);
                int v = (int) MatrixUtils.VVMul(TM, Gv);
                bSplinePoints[i + sectionNum*(N)] = new Point(u, v);
            }
        }
    }

    public float getCenterX() {
        if (centerX != null) {
            return centerX;
        }
        int minX = bSplinePoints[0].x;
        int maxX = bSplinePoints[0].x;
        for (int i = 1; i < bSplinePoints.length; i++) {
            minX = Math.min(minX, bSplinePoints[i].x);
            maxX = Math.max(maxX, bSplinePoints[i].x);
        }
        centerX = (maxX+minX)/2f;
        return centerX;
    }

    public int getMaxCoord() {
        if (this.maxCoord != -1) {
            return this.maxCoord;
        }
        int maxCoord = -1;
        for (Point point : bSplinePoints) {
            maxCoord = Math.max(Math.abs(point.y), maxCoord);
            maxCoord = Math.max(Math.abs(point.x), maxCoord);
        }
        this.maxCoord = maxCoord;
        return this.maxCoord;
    }

//    public Point[] calculate(int sectionNum) {
//        float[] arrGu = new float[SECTION_CONTROL_POINTS];
//        float[] arrGv = new float[SECTION_CONTROL_POINTS];
//        for (int i = 0; i < SECTION_CONTROL_POINTS; i++) {
//            arrGu[i] = (float) controlPoints.get(i+sectionNum).getX();
//            arrGv[i] = (float) controlPoints.get(i+sectionNum).getY();
//        }
//        Vector Gu = new Vector(arrGu);
//        Vector Gv = new Vector(arrGv);
////        for (int i = 0; i < Gu.getSize(); i++) {
////            g2d.drawLine((int) Gu.getElem(i), (int) Gv.getElem(i), (int) Gu.getElem((i+1)%Gu.getSize()), (int) Gv.getElem((i+1)%Gu.getSize()));
////        }
////        Vector T = new Vector(new float[] {0, 0, 0, 1});
////        Vector TM = MatrixUtils.VMMul(T, M);
////        int x = (int) MatrixUtils.VVMul(TM, Gu);
////        int y = (int) MatrixUtils.VVMul(TM, Gv);
//        Point[] points = new Point[N+1];
//        for (int i = 0; i <= N; i += 1) {
//            float j = (float) i/N;
//            Vector T = new Vector(new float[] {j*j*j, j*j, j, 1});
//            Vector TM = MatrixUtils.VMMul(T, M);
//            int u = (int) MatrixUtils.VVMul(TM, Gu);
//            int v = (int) MatrixUtils.VVMul(TM, Gv);
//            points[i] = new Point(u, v);
////            g2d.drawLine(x, y, u, v);
////            x = u;
////            y = v;
//        }
//        return points;
//    }
}
