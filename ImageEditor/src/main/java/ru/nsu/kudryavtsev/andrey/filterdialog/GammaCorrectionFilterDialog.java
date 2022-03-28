package ru.nsu.kudryavtsev.andrey.filterdialog;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;

public class GammaCorrectionFilterDialog extends JPanel {
    @Getter
    private double gamma;
    private final SliderSpinnerComboDouble gammaSSCombo;

    public GammaCorrectionFilterDialog(double gamma) {
        this.gamma = gamma;
        gammaSSCombo = new SliderSpinnerComboDouble(
                "<html><b>Gamma</b></html>",
                1, 20,
                10,
                1, 1,
                new Dimension(420, 50),
                new Dimension(40, 25),
                value -> this.gamma = (double) value
        );
        add(gammaSSCombo);
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
        gammaSSCombo.setValue(gamma);
    }
}
