package ru.nsu.kudryavtsev.andrey.filterdialog;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;

public class RotateFilterDialog extends JPanel {
    @Getter
    private int theta;
    private SliderSpinnerComboInt thetaSSCombo;

    public RotateFilterDialog(int theta) {
        this.theta = theta;
        thetaSSCombo = new SliderSpinnerComboInt(
                "<html><b>Theta</b></html>",
                -180, 180,
                theta,
                60, 60,
                new Dimension(320, 50),
                new Dimension(40, 25),
                value -> this.theta = (int) value
        );
        add(thetaSSCombo);
    }

    public void setTheta(int theta) {
        this.theta = theta;
        thetaSSCombo.setValue(theta);
    }
}
