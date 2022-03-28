package ru.nsu.kudryavtsev.andrey.filters;

import java.awt.image.BufferedImage;

public class BlackAndWhiteFilter implements Filter {

    @Override
    public BufferedImage getAlteredImage(BufferedImage img) {
        if (img == null) {
            return null;
        }

        return applyPixelOperation(img, rgb -> {
            int newComponent = (int) (0.299*ARGBUtil.getR(rgb) + 0.587*ARGBUtil.getG(rgb) + 0.114*ARGBUtil.getB(rgb));
            return ARGBUtil.getARGB(newComponent, newComponent, newComponent);
        });
    }
}
