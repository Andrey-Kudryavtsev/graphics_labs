package ru.nsu.kudryavtsev.andrey.filters;

import lombok.Getter;
import ru.nsu.kudryavtsev.andrey.filterdialog.RotateFilterDialog;

import java.awt.*;
import java.awt.image.BufferedImage;

public class RotateFilter implements Filter {
    private int theta;
    @Getter
    private final RotateFilterDialog dialog;

    public RotateFilter(int theta) {
        this.theta = theta;
        dialog = new RotateFilterDialog(theta);
    }

    @Override
    public BufferedImage getAlteredImage(BufferedImage img) {
        if (img == null) {
            return null;
        }

        int width = img.getWidth();
        int height = img.getHeight();
        double toRad = Math.toRadians(theta);
        int hPrime = (int) (width * Math.abs(Math.sin(toRad)) + height * Math.abs(Math.cos(toRad)));
        int wPrime = (int) (height * Math.abs(Math.sin(toRad)) + width * Math.abs(Math.cos(toRad)));

        BufferedImage copyImg = new BufferedImage(hPrime, wPrime, img.getType());
        Graphics2D g2d = copyImg.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, copyImg.getWidth(), copyImg.getHeight());
        g2d.translate(wPrime/2, hPrime/2);
        g2d.rotate(Math.toRadians(theta));
        g2d.translate(-width/2, -height/2);
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();

        return copyImg;
    }

    @Override
    public void applyChanges() {
        theta = dialog.getTheta();
    }

    @Override
    public void resetChanges() {
        dialog.setTheta(theta);
    }
}
