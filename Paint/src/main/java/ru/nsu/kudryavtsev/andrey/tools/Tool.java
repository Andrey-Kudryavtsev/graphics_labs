package ru.nsu.kudryavtsev.andrey.tools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public interface Tool {
    JPanel getDialog();
    void applyChanges();
    void mousePressed(MouseEvent e, Color color, BufferedImage img);
}
