package ru.nsu.kudryavtsev.andrey.filters;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.kudryavtsev.andrey.filterdialog.ResizeFilterDialog;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class ResizeTool {
    private int type;
    @Setter
    private int width;
    @Setter
    private int height;
    @Getter
    private final ResizeFilterDialog dialog;

    public ResizeTool(int type) {
        this.type = type;
        dialog = new ResizeFilterDialog(type);
    }

    public void applyChanges() {
        type = dialog.getType();
    }

    public void resetChanges() {
        dialog.setType(type);
    }

    public BufferedImage getAlteredImage(BufferedImage img, int width, int height) {
        if (img == null) {
            return null;
        }

        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();

        float k = Math.min(width / (float) imgWidth, height / (float) imgHeight);
        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(k, k), type);
        return ato.filter(img, null);
    }
}
