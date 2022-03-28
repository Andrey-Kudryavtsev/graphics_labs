package ru.nsu.kudryavtsev.andrey.filterdialog;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.AffineTransformOp;

public class ResizeFilterDialog extends JPanel {
    @Getter
    private int type;
    private final JRadioButton bicubic;
    private final JRadioButton bilinear;
    private final JRadioButton nearest;

    public ResizeFilterDialog(int type) {
        this.type = type;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        add(new JLabel("<html><b>Image interpolation type</b></html>"));
        add(Box.createRigidArea(new Dimension(0, 10)));
        bicubic = new JRadioButton("Bicubic (high quality, low performance)");
        bilinear = new JRadioButton("Bilinear (avg quality, avg performance)");
        nearest = new JRadioButton("Nearest neighbour (low quality, high performance)");
        switch (type) {
            case AffineTransformOp.TYPE_BICUBIC -> bicubic.setSelected(true);
            case AffineTransformOp.TYPE_BILINEAR -> bilinear.setSelected(true);
            case AffineTransformOp.TYPE_NEAREST_NEIGHBOR -> nearest.setSelected(true);
        }
        bicubic.addActionListener(evt -> this.type = AffineTransformOp.TYPE_BICUBIC);
        bilinear.addActionListener(evt -> this.type = AffineTransformOp.TYPE_BILINEAR);
        nearest.addActionListener(evt -> this.type = AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        ButtonGroup bg = new ButtonGroup();
        bg.add(bicubic);
        bg.add(bilinear);
        bg.add(nearest);
        add(bicubic);
        add(bilinear);
        add(nearest);
    }

    public void setType(int type) {
        this.type = type;
        switch (type) {
            case AffineTransformOp.TYPE_BICUBIC -> bicubic.setSelected(true);
            case AffineTransformOp.TYPE_BILINEAR -> bilinear.setSelected(true);
            case AffineTransformOp.TYPE_NEAREST_NEIGHBOR -> nearest.setSelected(true);
        }
    }
}
