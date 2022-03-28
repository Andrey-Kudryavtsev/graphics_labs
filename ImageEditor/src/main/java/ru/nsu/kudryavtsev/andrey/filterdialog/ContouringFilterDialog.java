package ru.nsu.kudryavtsev.andrey.filterdialog;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;

public class ContouringFilterDialog extends JPanel {
    @Getter
    private boolean isSobel;
    @Getter
    private int threshold;
    private final SliderSpinnerComboInt thresholdSSCombo;
    private final JRadioButton roberts;
    private final JRadioButton sobel;

    public ContouringFilterDialog(boolean isSobel, int threshold) {
        this.isSobel = isSobel;
        this.threshold = threshold;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        thresholdSSCombo = new SliderSpinnerComboInt(
                "<html><b>Threshold</b></html>",
                0, 256,
                128,
                32, 128,
                new Dimension(320, 50),
                new Dimension(40, 25),
                value -> this.threshold = (int) value
        );
        add(thresholdSSCombo);
        roberts = new JRadioButton("Roberts algorithm");
        sobel = new JRadioButton("Sobel algorithm");
        if (isSobel) {
            sobel.setSelected(true);
        } else {
            roberts.setSelected(true);
        }
        roberts.addActionListener(evt -> this.isSobel = false);
        sobel.addActionListener(evt -> this.isSobel = true);
        ButtonGroup bg = new ButtonGroup();
        bg.add(roberts);
        bg.add(sobel);
        add(roberts);
        add(sobel);
    }

    public void setSobel(boolean isSobel) {
        this.isSobel = isSobel;
        if (isSobel) {
            sobel.setSelected(true);
        } else {
            roberts.setSelected(true);
        }
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
        thresholdSSCombo.setValue(threshold);
    }
}
