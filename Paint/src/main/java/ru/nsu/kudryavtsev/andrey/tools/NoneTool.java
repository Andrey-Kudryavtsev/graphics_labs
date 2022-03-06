package ru.nsu.kudryavtsev.andrey.tools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class NoneTool implements Tool {
    @Override
    public JPanel getDialog() {
        return null;
    }

    @Override
    public void applyChanges() {

    }

    @Override
    public void mousePressed(MouseEvent e, Color color, BufferedImage img) {

    }
}
