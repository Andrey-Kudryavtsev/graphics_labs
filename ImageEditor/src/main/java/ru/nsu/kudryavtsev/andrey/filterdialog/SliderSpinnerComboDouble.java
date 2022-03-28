package ru.nsu.kudryavtsev.andrey.filterdialog;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.Hashtable;

public class SliderSpinnerComboDouble extends JPanel {
    private double value;
    @Getter
    private final JSlider slider;
    @Getter
    private final JSpinner spinner;
    private final SliderSpinnerComboListener listener;

    public SliderSpinnerComboDouble(String title,
                                 int minValue, int maxValue,
                                 int initValue,
                                 int smallStep, int largeStep,
                                 Dimension sliderPreferredSize,
                                 Dimension spinnerPreferredSize,
                                 SliderSpinnerComboListener listener) {
        this.listener = listener;

        slider = new JSlider(SwingConstants.HORIZONTAL, minValue, maxValue, initValue);

        Format f = new DecimalFormat("0.0");
        Hashtable<Integer, JLabel> labels = new Hashtable<>();
        for (int i = minValue; i <= maxValue; i+=largeStep) {
            labels.put(i, new JLabel(f.format(i*0.1)));
        }
        slider.setLabelTable(labels);

        slider.setMajorTickSpacing(largeStep);
        slider.setMinorTickSpacing(smallStep);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        if (sliderPreferredSize != null) {
            slider.setPreferredSize(sliderPreferredSize);
        }

        SpinnerModel spinnerModel = new SpinnerNumberModel(initValue*0.1, minValue*0.1, maxValue*0.1, smallStep*0.1);
        spinner = new JSpinner(spinnerModel);
        if (spinnerPreferredSize != null) {
            spinner.setPreferredSize(spinnerPreferredSize);
        }

        slider.addChangeListener(evt -> {
            if (value != slider.getValue() * 0.1) {
                value = slider.getValue() * 0.1;
                spinner.setValue(value);
                listener.valueChanged(value);
            }
        });
        spinner.addChangeListener(evt -> {
            if (value != (double) spinner.getValue()) {
                value = (double) spinner.getValue();
                slider.setValue((int) (value * 10));
                listener.valueChanged(value);
            }
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

    public void setValue(double value) {
        this.value = value;
        slider.setValue((int)(value*10));
        spinner.setValue(value);
        listener.valueChanged(value);
    }
}
