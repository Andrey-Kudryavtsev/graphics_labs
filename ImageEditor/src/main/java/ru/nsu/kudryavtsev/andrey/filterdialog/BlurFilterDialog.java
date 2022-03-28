package ru.nsu.kudryavtsev.andrey.filterdialog;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.Hashtable;

public class BlurFilterDialog extends JPanel {
    @Getter
    private int radius;
    @Getter
    private double sigma;
    private final SliderSpinnerComboInt radiusSSCombo;
    private final SliderSpinnerComboDouble sigmaSSCombo;

    public BlurFilterDialog(int radius, double sigma) {
        this.radius = radius;
        this.sigma = sigma;
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        Hashtable<Integer, JLabel> labels = new Hashtable<>();
        for (int i = 1; i <= 5; i++) {
            labels.put(i, new JLabel((i*2+1) + "x" + (i*2+1)));
        }
        radiusSSCombo = new SliderSpinnerComboInt(
                "<html><b>Matrix size</b></html>",
                1, 5,
                radius,
                1, 1,
                new Dimension(320, 50),
                new Dimension(40, 25),
                value -> this.radius = (int) value,
                labels
        );

        sigmaSSCombo = new SliderSpinnerComboDouble(
                "<html><b>Sigma</b></html>",
                1, 100,
                50,
                1, 11,
                new Dimension(320, 50),
                new Dimension(40, 25),
                value -> this.sigma = (double) value
        );

        add(radiusSSCombo);
        add(sigmaSSCombo);
    }

    public void setRadius(int radius) {
        this.radius = radius;
        radiusSSCombo.setValue(radius);
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
        sigmaSSCombo.setValue(sigma);
    }
}
