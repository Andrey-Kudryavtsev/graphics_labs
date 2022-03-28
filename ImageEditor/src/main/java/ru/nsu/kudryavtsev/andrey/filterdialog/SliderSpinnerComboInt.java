package ru.nsu.kudryavtsev.andrey.filterdialog;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.util.Dictionary;
import java.util.Hashtable;

public class SliderSpinnerComboInt extends JPanel {
    private int value;
    @Getter
    private final JSlider slider;
    @Getter
    private final JSpinner spinner;
    private final SliderSpinnerComboListener listener;

    public SliderSpinnerComboInt(String title,
                                 int minValue, int maxValue,
                                 int initValue,
                                 int smallStep, int largeStep,
                                 Dimension sliderPreferredSize,
                                 Dimension spinnerPreferredSize,
                                 SliderSpinnerComboListener listener,
                                 Hashtable<Integer, JLabel> labels) {
        this.listener = listener;
        slider = new JSlider(SwingConstants.HORIZONTAL, minValue, maxValue, initValue);
        if (labels != null) {
            slider.setLabelTable(labels);
        }
        slider.setMajorTickSpacing(largeStep);
        slider.setMinorTickSpacing(smallStep);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        if (sliderPreferredSize != null) {
            slider.setPreferredSize(sliderPreferredSize);
        }

        SpinnerModel spinnerModel = new SpinnerNumberModel(initValue, minValue, maxValue, smallStep);
        spinner = new JSpinner(spinnerModel);
        if (spinnerPreferredSize != null) {
            spinner.setPreferredSize(spinnerPreferredSize);
        }

        slider.addChangeListener(evt -> {
            value = slider.getValue();
            spinner.setValue(value);
            listener.valueChanged(value);
        });
        spinner.addChangeListener(evt -> {
            value = (int) spinner.getValue();
            slider.setValue(value);
            listener.valueChanged(value);
        });

        JPanel subPanel = new JPanel();
        subPanel.add(slider);
        subPanel.add(spinner);
        subPanel.setAlignmentX(LEFT_ALIGNMENT);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        add(new JLabel(title));
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(subPanel);
    }

    public SliderSpinnerComboInt(String title,
                                 int minValue, int maxValue,
                                 int initValue,
                                 int smallStep, int largeStep,
                                 Dimension sliderPreferredSize,
                                 Dimension spinnerPreferredSize,
                                 SliderSpinnerComboListener listener) {
        this(title, minValue, maxValue, initValue, smallStep, largeStep, sliderPreferredSize, spinnerPreferredSize, listener, null);
    }

    public void setValue(int value) {
        this.value = value;
        slider.setValue(value);
        spinner.setValue(value);
        listener.valueChanged(value);
    }
}
