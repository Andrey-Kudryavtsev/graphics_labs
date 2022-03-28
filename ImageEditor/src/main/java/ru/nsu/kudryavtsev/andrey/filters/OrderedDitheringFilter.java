package ru.nsu.kudryavtsev.andrey.filters;

import lombok.Getter;
import ru.nsu.kudryavtsev.andrey.filterdialog.OrderedDitheringFilterDialog;

import java.awt.image.BufferedImage;

public class OrderedDitheringFilter implements Filter {
    private final int[] orders = {2, 4, 8, 16};
    private int colorsR;
    private int colorsG;
    private int colorsB;
    @Getter
    private final OrderedDitheringFilterDialog dialog;

    public OrderedDitheringFilter(int colorsR, int colorsG, int colorsB) {
        this.colorsR = colorsR;
        this.colorsG = colorsG;
        this.colorsB = colorsB;
        dialog = new OrderedDitheringFilterDialog(colorsR, colorsG, colorsB);
    }

    private int getOrder(int maxColors) {
        double order = Math.sqrt(256.0/maxColors);
        for (int i = 0; i < orders.length; i++) {
            if (order <= orders[i]) {
                return orders[i];
            }
        }
        return orders[0];
    }

    private int[][] createIntKernel(int order) {
        if (order == 2) {
            return new int[][] {{0, 2},
                                {3, 1}};
        } else {
            int[][] prevKernel = createIntKernel(order/2);

            int[][] kernel = new int[order][order];
            for (int y = 0; y < order/2; y++) { // first quadrant
                for (int x = 0; x < order/2; x++) {
                    kernel[y][x] = 4 * prevKernel[y % (order/2)][x % (order/2)];
                }
            }
            for (int y = 0; y < order/2; y++) { // second quadrant
                for (int x = order/2; x < order; x++) {
                    kernel[y][x] = 4 * prevKernel[y % (order/2)][x % (order/2)] + 2;
                }
            }
            for (int y = order/2; y < order; y++) { // third quadrant
                for (int x = 0; x < order/2; x++) {
                    kernel[y][x] = 4 * prevKernel[y % (order/2)][x % (order/2)] + 3;
                }
            }
            for (int y = order/2; y < order; y++) { // fourth quadrant
                for (int x = order/2; x < order; x++) {
                    kernel[y][x] = 4 * prevKernel[y % (order/2)][x % (order/2)] + 1;
                }
            }
            return kernel;
        }
    }

    private double[][] createDoubleKernel(int n) {
        int[][] intKernel = createIntKernel(n);
        int order = intKernel.length;
        int elems = order*order;
        double[][] doubleKernel = new double[order][order];
        for (int y = 0; y < doubleKernel.length; y++) {
            for (int x = 0; x < doubleKernel.length; x++) {
                doubleKernel[y][x] = (intKernel[y][x] + 1.0) / elems - 0.5;
            }
        }
        return doubleKernel;
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

    @Override
    public BufferedImage getAlteredImage(BufferedImage img) {
        if (img == null) {
            return null;
        }

        int order = getOrder(Math.min(Math.min(colorsR, colorsG), colorsB));
        System.out.println(order);

        double[][] kernel = createDoubleKernel(order);
        int kernelSize = kernel.length;

        int[] paletteR = createNewPalette(colorsR);
        double rR = 255.0/colorsR;

        int[] paletteG = createNewPalette(colorsG);
        double rG = 255.0/colorsR;

        int[] paletteB = createNewPalette(colorsB);
        double rB = 255.0/colorsR;

        BufferedImage copyImg = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int rgb = img.getRGB(x, y);
                int newR = paletteR[roundColor((int) (ARGBUtil.getR(rgb) + rR*(kernel[y%kernelSize][x%kernelSize])))];
                int newG = paletteG[roundColor((int) (ARGBUtil.getG(rgb) + rG*(kernel[y%kernelSize][x%kernelSize])))];
                int newB = paletteB[roundColor((int) (ARGBUtil.getB(rgb) + rB*(kernel[y%kernelSize][x%kernelSize])))];
                int newRgb = ARGBUtil.getARGB(newR, newG, newB);
                copyImg.setRGB(x, y, newRgb);
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
