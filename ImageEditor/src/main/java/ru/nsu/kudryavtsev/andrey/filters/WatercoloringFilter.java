package ru.nsu.kudryavtsev.andrey.filters;

import java.awt.image.BufferedImage;
import java.util.Arrays;

public class WatercoloringFilter implements Filter {
    private final SharpeningFilter sharpeningFilter = new SharpeningFilter();
    private final double[][] kernel = {{1, 1, 1, 1, 1},
                                       {1, 1, 1, 1, 1},
                                       {1, 1, 1, 1, 1},
                                       {1, 1, 1, 1, 1},
                                       {1, 1, 1, 1, 1}};

    @Override
    public BufferedImage getAlteredImage(BufferedImage img) {
        if (img == null) {
            return null;
        }

        BufferedImage copyImg = medianBlur(img);
        copyImg = medianBlur(copyImg);
        copyImg = sharpeningFilter.getAlteredImage(copyImg);
        return copyImg;
    }

    public BufferedImage medianBlur(BufferedImage img) {
        return applyKernel(img, kernel, (kernel1, x, y) -> {
            int radius = kernel1.length/2;
            int[] R = new int[kernel1.length * kernel1.length];
            int[] G = new int[kernel1.length * kernel1.length];
            int[] B = new int[kernel1.length * kernel1.length];
            for (int i = -radius; i <= radius; i++) {
                for (int j = -radius; j <= radius; j++) {
                    int X = roundCoord(x+j, 0, img.getWidth()-1);
                    int Y = roundCoord(y+i, 0, img.getHeight()-1);
                    int idx = (i+radius)*kernel1.length + (j+radius);
                    R[idx] = (int) (ARGBUtil.getR(img.getRGB(X, Y)) * kernel1[i+radius][j+radius]);
                    G[idx] = (int) (ARGBUtil.getG(img.getRGB(X, Y)) * kernel1[i+radius][j+radius]);
                    B[idx] = (int) (ARGBUtil.getB(img.getRGB(X, Y)) * kernel1[i+radius][j+radius]);
                }
            }
            Arrays.sort(R);
            Arrays.sort(G);
            Arrays.sort(B);
            int idx = kernel.length * kernel.length/2;
            return ARGBUtil.getARGB(R[idx], G[idx], B[idx]);
        });
    }
}
