package ru.nsu.kudryavtsev.andrey.filters;

import java.awt.image.BufferedImage;

public interface Filter {
    BufferedImage getAlteredImage(BufferedImage img);

    default void applyChanges() {}

    default void resetChanges() {}

    default int roundCoord(int coord, int minCoord, int maxCoord) {
        return Math.min(Math.max(coord, minCoord), maxCoord);
    }

    default int roundColor(int color) {
        return Math.min(Math.max(color, 0), 255);
    }

    default BufferedImage applyPixelOperation(BufferedImage img, PixelOperation operation) {
        BufferedImage copyImg = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int rgb = img.getRGB(x, y);
                int newRgb = operation.makeOperation(rgb);
                copyImg.setRGB(x, y, newRgb);
            }
        }
        return copyImg;
    }

    default BufferedImage applyKernel(BufferedImage img, double[][] kernel, KernelOperation operation) {
        BufferedImage copyImg = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int newRgb = operation.makeOperation(kernel, x, y);
                copyImg.setRGB(x, y, newRgb);
            }
        }
        return copyImg;
    }
}
