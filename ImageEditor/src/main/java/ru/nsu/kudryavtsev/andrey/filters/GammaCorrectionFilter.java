package ru.nsu.kudryavtsev.andrey.filters;

import lombok.Getter;
import ru.nsu.kudryavtsev.andrey.filterdialog.GammaCorrectionFilterDialog;

import java.awt.image.BufferedImage;

public class GammaCorrectionFilter implements Filter {
    private double gamma;
    @Getter
    private final GammaCorrectionFilterDialog dialog;

    public GammaCorrectionFilter(double gamma) {
        this.gamma = gamma;
        dialog = new GammaCorrectionFilterDialog(gamma);
    }

    @Override
    public void applyChanges() {
        gamma = dialog.getGamma();
    }

    @Override
    public void resetChanges() {
        dialog.setGamma(gamma);
    }

    @Override
    public BufferedImage getAlteredImage(BufferedImage img) {
        if (img == null) {
            return null;
        }

        return applyPixelOperation(img, rgb -> {
            int newR = (int) (Math.pow(ARGBUtil.getR(rgb)/255.0, gamma) * 255);
            int newG = (int) (Math.pow(ARGBUtil.getG(rgb)/255.0, gamma) * 255);
            int newB = (int) (Math.pow(ARGBUtil.getB(rgb)/255.0, gamma) * 255);
            return ARGBUtil.getARGB(roundColor(newR), roundColor(newG), roundColor(newB));
        });
    }
}
