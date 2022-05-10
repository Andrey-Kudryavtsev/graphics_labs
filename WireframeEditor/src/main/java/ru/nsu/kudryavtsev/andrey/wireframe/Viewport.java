package ru.nsu.kudryavtsev.andrey.wireframe;

import lombok.Getter;
import ru.nsu.kudryavtsev.andrey.matrixutils.Matrix;
import ru.nsu.kudryavtsev.andrey.matrixutils.MatrixUtils;
import ru.nsu.kudryavtsev.andrey.matrixutils.Vector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Viewport extends JPanel implements MouseWheelListener, MouseMotionListener, MouseListener {
    @Getter
    private Wireframe wireframe;
    private final Camera camera;
    private  int zoom = 100;

    int lastX = -1;
    int lastY = -1;

    private Matrix rotationMatrix = new Matrix(new float[][] {{1, 0, 0, 0},
                                                              {0, 1, 0, 0},
                                                              {0, 0, 1, 0},
                                                              {0, 0, 0, 1}});

    public void setWireframe(Wireframe wireframe) {
        this.wireframe = wireframe;
        repaint();
    }

    public Viewport(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        wireframe = Wireframe.createDefaultWireframe();
        camera = new Camera(new Point3D(-10, 0, 0));

        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
    }

    public void resetRotation() {
        rotationMatrix = new Matrix(new float[][] {{1, 0, 0, 0},
                                                   {0, 1, 0, 0},
                                                   {0, 0, 1, 0},
                                                   {0, 0, 0, 1}});
        repaint();
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        setBackground(Color.WHITE);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);

        Matrix Mpersp = calcPerspectiveProjectionMatrix();
        var bSplinePoints = wireframe.getGeneratrix().getBSplinePoints();

        for (int j = 0; j < wireframe.getM1(); j++) {
            Matrix Rj = calcResultMatrix(Mpersp, j, wireframe.getM1());
            Point pp = bSplinePoints[0];
            Point ppdraw = getProjPoint(Rj, pp);
            for (int i = 1; i < bSplinePoints.length; i++) {
                Point cp = bSplinePoints[i];
                Point cpdraw = getProjPoint(Rj, cp);
                g2d.drawLine(ppdraw.x, ppdraw.y, cpdraw.x, cpdraw.y);

                ppdraw = cpdraw;
            }
        }

        int circleSections = wireframe.getM1()*wireframe.getM2();
        int N = wireframe.getGeneratrix().getN();
        int K = wireframe.getGeneratrix().getK();
        int circleN = K-2;

        Matrix Ri = calcResultMatrix(Mpersp, 0, circleSections);
        Point[] pps = new Point[circleN];
        for (int circleIdx = 0; circleIdx < circleN; circleIdx++) {
            int ppidx = circleIdx*N;
            Point pp = bSplinePoints[ppidx];
            Point ppdraw = getProjPoint(Ri, pp);
            pps[circleIdx] = ppdraw;
        }
        for (int i = 1; i <= circleSections; i++) {
            Ri = calcResultMatrix(Mpersp, i, circleSections);
            for (int circleIdx = 0; circleIdx < circleN; circleIdx++) {
                int cpidx = circleIdx*N;
                Point cp = bSplinePoints[cpidx];
                Point cpdraw = getProjPoint(Ri, cp);
                Point ppdraw = pps[circleIdx];
                g2d.drawLine(ppdraw.x, ppdraw.y, cpdraw.x, cpdraw.y);
                pps[circleIdx] = cpdraw;
            }
        }
    }

    private Point getProjPoint(Matrix matrix, Point point) {
        Vector CVVPoint = MatrixUtils.MVMul(matrix, new Vector(new float[] {point.x, point.y, 1, 1}));

        float a = (1f*getWidth()) / (CVVPoint.getElem(3) * 2f);
        float b = (1f*getHeight()) / (CVVPoint.getElem(3) * 2f);
        Matrix normalizeDisplayMatrix = new Matrix(new float[][] {{1f/CVVPoint.getElem(3), 0,  0,                        0},
                                                                  {                        0, a,  0,                        a},
                                                                  {                        0, 0, -b,                        b},
                                                                  {                        0, 0,  0, 1f/CVVPoint.getElem(3)}});
        Vector projPoint = MatrixUtils.MVMul(normalizeDisplayMatrix, CVVPoint);
        return new Point((int) projPoint.getElem(1), (int) projPoint.getElem(2));
    }

    private Matrix calcPerspectiveProjectionMatrix() {
        int boxSize = wireframe.getGeneratrix().getMaxCoord() * 2;
        int xf = 10 - camera.getPosition().x + zoom;
        int xb = xf + boxSize*2;
        Matrix perspectiveProjectionMatrix = MatrixUtils.createPerspectiveProjectionMatrix(xf, xb, getWidth(), getHeight());

        Matrix worldAndCameraMatrix = MatrixUtils.createTranslateMatrix(xf + boxSize - camera.getPosition().x, 0, 0);

        float centerZ = wireframe.getGeneratrix().getCenterX();
        Matrix centerMatrix = MatrixUtils.createTranslateMatrix(0, 0, -centerZ);

        Matrix P_WC = MatrixUtils.MMMul(perspectiveProjectionMatrix, worldAndCameraMatrix);
        Matrix P_WC_R = MatrixUtils.MMMul(P_WC, rotationMatrix);
        Matrix P_WC_R_C = MatrixUtils.MMMul(P_WC_R, centerMatrix);

        return P_WC_R_C;
    }

    private Matrix calcResultMatrix(Matrix M, int j, int sections) {
        double angle = Math.toRadians((j*360.0)/sections);
        Matrix Mto3d = new Matrix(new float[][] {{0, (float) Math.cos(angle), 0, 0},
                                                 {0, (float) Math.sin(angle), 0, 0},
                                                 {1,                       0, 0, 0},
                                                 {0,                       0, 0, 1}});

        return MatrixUtils.MMMul(M, Mto3d);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int scaleFactor = 100;
        if (zoom + e.getWheelRotation()*scaleFactor < 100) {
            return;
        }
        zoom += e.getWheelRotation() * scaleFactor;
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        lastX = e.getX();
        lastY = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int rotFactor = 10;
        int curX = e.getX();
        int curY = e.getY();
        int dx = curX - lastX;
        int dy = curY - lastY;
        double angleX = Math.toRadians((double) dx/rotFactor);
        double angleY = Math.toRadians((double) dy/rotFactor);
        Matrix rot1;
        if (SwingUtilities.isRightMouseButton(e)) {
            rot1 = MatrixUtils.createRotateMatrix(angleY, MatrixUtils.Axis.X, -1);
        } else {
            rot1 = MatrixUtils.createRotateMatrix(angleY, MatrixUtils.Axis.Y, -1);
        }
        Matrix rot2 = MatrixUtils.createRotateMatrix(angleX, MatrixUtils.Axis.Z, -1);
        Matrix rot = MatrixUtils.MMMul(rot1, rot2);
        rotationMatrix = MatrixUtils.MMMul(rot, rotationMatrix);
        lastX = curX;
        lastY = curY;
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
