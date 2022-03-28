package ru.nsu.kudryavtsev.andrey;

import lombok.Getter;
import ru.nsu.kudryavtsev.andrey.filters.Filter;
import ru.nsu.kudryavtsev.andrey.filters.ResizeTool;
import ru.nsu.kudryavtsev.andrey.filters.RotateFilter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

public class WorkPanel extends JPanel implements MouseListener, MouseMotionListener {
    private int lastX, lastY = -1;
    private final JScrollPane spImg;
    private BufferedImage originalImg; // real sized image
    private BufferedImage resizedImg; // unfiltered, but resized image
    private BufferedImage alteredImg; // filtered image
    @Getter
    private BufferedImage showedImg; // image to show to the viewer

    public WorkPanel(JScrollPane spImg) {
        this.spImg = spImg;
        spImg.setWheelScrollingEnabled(false);
        spImg.setDoubleBuffered(true);
        spImg.setViewportView(this);
        spImg.setViewportBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(4, 4, 4, 4),
                BorderFactory.createDashedBorder(Color.BLACK, 3, 1)
        ));
        spImg.revalidate();
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void setImg(BufferedImage img) {
        originalImg = cloneImg(img);
        resizedImg = cloneImg(img);
        alteredImg = cloneImg(img);
        showedImg = resizedImg;
        repaint();
    }

    private void swapShowedImg() {
        if (showedImg == resizedImg) {
            showedImg = alteredImg;
        } else {
            showedImg = resizedImg;
        }
    }

    public void fitToScreen(ResizeTool tool) {
        int fitWidth = spImg.getViewport().getWidth();
        if (spImg.getVerticalScrollBar().isVisible()) {
            fitWidth += spImg.getVerticalScrollBar().getWidth();
        }
        int fitHeight = spImg.getViewport().getHeight();
        if (spImg.getHorizontalScrollBar().isVisible()) {
            fitHeight += spImg.getHorizontalScrollBar().getHeight();
        }
        resizedImg = tool.getAlteredImage(originalImg, fitWidth, fitHeight);
        alteredImg = cloneImg(resizedImg);
        showedImg = resizedImg;
        repaint();
    }

    public void rotate(RotateFilter tool) {
        alteredImg = tool.getAlteredImage(resizedImg);
        showedImg = alteredImg;
        repaint();
    }

    public void backToOriginal() {
        resizedImg = cloneImg(originalImg);
        alteredImg = cloneImg(originalImg);
        showedImg = resizedImg;
        repaint();
    }

    public void apply(Filter filter) {
        alteredImg = filter.getAlteredImage(showedImg);
        showedImg = alteredImg;
        repaint();
    }

//    public void resizeWorkPanel() {
//        if (resizedImg == null) {
//            resizedImg = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
//            Graphics g = resizedImg.getGraphics();
//            g.setColor(Color.WHITE);
//            g.fillRect(0, 0, resizedImg.getWidth(), resizedImg.getHeight());
//            return;
//        }
//        BufferedImage tmp = new BufferedImage(Math.max(getWidth(), resizedImg.getWidth()), Math.max(getHeight(), resizedImg.getHeight()), BufferedImage.TYPE_INT_RGB);
//        Graphics g = tmp.getGraphics();
//        g.setColor(Color.WHITE);
//        g.fillRect(0, 0, tmp.getWidth(), tmp.getHeight());
//        g.drawImage(resizedImg, 0, 0, this);
//        resizedImg = tmp;
//
//        setPreferredSize(new Dimension(getWidth(), getHeight()));
//        repaint();
//    }

    private BufferedImage cloneImg(BufferedImage img) {
        if (img == null) {
            return null;
        }
        return new BufferedImage(img.getColorModel(), img.copyData(null), img.isAlphaPremultiplied(), null);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (showedImg != null) {
            setPreferredSize(new Dimension(showedImg.getWidth(), showedImg.getHeight()));
        }
        spImg.revalidate();

        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(showedImg, 0, 0, this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        swapShowedImg();
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        lastX = e.getX();
        lastY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point scroll = spImg.getViewport().getViewPosition();
        scroll.x += (lastX - e.getX());
        scroll.y += (lastY - e.getY());

        spImg.getHorizontalScrollBar().setValue(scroll.x);
        spImg.getVerticalScrollBar().setValue(scroll.y);
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}