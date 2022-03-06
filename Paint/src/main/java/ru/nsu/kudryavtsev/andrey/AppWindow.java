package ru.nsu.kudryavtsev.andrey;

import ru.nsu.kudryavtsev.andrey.fileutils.FileUtils;
import ru.nsu.kudryavtsev.andrey.tools.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class AppWindow extends MainFrame {
    public static final Color DEFAULT_COLOR = Color.BLACK;
    private final WorkPanel workPanel;
    private final HashMap<String, Tool> tools = new HashMap<>();
    private JLabel colorViewer;

    public AppWindow() {
        super();
        setPreferredSize(new Dimension(800, 600));
        setMinimumSize(new Dimension(640, 480));
        setResizable(true);
        setLocationByPlatform(true);

        try
        {
            addSubMenu("File", KeyEvent.VK_F);
            addMenuItem("File/Save", "Save current paint area", KeyEvent.VK_S, "save.png", "onSave");
            addMenuItem("File/Open", "Open existing picture", KeyEvent.VK_O, "open.png", "onOpen");
            addMenuSeparator("File");
            addMenuItem("File/Exit", "Exit application", KeyEvent.VK_E, "exit.png", "onExit");

            addSubMenu("View", KeyEvent.VK_V);
            addSubMenu("View/Tools", KeyEvent.VK_T, "tools.png");

            addRadioButtonMenuItem("View/Tools/Line", "Tools", "Draw a line between two points", KeyEvent.VK_L, "line.png", "onLine");
            addRadioButtonMenuItem("View/Tools/Shape", "Tools", "Draw a regular polygon or a regular star", KeyEvent.VK_S, "shape.png", "onShape");
            addRadioButtonMenuItem("View/Tools/Fill", "Tools", "Fill the area of one color", KeyEvent.VK_F, "fill.png", "onFill");
            addMenuItem("View/Palette", "Choose color", KeyEvent.VK_P, "palette.png", "onPalette");
            addMenuItem("View/Clear", "Clear paint area", KeyEvent.VK_C, "clear.png", "onClear");

            addSubMenu("Help", KeyEvent.VK_H);
            addMenuItem("Help/About...", "Shows program version and copyright information", KeyEvent.VK_A, "about.png", "onAbout");


            addToolBarButton("File/Exit");
            addToolBarSeparator();
            addToolBarButton("File/Save");
            addToolBarButton("File/Open");
            addToolBarSeparator();
            addToolBarToggleButton("View/Tools/Line", "Tools");
            addToolBarToggleButton("View/Tools/Shape", "Tools");
            addToolBarToggleButton("View/Tools/Fill", "Tools");
            addToolBarSeparator();
            addToolBarButton("View/Palette");
            initColorViewer();
            toolBar.add(colorViewer);
            addToolBarButton("View/Clear");
            addToolBarSeparator();
            addToolBarButton("Help/About...");

            tools.put("None", new NoneTool());
            tools.put("Line", new LineTool());
            tools.put("Shape", new ShapeTool());
            tools.put("Fill", new FillTool());

            workPanel = new WorkPanel();
            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    workPanel.resizeWorkPanel();
                }
            });

            JScrollPane sp = new JScrollPane(workPanel);
            add(sp);
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private void initColorViewer() {
        colorViewer = new JLabel(new ImageIcon(new BufferedImage(24, 24, BufferedImage.TYPE_INT_RGB)));
    }

    private void updateColorViewer(Color color) {
        BufferedImage c = new BufferedImage(24, 24, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = (Graphics2D) c.getGraphics();
        g2d.setColor(color);
        g2d.fillRect(0, 0, 24, 24);
        colorViewer.setIcon(new ImageIcon(c));
    }

    public void onAbout()
    {
        JOptionPane.showMessageDialog(this, """
                Paint, version 1.0
                Tools:
                    Line - draw a line between two points
                    Shape - draw a regular polygon or a regular star
                    Fill - fill the area of one color
                Utils:
                    Palette - choose a color
                    Clear - clear all paint area
                You are able to save your paint area in .png format, as well as open files in .png, .jpg, .bmp and .gif formats
                
                Copyright \u00a9 2022 Kudryavtsev Andrey, FIT, group 19203""", "About Init", JOptionPane.INFORMATION_MESSAGE);
    }

    public void onExit()
    {
        System.exit(0);
    }

    public void onLine() {
        LineTool lineTool = (LineTool) tools.get("Line");
        int result = JOptionPane.showOptionDialog(this,
                lineTool.getDialog(),
                "Parameters",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                null);
        if (result == JOptionPane.OK_OPTION) {
            lineTool.applyChanges();
        }
        workPanel.setCurTool(lineTool);
    }

    public void onShape() {
        ShapeTool shapeTool = (ShapeTool) tools.get("Shape");
        int result = JOptionPane.showOptionDialog(this,
                shapeTool.getDialog(),
                "Parameters",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                null);
        if (result == JOptionPane.OK_OPTION) {
            shapeTool.applyChanges();
        }
        workPanel.setCurTool(shapeTool);
    }

    public void onFill() {
        workPanel.setCurTool(tools.get("Fill"));
    }

    public void onClear() {
        workPanel.clearArea();
    }

    public void onPalette() {
        Color color = JColorChooser.showDialog(this, "Choose a color", DEFAULT_COLOR);
        if (color != null) {
            updateColorViewer(color);
            workPanel.setCurColor(color);
        }
    }

    public void onSave() {
        File file = FileUtils.getSaveFileName(this, "png", "png image");
        if (file == null) {
            return;
        }
        try {
            ImageIO.write(workPanel.getImg(), "png", file);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Unable to save the file", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void onOpen() {
        File file = FileUtils.getOpenFileName(this, "png", "png image");
        if (file == null) {
            return;
        }
        if (!FileUtils.isCorrectExtForOpen(file)) {
            JOptionPane.showMessageDialog(this, "Wrong file extension", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            BufferedImage img = ImageIO.read(file);
            workPanel.setImg(img);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Unable to open the file", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        AppWindow appWindow = new AppWindow();
        appWindow.pack();
        appWindow.setVisible(true);
    }
}
