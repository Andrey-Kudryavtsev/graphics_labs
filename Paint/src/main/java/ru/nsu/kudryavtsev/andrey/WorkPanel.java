package ru.nsu.kudryavtsev.andrey;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.kudryavtsev.andrey.tools.NoneTool;
import ru.nsu.kudryavtsev.andrey.tools.Tool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class WorkPanel extends JPanel implements MouseListener {
    @Setter
    private Tool curTool = new NoneTool();
    @Setter
    private Color curColor = Color.BLACK;
    @Getter
    private BufferedImage img;

    public WorkPanel() {
        addMouseListener(this);
    }

    public void setImg(BufferedImage img) {
        this.img = img;
        setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
        repaint();
    }

    public void resizeWorkPanel() {
        if (img == null) {
            img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics g = img.getGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, img.getWidth(), img.getHeight());
            return;
        }
        BufferedImage tmp = new BufferedImage(Math.max(getWidth(), img.getWidth()), Math.max(getHeight(), img.getHeight()), BufferedImage.TYPE_INT_RGB);
        Graphics g = tmp.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, tmp.getWidth(), tmp.getHeight());
        g.drawImage(img, 0, 0, this);
        img = tmp;

        setPreferredSize(new Dimension(getWidth(), getHeight()));
        repaint();
    }

    public void clearArea() {
        Graphics g = img.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        repaint();
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(img, 0, 0, this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        curTool.mousePressed(e, curColor, img);
        repaint();
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
}
