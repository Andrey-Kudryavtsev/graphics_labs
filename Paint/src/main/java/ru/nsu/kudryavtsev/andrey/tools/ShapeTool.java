package ru.nsu.kudryavtsev.andrey.tools;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.kudryavtsev.andrey.tooldialog.ShapeToolDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class ShapeTool implements Tool {
    private int N = 5;
    private int R = 100;
    private int r = (int) (0.4 * R);
    private int phi = -90;
    private boolean isStar = false;
    private Color color = Color.BLACK;
    @Getter
    private final ShapeToolDialog dialog = new ShapeToolDialog();

    @Override
    public void applyChanges() {
        N = dialog.getN();
        R = dialog.getR();
        r = (int) (0.4 * R);
        phi = -90 + dialog.getPhi();
        isStar = dialog.isStar();
    }

    @Override
    public void mousePressed(MouseEvent e, Color color, BufferedImage img) {
        this.color = color;
        if (isStar) {
            drawRegularStar(e.getX(), e.getY(), img);
        } else {
            drawRegularPolygon(e.getX(), e.getY(), img);
        }
    }

    private void drawRegularPolygon(int centerX, int centerY, BufferedImage img) {
        Graphics2D g2d = (Graphics2D) img.getGraphics();
        int[] x = new int[N];
        int[] y = new int[N];
        for (int i = 0; i < N; i++) {
            x[i] = (int) Math.round(centerX + R*Math.cos(Math.toRadians(phi) + 2*Math.PI*i/N));
            y[i] = (int) Math.round(centerY + R*Math.sin(Math.toRadians(phi) + 2*Math.PI*i/N));
        }
        g2d.setColor(color);
        g2d.drawPolygon(new Polygon(x, y, N));
    }

    private void drawRegularStar(int centerX, int centerY, BufferedImage img) {
        Graphics2D g2d = (Graphics2D) img.getGraphics();
        int[] x = new int[2*N];
        int[] y = new int[2*N];
        for (int i = 0; i < 2*N; i+=2) {
            x[i] = (int) Math.round(centerX + R*Math.cos( Math.toRadians(phi) + Math.PI*i/N ));
            x[i+1] = (int) Math.round(centerX + r*Math.cos( Math.toRadians(phi) + Math.PI*(i+1)/N ));
            y[i] = (int) Math.round(centerY + R*Math.sin( Math.toRadians(phi) + Math.PI*i/N ));
            y[i+1] = (int) Math.round(centerY + r*Math.sin( Math.toRadians(phi) + Math.PI*(i+1)/N ));
        }
        g2d.setColor(color);
        g2d.drawPolygon(new Polygon(x, y, 2*N));
    }
}
