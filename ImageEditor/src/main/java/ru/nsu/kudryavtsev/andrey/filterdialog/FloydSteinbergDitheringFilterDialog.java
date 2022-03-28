package ru.nsu.kudryavtsev.andrey.filterdialog;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;

public class FloydSteinbergDitheringFilterDialog extends JPanel {
    @Getter
    private int colorsR;
    @Getter
    private int colorsG;
    @Getter
    private int colorsB;
    private final SliderSpinnerComboInt colorsRSSCombo;
    private final SliderSpinnerComboInt colorsGSSCombo;
    private final SliderSpinnerComboInt colorsBSSCombo;

    public FloydSteinbergDitheringFilterDialog (int colorsR, int colorsG, int colorsB) {
        this.colorsR = colorsR;
        this.colorsG = colorsG;
        this.colorsB = colorsB;

        colorsRSSCombo = new SliderSpinnerComboInt(
                "<html><b>Red color quantization</html></b>",
                2, 128,
                colorsR,
                1, 63,
                new Dimension(320, 50),
                new Dimension(40, 25),
                value -> this.colorsR = (int) value
        );
        colorsGSSCombo = new SliderSpinnerComboInt(
                "<html><b>Green color quantization</html></b>",
                2, 128,
                colorsG,
                1, 63,
                new Dimension(320, 50),
                new Dimension(40, 25),
                value -> this.colorsG = (int) value
        );
        colorsBSSCombo = new SliderSpinnerComboInt(
                "<html><b>Blue color quantization</html></b>",
                2, 128,
                colorsB,
                1, 63,
                new Dimension(320, 50),
                new Dimension(40, 25),
                value -> this.colorsB = (int) value
        );
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        add(colorsRSSCombo);
        add(colorsGSSCombo);
        add(colorsBSSCombo);
    }

    public void setColorsR(int colorsR) {
        this.colorsR = colorsR;
        colorsRSSCombo.setValue(colorsR);
    }

    public void setColorsG(int colorsG) {
        this.colorsG = colorsG;
        colorsGSSCombo.setValue(colorsG);
    }

    public void setColorsB(int colorsB) {
        this.colorsB = colorsB;
        colorsBSSCombo.setValue(colorsB);
    }
}
