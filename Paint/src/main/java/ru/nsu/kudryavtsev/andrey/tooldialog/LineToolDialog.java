package ru.nsu.kudryavtsev.andrey.tooldialog;

import lombok.Getter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;

public class LineToolDialog extends JPanel {
    public static final int MIN_THICKNESS = 1;
    public static final int MAX_THICKNESS = 25;
    public static final int LARGE_STEP = 4;
    public static final int SMALL_STEP = 1;
    @Getter
    private int thickness = MIN_THICKNESS;
    private final JSlider slider;
    private final JSpinner spinner;

    public LineToolDialog() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        add(new JLabel("Thickness"));

        JPanel subPanel = new JPanel();
        slider = new JSlider(SwingConstants.HORIZONTAL, MIN_THICKNESS, MAX_THICKNESS, MIN_THICKNESS);
        slider.setMajorTickSpacing(LARGE_STEP);
        slider.setMinorTickSpacing(SMALL_STEP);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setSnapToTicks(true);

        SpinnerModel model = new SpinnerNumberModel(MIN_THICKNESS, MIN_THICKNESS, MAX_THICKNESS, SMALL_STEP);
        spinner = new JSpinner(model);

        subPanel.add(slider);
        subPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        subPanel.add(spinner);

        slider.addChangeListener(this::thicknessStateChanged);
        spinner.addChangeListener(this::thicknessStateChanged);
        add(subPanel);
    }

    public void thicknessStateChanged(ChangeEvent e) {
        if (e.getSource() == slider) {
            thickness = slider.getValue();
            spinner.setValue(thickness);
        } else if (e.getSource() == spinner) {
            thickness = (int) spinner.getValue();
            slider.setValue(thickness);
        }
    }
}
