package ru.nsu.kudryavtsev.andrey.editdialog;

import lombok.Getter;
import ru.nsu.kudryavtsev.andrey.wireframe.Wireframe;

import javax.swing.*;
import java.awt.*;

public class WireframeEditDialog extends JPanel {
    private final BSplineViewer bSplineViewer;
    private final EditBox editBox;
    @Getter
    private Wireframe wireframe;

    public WireframeEditDialog(int width, int height) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setPreferredSize(new Dimension(width, height));

        JScrollPane sp = new JScrollPane();
        bSplineViewer = new BSplineViewer(sp, width, height);
        add(sp);

        editBox = new EditBox(bSplineViewer);
        add(editBox);

        bSplineViewer.setActivePointListener(editBox);

        wireframe = Wireframe.createDefaultWireframe();
    }

    public void applyChanges() {
        wireframe = new Wireframe(bSplineViewer.getBSpline(), editBox.getM1(), editBox.getM2());
    }

    public void resetChanges() {
        bSplineViewer.setBSpline(wireframe.getGeneratrix());
        editBox.setM1(wireframe.getM1());
        editBox.setM2(wireframe.getM2());
    }
}
