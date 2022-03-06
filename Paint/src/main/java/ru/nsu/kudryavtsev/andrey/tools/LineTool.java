package ru.nsu.kudryavtsev.andrey.tools;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.kudryavtsev.andrey.tooldialog.LineToolDialog;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class LineTool implements Tool {
    private int x = -1;
    private int y = -1;
    private int thickness = 1;
    private Color color = Color.BLACK;
    @Getter
    private final LineToolDialog dialog = new LineToolDialog();

    @Override
    public void applyChanges() {
        thickness = dialog.getThickness();
    }

    @Override
    public void mousePressed(MouseEvent e, Color color, BufferedImage img) {
        this.color = color;
        if (x <= -1) {
            x = e.getX();
            y = e.getY();
        } else {
            if (thickness == 1) {
                bresenhamLine(x, y, e.getX(), e.getY(), img);
            } else {
                Graphics2D g2d = (Graphics2D) img.getGraphics();
                g2d.setStroke(new BasicStroke(thickness));
                g2d.setColor(color);
                g2d.drawLine(x, y, e.getX(), e.getY());
            }
            x = -1;
        }
    }

    public void bresenhamLine(int x1, int y1, int x2, int y2, BufferedImage img) {
        boolean diagReflect = (Math.abs(x2 - x1) < Math.abs(y2 - y1));
        if (diagReflect) {
            int tmp = y1;
            y1 = x1;
            x1 = tmp;

            tmp = y2;
            y2 = x2;
            x2 = tmp;
        }
        int x = x1;
        int y = y1;
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int ystep = (y1 > y2) ? -1 : 1;
        int xstep = (x1 > x2) ? -1 : 1;
        int err = -dx;
        for (int i = 0; i < dx; i++) {
            x += xstep;
            err += 2*dy;
            if (err > 0) {
                err -= 2*dx;
                y += ystep;
            }
            img.setRGB(diagReflect ? y : x, diagReflect ? x : y, color.getRGB());
        }
    }
}
