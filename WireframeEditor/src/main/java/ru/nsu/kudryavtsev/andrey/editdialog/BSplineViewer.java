package ru.nsu.kudryavtsev.andrey.editdialog;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class BSplineViewer extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {
    private final int CONTROL_POINT_RADIUS = 5;

    @Getter @Setter
    private BSpline bSpline;

    private boolean isMovingPoint = false;
    private int activeControlPointIdx;

    private int lastX, lastY = -1;
    private int prefWidth;
    private int prefHeight;

    // for cartesian coordinates
    private Point origin;
    private int zoom = 1;
    private int lineStep = 150;
    private int valStep = 150;

    private final JScrollPane sp;
    @Setter
    private ActivePointListener activePointListener;

    public BSplineViewer(JScrollPane sp, int width, int height) {
        prefWidth = width;
        prefHeight = height;
        setSize(new Dimension(prefWidth, prefHeight));
        setPreferredSize(new Dimension(prefWidth, prefHeight));
        origin = new Point(prefWidth/2, prefHeight/2);

        this.sp = sp;
        this.sp.setWheelScrollingEnabled(false);
        this.sp.setDoubleBuffered(true);
        this.sp.setViewportView(this);
        this.sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        this.sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        sp.revalidate();

        bSpline = BSpline.createDefaultBSpline();
        activeControlPointIdx = bSpline.getK()-1;

        addMouseListener(this);
        addMouseMotionListener(this);
//        addMouseWheelListener(this);
    }

    public void setK(int K) {
        bSpline.setK(K);
        activeControlPointIdx = bSpline.getK()-1;

        bSpline.recalculate();
        repaint();
    }

    public void setN(int N) {
        bSpline.setN(N);

        bSpline.recalculate();
        repaint();
    }

    public void setActiveControlPointX(int x) {
        Point activeControlPoint = getActivePoint();
        activeControlPoint.move(x, activeControlPoint.y);
        bSpline.recalculate();
        repaint();
    }

    public void setActiveControlPointY(int y) {
        Point activeControlPoint = getActivePoint();
        activeControlPoint.move(activeControlPoint.x, y);
        bSpline.recalculate();
        repaint();
    }

    public Point getActivePoint() {
        return bSpline.getControlPoints().get(activeControlPointIdx);
    }

    /////////// TODO: make it through TransformUtils
    private int inW_x(int x) {
        return x+origin.x;
    }

    private int inW_y(int y) {
        return origin.y-y;
    }

    private Point inW_p(Point p) {
        return new Point(inW_x(p.x), inW_y(p.y));
    }

    private int inL_x(int x) {
        return x-origin.x;
    }

    private int inL_y(int y) {
        return origin.y-y;
    }
    ////////////

    private void drawCartesianCoordinates(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        Dimension panelDimension = new Dimension(getWidth(), getHeight());
        int marginX = 5;
        int marginY = 15;

        g2d.setColor(Color.LIGHT_GRAY);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine(origin.x, 0, origin.x, panelDimension.height-1);
        g2d.drawLine(0, origin.y, panelDimension.width-1, origin.y);

        g2d.setColor(Color.GRAY);
        g2d.setStroke(new BasicStroke(1));
        int i = 1;
        for (int x = origin.x+lineStep; x < panelDimension.width; x+=lineStep) {
            g2d.drawLine(x, 0, x, panelDimension.height-1);
            g2d.drawString(Integer.toString(i*valStep*zoom), x+marginX, origin.y+marginY);
            i++;
        }
        i = 1;
        for (int x = origin.x-lineStep; x >= 0; x-=lineStep) {
            g2d.drawLine(x, 0, x, panelDimension.height-1);
            g2d.drawString(Integer.toString(-i*valStep*zoom), x+marginX, origin.y+marginY);
            i++;
        }
        i = 1;
        for (int y = origin.y+lineStep; y < panelDimension.height; y+=lineStep) {
            g2d.drawLine(0, y, panelDimension.width-1, y);
            g2d.drawString(Integer.toString(-i*valStep*zoom), origin.x+marginX, y+marginY);
            i++;
        }
        i = 1;
        for (int y = origin.y-lineStep; y >= 0; y-=lineStep) {
            g2d.drawLine(0, y, panelDimension.width-1, y);
            g2d.drawString(Integer.toString(i*valStep*zoom), origin.x+marginX, y+marginY);
            i++;
        }
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
    }

    private void drawControlPoints(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.RED);

        int i;
        int diam = CONTROL_POINT_RADIUS*2;
        ArrayList<Point> controlPoints = bSpline.getControlPoints();
        for (i = 0; i < bSpline.getK() - 1; i++) {
            var curPoint = inW_p(controlPoints.get(i));
            var nextPoint = inW_p(controlPoints.get(i+1));
            g2d.draw(new Ellipse2D.Float(curPoint.x-CONTROL_POINT_RADIUS, curPoint.y-CONTROL_POINT_RADIUS, diam, diam));
            g2d.drawLine(curPoint.x, curPoint.y, nextPoint.x, nextPoint.y);
        }
        g2d.draw(new Ellipse2D.Float(inW_p(controlPoints.get(i)).x-CONTROL_POINT_RADIUS, inW_p(controlPoints.get(i)).y-CONTROL_POINT_RADIUS, diam, diam));
        // draw active control point
        g2d.setColor(Color.GREEN);
        Point activeControlPoint = inW_p(controlPoints.get(activeControlPointIdx));
        g2d.draw(new Ellipse2D.Float(activeControlPoint.x-CONTROL_POINT_RADIUS, activeControlPoint.y-CONTROL_POINT_RADIUS, diam, diam));

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }

    private void drawBSpline(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.BLUE);
        g2d.setStroke(new BasicStroke(2));

//        bSpline.recalculate();
        Point[] bSplinePoints = bSpline.getBSplinePoints();
        for (int i = 0; i < bSplinePoints.length-1; i++) {
            Point curP = inW_p(bSplinePoints[i]);
            Point nextP = inW_p(bSplinePoints[i+1]);
            g2d.drawLine(curP.x, curP.y, nextP.x, nextP.y);
        }

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        setSize(new Dimension(prefWidth, prefHeight));
        setPreferredSize(new Dimension(prefWidth, prefHeight));
        setBackground(Color.BLACK);

        Graphics2D g2d = (Graphics2D) g;
        drawCartesianCoordinates(g2d);
        drawControlPoints(g2d);
        drawBSpline(g2d);

        sp.revalidate();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    private int getSelectedPointIdx(int x, int y) {
        int selectionRadius = CONTROL_POINT_RADIUS + 4;
        ArrayList<Point> controlPoints = bSpline.getControlPoints();
        for (int i = 0; i < controlPoints.size(); i++) {
            if (inW_p(controlPoints.get(i)).distance(x, y) <= selectionRadius) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void mousePressed(MouseEvent e) {
//        System.out.println("Pressed on " + e.getX() + " " + e.getY());
        int activeControlPointIdx = getSelectedPointIdx(e.getX(), e.getY());
        if (activeControlPointIdx != -1) {
            this.activeControlPointIdx = activeControlPointIdx;
            isMovingPoint = true;
            activePointListener.activePointChanged(getActivePoint());
        } else {
            isMovingPoint = false;
        }
        lastX = e.getX();
        lastY = e.getY();
        repaint();
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

    private void translateControlPoints(int dx, int dy) {
        if (dx == 0 && dy == 0) {
            return;
        }

        ArrayList<Point> controlPoints = bSpline.getControlPoints();
        for (var point : controlPoints) {
            point.translate(dx, dy);
        }
    }

    private boolean insideViewport(int x, int y) {
        Dimension viewportDimension = new Dimension(sp.getViewport().getWidth(), sp.getViewport().getHeight());
        Point viewPosition = sp.getViewport().getViewPosition();
        return (x >= viewPosition.x + CONTROL_POINT_RADIUS &&
                x < viewPosition.x + viewportDimension.width - CONTROL_POINT_RADIUS &&
                y >= viewPosition.y + CONTROL_POINT_RADIUS &&
                y < viewPosition.y + viewportDimension.height - CONTROL_POINT_RADIUS);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int curX = e.getX();
        int curY = e.getY();
        if (isMovingPoint) {
            if (insideViewport(curX, curY)) {
                bSpline.getControlPoints().get(activeControlPointIdx).move(inL_x(curX), inL_y(curY));
                bSpline.recalculate();
                activePointListener.activePointChanged(bSpline.getControlPoints().get(activeControlPointIdx));
            }
        } else {
            Point viewPosition = sp.getViewport().getViewPosition();
            int dx = lastX - curX;
            int dy = lastY - curY;
            int maxVisibleX = viewPosition.x + sp.getViewport().getWidth();
            int maxVisibleY = viewPosition.y + sp.getViewport().getHeight();
            Dimension panelDimension = new Dimension(getWidth(), getHeight());
            if (dx >= 0 && maxVisibleX == panelDimension.width) {
                prefWidth += dx;
            } else if (dx < 0 && viewPosition.x == 0) {
                prefWidth -= dx;
//                translateControlPoints(-dx, 0);
                origin.translate(-dx, 0);
                lastX -= dx;
            }
            if (dy >= 0 && maxVisibleY == panelDimension.height) {
                prefHeight += dy;
            } else if (dy < 0 && viewPosition.y == 0) {
                prefHeight -= dy;
//                translateControlPoints(0, -dy);
                origin.translate(0, -dy);
                lastY -= dy;
            }

            sp.getHorizontalScrollBar().setValue(viewPosition.x + dx);
            sp.getVerticalScrollBar().setValue(viewPosition.y + dy);
        }
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int nextZoom = zoom + e.getWheelRotation();
        if (nextZoom < 1 || nextZoom > 10) {
            return;
        }
        zoom = nextZoom;
        repaint();
    }
}
