package ru.nsu.kudryavtsev.andrey.filters;

import lombok.Getter;
import ru.nsu.kudryavtsev.andrey.filterdialog.FloydSteinbergDitheringFilterDialog;

import java.awt.image.BufferedImage;

public class FloydSteinbergDitheringFilter implements Filter {
    private int colorsR;
    private int colorsG;
    private int colorsB;
    @Getter
    private final FloydSteinbergDitheringFilterDialog dialog;

    public FloydSteinbergDitheringFilter(int colorsR, int colorsG, int colorsB) {
        this.colorsR = colorsR;
        this.colorsG = colorsG;
        this.colorsB = colorsB;
        dialog = new FloydSteinbergDitheringFilterDialog(colorsR, colorsG, colorsB);
    }

    private int[] createNewPalette(int colors) {
        int[] palette = new int[256];
        int step = (int) Math.round(256.0/colors);
        int colorStep = (int) Math.round(256.0/(colors-1));
        for (int i = 0; i < colors; i++) {
            int leftBorder = i*step;
            int rightBorder = i == colors-1 ? 256 : (i+1)*step;
            for (int j = leftBorder; j < rightBorder; j++) {
                palette[j] = i == colors-1 ? 255 : i*colorStep;
            }
        }

        return palette;
    }

    private void addError(BufferedImage img, int x, int y, int errR, int errG, int errB, double part) {
        if (x < 0 || x >= img.getWidth() || y < 0 || y >= img.getHeight()) {
            return;
        }
        int rgb = img.getRGB(x, y);
        int newRgb = ARGBUtil.getARGB(
                roundColor((int) (ARGBUtil.getR(rgb) + errR*part)),
                roundColor((int) (ARGBUtil.getG(rgb) + errG*part)),
                roundColor((int) (ARGBUtil.getB(rgb) + errB*part)));
        img.setRGB(x, y, newRgb);
    }

    private double[] distributeParts(BufferedImage img, int x, int y) {
        double[] parts = {3.0/16, 5.0/16, 1.0/16, 7.0/16};
        if (x == 0) {
            parts[0] = -1;
        } else if (x == img.getWidth()-1) {
            parts[2] = parts[3] = -1;
        }
        if (y == img.getHeight()-1) {
            parts[0] = parts[1] = parts[2] = -1;
        }
        double truePart = 0;
        int truePartNum = 0;
        for (int i = 0; i < parts.length; i++) {
            if (parts[i] != -1) {
                truePart += parts[i];
                truePartNum++;
            }
        }
        double distributedPart = 1 - truePart;
        for (int i = 0; i < parts.length; i++) {
            if (parts[i] != -1) {
                parts[i] += distributedPart/truePartNum;
            }
        }
        return parts;
    }

    @Override
    public BufferedImage getAlteredImage(BufferedImage img) {
        if (img == null) {
            return null;
        }

        int[] paletteR = createNewPalette(colorsR);
        int[] paletteG = createNewPalette(colorsG);
        int[] paletteB = createNewPalette(colorsB);

        BufferedImage copyImg = new BufferedImage(img.getColorModel(), img.copyData(null), img.getColorModel().isAlphaPremultiplied(), null);
        for (int y = 0; y < copyImg.getHeight(); y++) {
            for (int x = 0; x < copyImg.getWidth(); x++) {
                int rgb = copyImg.getRGB(x, y);

                int R = ARGBUtil.getR(rgb);
                int G = ARGBUtil.getG(rgb);
                int B = ARGBUtil.getB(rgb);

                int newR = paletteR[R];
                int newG = paletteG[G];
                int newB = paletteB[B];

                int errR = R - newR;
                int errG = G - newG;
                int errB = B - newB;

                int newRgb = ARGBUtil.getARGB(newR, newG, newB);
                copyImg.setRGB(x, y, newRgb);

                double[] parts = distributeParts(copyImg, x, y);
                addError(copyImg, x-1, y+1, errR, errG, errB, parts[0]);
                addError(copyImg, x, y+1, errR, errG, errB, parts[1]);
                addError(copyImg, x+1, y+1, errR, errG, errB, parts[2]);
                addError(copyImg, x+1, y, errR, errG, errB, parts[3]);
            }
        }
        return copyImg;
    }

    @Override
    public void applyChanges() {
        colorsR = dialog.getColorsR();
        colorsG = dialog.getColorsG();
        colorsB = dialog.getColorsB();
    }

    @Override
    public void resetChanges() {
        dialog.setColorsR(colorsR);
        dialog.setColorsG(colorsG);
        dialog.setColorsB(colorsB);
    }
}
