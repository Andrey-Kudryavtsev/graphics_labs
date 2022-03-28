package ru.nsu.kudryavtsev.andrey.filters;

import java.awt.image.BufferedImage;

public class ErosionFilter implements Filter {
    private final double[][] kernel = {{0, 1, 0},
                                       {1, 1, 1},
                                       {0, 1, 0}};

    @Override
    public BufferedImage getAlteredImage(BufferedImage img) {
        if (img == null) {
            return null;
        }

        return applyKernel(img, kernel, (kernel1, x, y) -> {
            int radius = kernel.length/2;
            int newR = 255;
            int newG = 255;
            int newB = 255;
            for (int i = -radius; i <= radius; i++) {
                for (int j = -radius; j <= radius; j++) {
                    int X = roundCoord(x+j, 0, img.getWidth()-1);
                    int Y = roundCoord(y+i, 0, img.getHeight()-1);
                    int val = (int) (ARGBUtil.getR(img.getRGB(X, Y)) * kernel1[i+radius][j+radius]);
                    newR = kernel1[i+radius][j+radius] == 0 ? newR : Math.min(val, newR);
                    val = (int) (ARGBUtil.getG(img.getRGB(X, Y)) * kernel1[i+radius][j+radius]);
                    newG = kernel1[i+radius][j+radius] == 0 ? newG : Math.min(val, newG);
                    val = (int) (ARGBUtil.getB(img.getRGB(X, Y)) * kernel1[i+radius][j+radius]);
                    newB = kernel1[i+radius][j+radius] == 0 ? newB : Math.min(val, newB);
                }
            }
            return ARGBUtil.getARGB(roundColor(newR), roundColor(newG), roundColor(newB));
        });
    }
}
