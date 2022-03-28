package ru.nsu.kudryavtsev.andrey.filters;

import lombok.Getter;
import ru.nsu.kudryavtsev.andrey.filterdialog.ContouringFilterDialog;

import java.awt.image.BufferedImage;

public class ContouringFilter implements Filter {
    private final double[][] robertsKernel1 = {{0.5,    0},
                                               {  0, -0.5}};
    private final double[][] robertsKernel2 = {{   0, 0.5},
                                               {-0.5,   0}};
    private final double[][] sobelKernel1 = {{-0.25, 0, 0.25},
                                             { -0.5, 0,  0.5},
                                             {-0.25, 0, 0.25}};
    private final double[][] sobelKernel2 = {{-0.25, -0.5, -0.25},
                                             {    0,    0,     0},
                                             { 0.25,  0.5,  0.25}};
    private boolean isSobel;
    private int threshold;
    @Getter
    private final ContouringFilterDialog dialog;

    public ContouringFilter(boolean isSobel, int threshold) {
        this.isSobel = isSobel;
        this.threshold = threshold;
        dialog = new ContouringFilterDialog(isSobel, threshold);
    }

    @Override
    public void applyChanges() {
        isSobel = dialog.isSobel();
        threshold = dialog.getThreshold();
    }

    @Override
    public void resetChanges() {
        dialog.setSobel(isSobel);
        dialog.setThreshold(threshold);
    }

    @Override
    public BufferedImage getAlteredImage(BufferedImage img) {
        if (img == null) {
            return null;
        }

        return isSobel ? sobelAlgorithm(img) : robertsAlgorithm(img);
    }

    private BufferedImage robertsAlgorithm(BufferedImage img) {
        BufferedImage copyImg = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        int size = robertsKernel1.length/2;
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                double newR1 = 0;
                double newG1 = 0;
                double newB1 = 0;
                double newR2 = 0;
                double newG2 = 0;
                double newB2 = 0;
                for (int i = 0; i <= size; i++) {
                    for (int j = 0; j <= size; j++) {
                        int X = Math.min(Math.max(x+j, 0), img.getWidth()-1);
                        int Y = Math.min(Math.max(y+i, 0), img.getHeight()-1);
                        newR1 += ARGBUtil.getR(img.getRGB(X, Y)) * robertsKernel1[i][j];
                        newG1 += ARGBUtil.getG(img.getRGB(X, Y)) * robertsKernel1[i][j];
                        newB1 += ARGBUtil.getB(img.getRGB(X, Y)) * robertsKernel1[i][j];
                        newG2 += ARGBUtil.getG(img.getRGB(X, Y)) * robertsKernel2[i][j];
                        newB2 += ARGBUtil.getB(img.getRGB(X, Y)) * robertsKernel2[i][j];
                        newR2 += ARGBUtil.getR(img.getRGB(X, Y)) * robertsKernel2[i][j];
                    }
                }
                double SR = Math.abs(newR1) + Math.abs(newR2);
                double SG = Math.abs(newG1) + Math.abs(newG2);
                double SB = Math.abs(newB1) + Math.abs(newB2);
                if ((SR+SG+SB)/3 >= threshold) {
                    copyImg.setRGB(x, y, ARGBUtil.getARGB(255, 255, 255));
                } else {
                    copyImg.setRGB(x, y, ARGBUtil.getARGB(0, 0, 0));
                }
            }
        }
        return copyImg;
    }

    private BufferedImage sobelAlgorithm(BufferedImage img) {
        BufferedImage copyImg = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        int radius = sobelKernel1.length/2;
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                double newR1 = 0;
                double newG1 = 0;
                double newB1 = 0;
                double newR2 = 0;
                double newG2 = 0;
                double newB2 = 0;
                for (int i = -radius; i <= radius; i++) {
                    for (int j = -radius; j <= radius; j++) {
                        int X = roundCoord(x+j, 0, img.getWidth()-1);
                        int Y = roundCoord(y+i, 0, img.getHeight()-1);
                        newR1 += ARGBUtil.getR(img.getRGB(X, Y)) * sobelKernel1[i+radius][j+radius];
                        newR2 += ARGBUtil.getR(img.getRGB(X, Y)) * sobelKernel2[i+radius][j+radius];
                        newG1 += ARGBUtil.getG(img.getRGB(X, Y)) * sobelKernel1[i+radius][j+radius];
                        newG2 += ARGBUtil.getG(img.getRGB(X, Y)) * sobelKernel2[i+radius][j+radius];
                        newB1 += ARGBUtil.getB(img.getRGB(X, Y)) * sobelKernel1[i+radius][j+radius];
                        newB2 += ARGBUtil.getB(img.getRGB(X, Y)) * sobelKernel2[i+radius][j+radius];
                    }
                }
                double SR = Math.abs(newR1) + Math.abs(newR2);
                double SG = Math.abs(newG1) + Math.abs(newG2);
                double SB = Math.abs(newB1) + Math.abs(newB2);
                if ((SR+SG+SB)/3 >= threshold) {
                    copyImg.setRGB(x, y, ARGBUtil.getARGB(255, 255, 255));
                } else {
                    copyImg.setRGB(x, y, ARGBUtil.getARGB(0, 0, 0));
                }
            }
        }
        return copyImg;
    }
}
