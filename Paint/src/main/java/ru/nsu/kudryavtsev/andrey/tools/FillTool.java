package ru.nsu.kudryavtsev.andrey.tools;

import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Stack;

public class FillTool implements Tool {
    private Color color = Color.BLACK;
    private int oldColor;
    @Override
    public JPanel getDialog() {
        return null;
    }

    @Override
    public void applyChanges() {

    }

    @Override
    public void mousePressed(MouseEvent e, Color color, BufferedImage img) {
        this.color = color;
        spanFilling(e.getX(), e.getY(), img);
    }

    public void spanFilling(int seedX, int seedY, BufferedImage img) {
        oldColor = img.getRGB(seedX, seedY);
        if (oldColor == color.getRGB()) {
            return;
        }
        Stack<Span> spans = new Stack<>();
        Span initialSpan = findSpan(seedX, seedY, img);
        spans.push(initialSpan);

        while(!spans.empty()) {
            Span span = spans.pop();
            fillSpan(span, img);
            if (span.y - 1 >= 0) {
                findAdjacentSpans(spans, span, span.y - 1, img);
            }
            if (span.y + 1 < img.getHeight()) {
                findAdjacentSpans(spans, span, span.y + 1, img);
            }
        }
    }

    private void findAdjacentSpans(Stack<Span> spans, Span parentSpan, int y, BufferedImage img) {
        int x = parentSpan.start;
        while (x <= parentSpan.end) {
            if (img.getRGB(x, y) == oldColor) {
                Span span = findSpan(x, y, img);
                x = span.end;
                spans.push(span);
            }
            x++;
        }
    }

    private Span findSpan(int x, int y, BufferedImage img) {
        int start = x;
        int end = x;
        while (start >= 0 && img.getRGB(start, y) == oldColor) {
            start--;
        }
        while (end < img.getWidth() && img.getRGB(end, y) == oldColor) {
            end++;
        }
        return new Span(start + 1, end - 1, y);
    }

    private void fillSpan(Span span, BufferedImage img) {
        for (int x = span.start; x <= span.end; x++) {
            img.setRGB(x, span.y, color.getRGB());
        }
    }

    private static class Span {
        private final int start;
        private final int end;
        private final int y;

        Span(int start, int end, int y) {
            this.start = start;
            this.end = end;
            this.y = y;
        }
    }
}
