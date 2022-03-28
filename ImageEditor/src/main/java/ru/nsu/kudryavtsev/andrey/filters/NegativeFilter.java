package ru.nsu.kudryavtsev.andrey.filters;

import java.awt.image.BufferedImage;

public class NegativeFilter implements Filter {

    @Override
    public BufferedImage getAlteredImage(BufferedImage img) {
        if (img == null) {
            return null;
        }

        return applyPixelOperation(img, rgb -> {
            int newR = 255 - ARGBUtil.getR(rgb);
            int newG = 255 - ARGBUtil.getG(rgb);
            int newB = 255 - ARGBUtil.getB(rgb);
            return ARGBUtil.getARGB(newR, newG, newB);
        });
    }
}
