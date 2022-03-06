package ru.nsu.kudryavtsev.andrey.tooldialog;

import lombok.Getter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;

public class ShapeToolDialog extends JPanel implements ActionListener {
    @Getter
    private int N = INIT_N;
    public static final int MIN_N = 3;
    public static final int MAX_N = 16;
    public static final int INIT_N = 5;
    public static final int SMALL_STEP_N = 1;
    @Getter
    private int R = INIT_R;
    public static final int MIN_R = 10;
    public static final int MAX_R = 100;
    public static final int INIT_R = 50;
    public static final int LARGE_STEP_R = 10;
    public static final int SMALL_STEP_R = 1;
    @Getter
    private int phi = INIT_PHI;
    public static final int MIN_PHI = -360;
    public static final int MAX_PHI = 360;
    public static final int INIT_PHI = 0;
    public static final int LARGE_STEP_PHI = 90;
    public static final int SMALL_STEP_PHI = 1;
    @Getter
    private boolean isStar = false;

    private final JSlider NSlider;
    private final JSpinner NSpinner;
    private final JSlider RSlider;
    private final JSpinner RSpinner;
    private final JSlider phiSlider;
    private final JSpinner phiSpinner;
    private final ButtonGroup buttonGroup;

    public ShapeToolDialog() {
        try {
            setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
            setAlignmentX(LEFT_ALIGNMENT);

            add(new JLabel("Vertices"));
            JPanel NSubPanel = new JPanel();
            NSlider = createSlider(MIN_N, MAX_N, INIT_N, SMALL_STEP_N, SMALL_STEP_N, "NStateChanged");
            NSpinner = createSpinner(MIN_N, MAX_N, INIT_N, SMALL_STEP_N, "NStateChanged");
            NSlider.setPreferredSize(new Dimension(320, 70));
            NSubPanel.add(NSlider);
            NSubPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            NSubPanel.add(NSpinner);
            add(NSubPanel);

            add(new JLabel("Radius"));
            JPanel RSubPanel = new JPanel();
            RSlider = createSlider(MIN_R, MAX_R, INIT_R, LARGE_STEP_R, SMALL_STEP_R, "RStateChanged");
            RSpinner = createSpinner(MIN_R, MAX_R, INIT_R, SMALL_STEP_R, "RStateChanged");
            RSlider.setPreferredSize(new Dimension(320, 70));
            RSubPanel.add(RSlider);
            RSubPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            RSubPanel.add(RSpinner);
            add(RSubPanel);

            add(new JLabel("Rotation"));
            JPanel phiSubPanel = new JPanel();
            phiSlider = createSlider(MIN_PHI, MAX_PHI, INIT_PHI, LARGE_STEP_PHI, LARGE_STEP_PHI, "phiStateChanged");
            phiSpinner = createSpinner(MIN_PHI, MAX_PHI, INIT_PHI, SMALL_STEP_PHI, "phiStateChanged");
            phiSlider.setPreferredSize(new Dimension(320, 70));
            phiSubPanel.add(phiSlider);
            phiSubPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            phiSubPanel.add(phiSpinner);
            add(phiSubPanel);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        JRadioButton PRadioButton = new JRadioButton("Regular polygon");
        PRadioButton.setSelected(true);
        PRadioButton.setActionCommand("polygon");
        JRadioButton SRadioButton = new JRadioButton("Regular star");
        SRadioButton.setActionCommand("star");
        buttonGroup = new ButtonGroup();
        buttonGroup.add(PRadioButton);
        buttonGroup.add(SRadioButton);
        JPanel PSSubPanel = new JPanel();
        PSSubPanel.add(PRadioButton);
        PSSubPanel.add(SRadioButton);
        PRadioButton.addActionListener(this);
        SRadioButton.addActionListener(this);
        add(PSSubPanel);
    }

    private JSlider createSlider(int minValue, int maxValue, int initValue, int largeStep, int smallStep, String stateChangedMethod)
            throws NoSuchMethodException {
        JSlider slider = new JSlider(SwingConstants.HORIZONTAL, minValue, maxValue, initValue);
        slider.setMajorTickSpacing(largeStep);
        slider.setMinorTickSpacing(smallStep);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        final Method method = getClass().getMethod(stateChangedMethod, ChangeEvent.class);
        slider.addChangeListener(evt -> {
            try {
                method.invoke(ShapeToolDialog.this, evt);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return slider;
    }

    private JSpinner createSpinner(int minValue, int maxValue, int initValue, int smallStep, String stateChangedMethod)
            throws NoSuchMethodException {
        SpinnerModel model = new SpinnerNumberModel(initValue, minValue, maxValue, smallStep);
        JSpinner spinner = new JSpinner(model);

        final Method method = getClass().getMethod(stateChangedMethod, ChangeEvent.class);
        spinner.addChangeListener(evt -> {
            try {
                method.invoke(ShapeToolDialog.this, evt);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return spinner;
    }

    public void NStateChanged(ChangeEvent e) {
        if (e.getSource() == NSlider) {
            N = NSlider.getValue();
            NSpinner.setValue(N);
        } else if (e.getSource() == NSpinner) {
            N = (int) NSpinner.getValue();
            NSlider.setValue(N);
        }
    }

    public void RStateChanged(ChangeEvent e) {
        if (e.getSource() == RSlider) {
            R = RSlider.getValue();
            RSpinner.setValue(R);
        } else if (e.getSource() == RSpinner) {
            R = (int) RSpinner.getValue();
            RSlider.setValue(R);
        }
    }

    public void phiStateChanged(ChangeEvent e) {
        if (e.getSource() == phiSlider) {
            phi = phiSlider.getValue();
            phiSpinner.setValue(phi);
        } else if (e.getSource() == phiSpinner) {
            phi = (int) phiSpinner.getValue();
            phiSlider.setValue(phi);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = buttonGroup.getSelection().getActionCommand();
        if ("polygon".equals(actionCommand)) {
            isStar = false;
        } else if ("star".equals(actionCommand)) {
            isStar = true;
        }
    }
}
