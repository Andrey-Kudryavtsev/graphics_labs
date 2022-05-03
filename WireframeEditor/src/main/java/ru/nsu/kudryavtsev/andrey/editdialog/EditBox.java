package ru.nsu.kudryavtsev.andrey.editdialog;

import ru.nsu.kudryavtsev.andrey.wireframe.Wireframe;

import javax.swing.*;
import java.awt.*;

public class EditBox extends JPanel implements ActivePointListener {
    public static final int MIN_CONTROL_POINTS = BSpline.SECTION_CONTROL_POINTS;
    public static final int MAX_CONTROL_POINTS = 100;
    public static final int STEP_CONTROL_POINTS = 1;

    public static final int MIN_SECTION_SEGMENTS = 1;
    public static final int MAX_SECTION_SEGMENTS = 100;
    public static final int STEP_SECTION_SEGMENTS = 1;

    public static final int MIN_GENERATRICES = 1;
    public static final int MAX_GENERATRICES = 100;
    public static final int STEP_GENERATRICES = 1;

    public static final int MIN_CIRCLE_SEGMENTS = 1;
    public static final int MAX_CIRCLE_SEGMENTS = 100;
    public static final int STEP_CIRCLE_SEGMENTS = 1;

    private final JSpinner M1Spinner;
    private final JSpinner M2Spinner;
    private final JSpinner activePointXSpinner;
    private final JSpinner activePointYSpinner;

    public EditBox(BSplineViewer bSplineViewer) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        addSpinner("<html><b>Amount of control points (K)</b></html>",
                BSpline.DEFAULT_K, MIN_CONTROL_POINTS, MAX_CONTROL_POINTS, STEP_CONTROL_POINTS,
                bSplineViewer::setK);

        addSpinner("<html><b>Amount of B-spline section segments (N)</b></html>",
                BSpline.DEFAULT_N, MIN_SECTION_SEGMENTS, MAX_SECTION_SEGMENTS, STEP_SECTION_SEGMENTS,
                bSplineViewer::setN);

        M1Spinner = addSpinner("<html><b>Amount of generatrices (M1)</b></html>",
                Wireframe.DEFAULT_M1, MIN_GENERATRICES, MAX_GENERATRICES, STEP_GENERATRICES,
                null);

        M2Spinner = addSpinner("<html><b>Amount of circle segments (M2)</html></b>",
                Wireframe.DEFAULT_M2, MIN_CIRCLE_SEGMENTS, MAX_CIRCLE_SEGMENTS, STEP_CIRCLE_SEGMENTS,
                null);

        activePointXSpinner = addSpinner("<html><b>Active point X</b></html>",
                bSplineViewer.getActivePoint().x, null, null, 1,
                bSplineViewer::setActiveControlPointX);
        activePointXSpinner.setPreferredSize(new Dimension(80, 20));

        activePointYSpinner = addSpinner("<html><b>Active point Y</b></html>",
                bSplineViewer.getActivePoint().y, null, null, 1,
                bSplineViewer::setActiveControlPointY);
        activePointYSpinner.setPreferredSize(new Dimension(80, 20));
    }

    private JSpinner addSpinner(String text, Integer curVal, Integer min, Integer max, Integer step, SpinnerListener listener) {
        JPanel subPanel = new JPanel();
        subPanel.add(new JLabel(text));
        SpinnerModel model = new SpinnerNumberModel(curVal, min, max, step);
        JSpinner spinner = new JSpinner(model);
        if (listener != null) {
            spinner.addChangeListener(evt -> listener.set((int) spinner.getValue()));
        }
        subPanel.add(spinner);
        add(subPanel);

        return spinner;
    }

    public int getM1() {
        return (int) M1Spinner.getValue();
    }

    public void setM1(int M1) {
        M1Spinner.setValue(M1);
    }

    public int getM2() {
        return (int) M2Spinner.getValue();
    }

    public void setM2(int M2) {
        M2Spinner.setValue(M2);
    }

    @Override
    public void activePointChanged(Point point) {
        activePointXSpinner.setValue(point.x);
        activePointYSpinner.setValue(point.y);
    }
}
