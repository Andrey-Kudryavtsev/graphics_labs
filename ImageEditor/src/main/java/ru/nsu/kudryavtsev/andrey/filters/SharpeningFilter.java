package ru.nsu.kudryavtsev.andrey.filters;

import java.awt.image.BufferedImage;

public class SharpeningFilter implements Filter {
    private final double[][] kernel = {{ 0, -1,  0},
                                       {-1,  5, -1},
                                       { 0, -1,  0}};

    @Override
    public BufferedImage getAlteredImage(BufferedImage img) {
        if (img == null) {
            return null;
        }

        return applyKernel(img, kernel, (kernel1, x, y) -> {
            int radius = kernel1.length/2;
            int newR = 0;
            int newG = 0;
            int newB = 0;
            for (int i = -radius; i <= radius; i++) {
                for (int j = -radius; j <= radius; j++) {
                    int X = roundCoord(x+j, 0, img.getWidth()-1);
                    int Y = roundCoord(y+i, 0, img.getHeight()-1);
                    newR += ARGBUtil.getR(img.getRGB(X, Y)) * kernel1[i + radius][j + radius];
                    newG += ARGBUtil.getG(img.getRGB(X, Y)) * kernel1[i + radius][j + radius];
                    newB += ARGBUtil.getB(img.getRGB(X, Y)) * kernel1[i + radius][j + radius];
                }
            }
            return ARGBUtil.getARGB(roundColor(newR), roundColor(newG), roundColor(newB));
        });
    }
}
