package ru.nsu.kudryavtsev.andrey;

import ru.nsu.kudryavtsev.andrey.editdialog.WireframeEditDialog;
import ru.nsu.kudryavtsev.andrey.fileutils.FileUtils;
import ru.nsu.kudryavtsev.andrey.wireframe.Viewport;
import ru.nsu.kudryavtsev.andrey.wireframe.Wireframe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

public class AppWindow extends MainFrame {
    private final int width = 1600;
    private final int height = 900;
    private final WireframeEditDialog wireframeEditDialog;
    private final Viewport viewport;

    public AppWindow() throws NoSuchMethodException {
        super();
        setPreferredSize(new Dimension(1280, 720));
        setMinimumSize(new Dimension(640, 480));
        setResizable(true);

        addSubMenu("File", KeyEvent.VK_F);
        addMenuItem("File/Save", "Save curve of the displayed wireframe", KeyEvent.VK_S, "save.png", "onSave");
        addMenuItem("File/Open", "Load existing wireframe curve", KeyEvent.VK_O, "open.png", "onOpen");
        addMenuSeparator("File");
        addMenuItem("File/Exit", "Exit application", KeyEvent.VK_E, "exit.png", "onExit");

        addSubMenu("Edit", KeyEvent.VK_E);
        addMenuItem("Edit/Wireframe", "Edit wireframe parameters", KeyEvent.VK_C, "spline.png", "onCurve");
        addMenuItem("Edit/Reset rotation", "Reset wireframe rotation", KeyEvent.VK_R, "back.png", "onReset");

        addSubMenu("Help", KeyEvent.VK_H);
        addMenuItem("Help/About", "Shows program version and copyright information", KeyEvent.VK_A, "about.png", "onAbout");

        addToolBarButton("File/Exit");
        addToolBarSeparator();
        addToolBarButton("File/Save");
        addToolBarButton("File/Open");
        addToolBarSeparator();
        addToolBarButton("Edit/Reset rotation");
        addToolBarButton("Edit/Wireframe");
        addToolBarSeparator();
        addToolBarButton("Help/About");

        wireframeEditDialog = new WireframeEditDialog(width, height);
        viewport = new Viewport(1280, 720);
        add(viewport);
    }

    public void onSave(ActionEvent evt) {
        File file = FileUtils.getSaveFileName(this, "txt", "text file");
        if (file == null) {
            return;
        }
        try {
            FileUtils.saveWireframe(file, wireframeEditDialog.getWireframe());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Unable to save the file", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void onOpen(ActionEvent evt) {
        File file = FileUtils.getOpenFileName(this, "txt", "text file");
        if (file == null) {
            return;
        }
        if (!FileUtils.isCorrectExtForOpen(file)) {
            JOptionPane.showMessageDialog(this, "Wrong file extension", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Wireframe wireframe = FileUtils.loadWireframe(file);
            if (wireframe == null) {
                JOptionPane.showMessageDialog(this, "File has inappropriate parameters", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            wireframeEditDialog.setWireframe(wireframe);
            viewport.setWireframe(wireframe);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Unable to open the file", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void onExit(ActionEvent evt)
    {
        System.exit(0);
    }

    public void onAbout(ActionEvent evt)
    {
        JOptionPane.showMessageDialog(this, """
                WireframeEditor, version 1.0
                
                Set wireframe generatrix as arbitrary curve.
                Move curve control points with mouse press+drag
                as well as move across the field.
                
                Generated wireframe is shown in the main window.
                Rotate wireframe with mouse press+drag.
                Zoom wireframe with mouse wheel.
                
                Save/Load curve in specified format.
                
                Copyright \u00a9 2022 Kudryavtsev Andrey, FIT, group 19203""", "About Init", JOptionPane.INFORMATION_MESSAGE);
    }

    public void onCurve(ActionEvent evt) {
        int result = JOptionPane.showOptionDialog(this,
                wireframeEditDialog,
                "Parameters",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                null);
        if (result == JOptionPane.OK_OPTION) {
            wireframeEditDialog.applyChanges();
            viewport.setWireframe(wireframeEditDialog.getWireframe());
            viewport.repaint();
        } else {
            wireframeEditDialog.resetChanges();
        }
        revalidate();
    }

    public void onReset(ActionEvent evt) {
        viewport.resetRotation();
        revalidate();
    }

    public static void main(String[] args) throws Exception {
        AppWindow appWindow = new AppWindow();
        appWindow.pack();
        appWindow.setVisible(true);
    }
}
