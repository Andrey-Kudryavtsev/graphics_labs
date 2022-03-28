package ru.nsu.kudryavtsev.andrey.filters;

import lombok.Getter;
import ru.nsu.kudryavtsev.andrey.filterdialog.BlurFilterDialog;

import java.awt.image.BufferedImage;

public class BlurFilter implements Filter {
    private int radius;
    private double sigma;
    @Getter
    private BlurFilterDialog dialog;

    public BlurFilter(int radius, double sigma) {
        this.radius = radius;
        this.sigma = sigma;
        dialog = new BlurFilterDialog(radius, sigma);
    }

    @Override
    public void applyChanges() {
        radius = dialog.getRadius();
        sigma = dialog.getSigma();
    }

    @Override
    public void resetChanges() {
        dialog.setRadius(radius);
        dialog.setSigma(sigma);
    }

    private double[][] createKernel() {
        double[][] kernel = new double[2*radius+1][2*radius+1];
        double sum = 0;
        for (int y = -radius; y <= radius; y++) {
            for (int x = -radius; x <= radius; x++) {
                double val = 1/(2*Math.PI*sigma*sigma) * Math.exp(-(x*x+y*y)/(2*sigma*sigma));
                kernel[y+radius][x+radius] = val;
                sum += val;
            }
        }
        for (int y = -radius; y <= radius; y++) {
            for (int x = -radius; x <= radius; x++) {
                kernel[y+radius][x+radius] /= sum;
            }
        }
        return kernel;
    }

    @Override
    public BufferedImage getAlteredImage(BufferedImage img) {
        if (img == null) {
            return null;
        }

        double[][] kernel = createKernel();

        return applyKernel(img, kernel, (kernel1, x, y) -> {
            int radius = kernel1.length/2;
            double newR = 0;
            double newG = 0;
            double newB = 0;
            for (int i = -radius; i <= radius; i++) {
                for (int j = -radius; j <= radius; j++) {
                    int X = roundCoord(x+j, 0, img.getWidth()-1);
                    int Y = roundCoord(y+i, 0, img.getHeight()-1);
                    newR += ARGBUtil.getR(img.getRGB(X, Y)) * kernel1[i + radius][j + radius];
                    newG += ARGBUtil.getG(img.getRGB(X, Y)) * kernel1[i + radius][j + radius];
                    newB += ARGBUtil.getB(img.getRGB(X, Y)) * kernel1[i + radius][j + radius];
                }
            }
            return ARGBUtil.getARGB((int) newR, (int) newG, (int) newB);
        });
    }
}
