package ru.nsu.kudryavtsev.andrey;

import ru.nsu.kudryavtsev.andrey.fileutils.FileUtils;
import ru.nsu.kudryavtsev.andrey.filters.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class AppWindow extends MainFrame {
    private final WorkPanel workPanel;
    private final ResizeTool resizeTool = new ResizeTool(AffineTransformOp.TYPE_BILINEAR);
    private final HashMap<String, Filter> filters = new HashMap<>();

    public AppWindow() throws Exception {
        super();
        setPreferredSize(new Dimension(800, 600));
        setMinimumSize(new Dimension(640, 480));
        setResizable(true);
        setLocationByPlatform(true);

        addSubMenu("File", KeyEvent.VK_F);
        addMenuItem("File/Save", "Save displayed image", KeyEvent.VK_S, "save.png", "onSave");
        addMenuItem("File/Open", "Open existing image", KeyEvent.VK_O, "open.png", "onOpen");
        addMenuSeparator("File");
        addMenuItem("File/Exit", "Exit application", KeyEvent.VK_E, "exit.png", "onExit");

        addSubMenu("View", KeyEvent.VK_V);
        addMenuItem("View/Fit to screen", "Fit image to the screen", KeyEvent.VK_F, "enlarge.png", "onFit");
        addMenuItem("View/Rotate", "Rotate image around it's center", KeyEvent.VK_R, "rotate.png", "onRotate");
        addMenuSeparator("View");
        addMenuItem("View/Black and white", "Turn image into black and white colors", KeyEvent.VK_W, "black-and-white.png", "onBlackAndWhite");
        addMenuItem("View/Negative", "Inverse image colors", KeyEvent.VK_N, "negative.png", "onNegative");
        addMenuItem("View/Gamma correction", "Change brightness of the image", KeyEvent.VK_G, "gamma.png", "onGamma");
        addMenuSeparator("View");
        addMenuItem("View/Blur", "Make image less sharp (gaussian blur)", KeyEvent.VK_B, "blur.png", "onBlur");
        addMenuItem("View/Sharpening", "Sharpen an image", KeyEvent.VK_S, "sharpening.png", "onSharpening");
        addMenuItem("View/Embossing", "Make image looks like embossing", KeyEvent.VK_E, "embossing.png", "onEmbossing");
        addMenuItem("View/Contouring", "Highlight image contours", KeyEvent.VK_C, "contouring.png", "onContouring");
        addMenuItem("View/Erosion", "Make dark areas even darker", KeyEvent.VK_S, "erosion.png", "onErosion");
        addMenuItem("View/Watercoloring", "Make image looks like watercolor painting", KeyEvent.VK_A, "watercoloring.png", "onWatercoloring");
        addMenuItem("View/Ordered dithering", "Decrease amount of colors in the palette (ordered dithering)", KeyEvent.VK_O, "ord-dithering.png", "onOrderedDithering");
        addMenuItem("View/Floyd-Steinberg dithering", "Decrease amount of colors in the palette (Floyd-Steinberg dithering)", KeyEvent.VK_F, "fs-dithering.png", "onFloydSteinbergDithering");
        addMenuSeparator("View");
        addMenuItem("View/Back to original", "Back to original image", KeyEvent.VK_O, "back.png", "onOriginal");

        addSubMenu("Help", KeyEvent.VK_H);
        addMenuItem("Help/About", "Shows program version and copyright information", KeyEvent.VK_A, "about.png", "onAbout");


        addToolBarButton("File/Exit");
        addToolBarSeparator();
        addToolBarButton("File/Save");
        addToolBarButton("File/Open");
        addToolBarSeparator();
        addToolBarButton("View/Back to original");
        addToolBarButton("View/Fit to screen");
        addToolBarButton("View/Rotate");
        addToolBarSeparator();
        addToolBarButton("View/Black and white");
        addToolBarButton("View/Negative");
        addToolBarButton("View/Gamma correction");
        addToolBarSeparator();
        addToolBarButton("View/Blur");
        addToolBarButton("View/Sharpening");
        addToolBarButton("View/Embossing");
        addToolBarButton("View/Contouring");
        addToolBarButton("View/Erosion");
        addToolBarButton("View/Watercoloring");
        addToolBarButton("View/Ordered dithering");
        addToolBarButton("View/Floyd-Steinberg dithering");
        addToolBarSeparator();
        addToolBarButton("Help/About");


        filters.put("BlackAndWhite", new BlackAndWhiteFilter());
        filters.put("Negative", new NegativeFilter());
        filters.put("Blur", new BlurFilter(2, 2));
        filters.put("Sharpening", new SharpeningFilter());
        filters.put("Embossing", new EmbossingFilter());
        filters.put("GammaCorrection", new GammaCorrectionFilter(1));
        filters.put("Contouring", new ContouringFilter(true, 128));
        filters.put("Erosion", new ErosionFilter());
        filters.put("Watercoloring", new WatercoloringFilter());
        filters.put("OrderedDithering", new OrderedDitheringFilter(2, 2, 2));
        filters.put("FloydSteinbergDithering", new FloydSteinbergDitheringFilter(2, 2, 2));
        filters.put("Rotate", new RotateFilter(45));

        JScrollPane sp = new JScrollPane();
        workPanel = new WorkPanel(sp);
        add(sp);
    }

    public void onSave(ActionEvent evt) {
        File file = FileUtils.getSaveFileName(this, "png", "png image");
        if (file == null || workPanel.getShowedImg() == null) {
            return;
        }
        try {
            ImageIO.write(workPanel.getShowedImg(), "png", file);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Unable to save the file", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void onOpen(ActionEvent evt) {
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

    public void onExit(ActionEvent evt)
    {
        System.exit(0);
    }

    public void onAbout(ActionEvent evt)
    {
        JOptionPane.showMessageDialog(this, """
                ImageEditor, version 1.0
                Filters:
                    Fit to screen - Fit image to the screen
                    Rotate - Rotate image around it's center
                    Black and white - Turn image into black and white colors
                    Negative - Inverse image colors
                    Gamma correction - Change brightness of the image
                    Blur - Make image less sharp (gaussian blur)
                    Sharpening - Sharpen an image
                    Embossing - Make image looks like embossing
                    Contouring - Highlight image contours
                    Erosion - Make dark areas even darker
                    Watercoloring - Make image looks like watercolor painting
                    Ordered dithering - Decrease amount of colors in the palette (ordered dithering)
                    Floyd-Steinberg dithering - Decrease amount of colors in the palette (Floyd-Steinberg dithering)
                Utils:
                    Back to original - Back to original image
                You are able to save filtered images in .png format, as well as open files in .png, .jpg, .bmp and .gif formats
                
                Copyright \u00a9 2022 Kudryavtsev Andrey, FIT, group 19203""", "About Init", JOptionPane.INFORMATION_MESSAGE);
    }

    public void onFit(ActionEvent evt) {
        int result = JOptionPane.showOptionDialog(this,
                resizeTool.getDialog(),
                "Parameters",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                null);
        if (result == JOptionPane.OK_OPTION) {
            resizeTool.applyChanges();
        } else {
            resizeTool.resetChanges();
        }
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        workPanel.fitToScreen(resizeTool);
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    public void onOriginal(ActionEvent evt) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        workPanel.backToOriginal();
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    public void onBlackAndWhite(ActionEvent evt) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        workPanel.apply(filters.get("BlackAndWhite"));
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    public void onNegative(ActionEvent evt) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        workPanel.apply(filters.get("Negative"));
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    public void onBlur(ActionEvent evt) {
        BlurFilter blurFilter = (BlurFilter) filters.get("Blur");
        int result = JOptionPane.showOptionDialog(this,
                blurFilter.getDialog(),
                "Parameters",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                null);
        if (result == JOptionPane.OK_OPTION) {
            blurFilter.applyChanges();
        } else {
            blurFilter.resetChanges();
        }
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        workPanel.apply(filters.get("Blur"));
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    public void onSharpening(ActionEvent evt) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        workPanel.apply(filters.get("Sharpening"));
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    public void onEmbossing(ActionEvent evt) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        workPanel.apply(filters.get("Embossing"));
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    public void onGamma(ActionEvent evt) {
        GammaCorrectionFilter gammaCorrectionFilter = (GammaCorrectionFilter) filters.get("GammaCorrection");
        int result = JOptionPane.showOptionDialog(this,
                gammaCorrectionFilter.getDialog(),
                "Parameters",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                null);
        if (result == JOptionPane.OK_OPTION) {
            gammaCorrectionFilter.applyChanges();
        } else {
            gammaCorrectionFilter.resetChanges();
        }
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        workPanel.apply(gammaCorrectionFilter);
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    public void onContouring(ActionEvent evt) {
        ContouringFilter contouringFilter = (ContouringFilter) filters.get("Contouring");
        int result = JOptionPane.showOptionDialog(this,
                contouringFilter.getDialog(),
                "Parameters",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                null);
        if (result == JOptionPane.OK_OPTION) {
            contouringFilter.applyChanges();
        } else {
            contouringFilter.resetChanges();
        }
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        workPanel.apply(contouringFilter);
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    public void onErosion(ActionEvent evt) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        workPanel.apply(filters.get("Erosion"));
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    public void onWatercoloring(ActionEvent evt) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        workPanel.apply(filters.get("Watercoloring"));
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    public void onRotate(ActionEvent evt) {
        RotateFilter rotateFilter = (RotateFilter) filters.get("Rotate");
        int result = JOptionPane.showOptionDialog(this,
                rotateFilter.getDialog(),
                "Parameters",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                null);
        if (result == JOptionPane.OK_OPTION) {
            rotateFilter.applyChanges();
        } else {
            rotateFilter.resetChanges();
        }
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        workPanel.rotate(rotateFilter);
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    public void onOrderedDithering(ActionEvent evt) {
        OrderedDitheringFilter orderedDitheringFilter = (OrderedDitheringFilter) filters.get("OrderedDithering");
        int result = JOptionPane.showOptionDialog(this,
                orderedDitheringFilter.getDialog(),
                "Parameters",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                null);
        if (result == JOptionPane.OK_OPTION) {
            orderedDitheringFilter.applyChanges();
        } else {
            orderedDitheringFilter.resetChanges();
        }
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        workPanel.apply(orderedDitheringFilter);
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    public void onFloydSteinbergDithering(ActionEvent evt) {
        FloydSteinbergDitheringFilter floydSteinbergDitheringFilter = (FloydSteinbergDitheringFilter) filters.get("FloydSteinbergDithering");
        int result = JOptionPane.showOptionDialog(this,
                floydSteinbergDitheringFilter.getDialog(),
                "Parameters",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                null);
        if (result == JOptionPane.OK_OPTION) {
            floydSteinbergDitheringFilter.applyChanges();
        } else {
            floydSteinbergDitheringFilter.resetChanges();
        }
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        workPanel.apply(floydSteinbergDitheringFilter);
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    public static void main(String[] args) throws Exception {
        AppWindow appWindow = new AppWindow();
        appWindow.pack();
        appWindow.setVisible(true);
    }
}
