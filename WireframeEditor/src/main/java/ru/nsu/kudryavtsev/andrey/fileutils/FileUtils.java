package ru.nsu.kudryavtsev.andrey.fileutils;

import ru.nsu.kudryavtsev.andrey.editdialog.BSpline;
import ru.nsu.kudryavtsev.andrey.editdialog.EditBox;
import ru.nsu.kudryavtsev.andrey.wireframe.Wireframe;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class FileUtils {
    private static File dataDirectory = null;
    /**
     * Returns File pointing to Data directory of current project. If Data directory is not found, returns project directory.
     * @return File object.
     */
    public static File getDataDirectory()
    {
        if(dataDirectory == null)
        {
            dataDirectory = new File(".");
            if (!dataDirectory.exists()) {
                dataDirectory = new File(".");
            }
            for(File f: dataDirectory.listFiles())
            {
                if(f.isDirectory() && f.getName().equals("wireframes"))
                {
                    dataDirectory = f;
                    break;
                }
            }
        }
        return dataDirectory;
    }

    /**
     * Prompts user for file name to save and returns it
     * @param parent - parent frame for file selection dialog
     * @param extension - preferred file extension (example: "txt")
     * @param description - description of specified file type (example: "Text files")
     * @return File specified by user or null if user canceled operation
     */
    public static File getSaveFileName(JFrame parent, String extension, String description)
    {
        JFileChooser fileChooser = new JFileChooser();
        FileFilter filter = new ExtensionFileFilter(extension, description);
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setCurrentDirectory(getDataDirectory());
        if (fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION)
        {
            File f = fileChooser.getSelectedFile();
            f = new File(f.getParent(), f.getName()+"."+extension);
            return f;
        }
        return null;
    }

    /**
     * Prompts user for file name to open and returns it
     * @param parent - parent frame for file selection dialog
     * @param extension - preferred file extension (example: "txt")
     * @param description - description of specified file type (example: "Text files")
     * @return File specified by user or null if user canceled operation
     */
    public static File getOpenFileName(JFrame parent, String extension, String description)
    {
        JFileChooser fileChooser = new JFileChooser();
        FileFilter filter = new ExtensionFileFilter(extension, description);
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setCurrentDirectory(getDataDirectory());
        if(fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION)
        {
            File f = fileChooser.getSelectedFile();
            if(!f.getName().contains("."))
                f = new File(f.getParent(), f.getName()+"."+extension);
            return f;
        }
        return null;
    }

    public static boolean isCorrectExtForOpen(File file) {
        return file.getName().endsWith(".txt");
    }

    public static void saveWireframe(File file, Wireframe wireframe) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(file));
        writer.printf("%d\n", wireframe.getGeneratrix().getK());
        writer.printf("%d\n", wireframe.getGeneratrix().getN());
        writer.printf("%d\n", wireframe.getM1());
        writer.printf("%d\n", wireframe.getM2());
        for (Point point : wireframe.getGeneratrix().getControlPoints()) {
            writer.printf("%d %d\n", point.x, point.y);
        }
        writer.close();
    }

    public static Wireframe loadWireframe(File file) throws IOException {
        Scanner reader = new Scanner(file);
        int K = reader.nextInt();
        if (K < EditBox.MIN_CONTROL_POINTS || K > EditBox.MAX_CONTROL_POINTS) {
            reader.close();
            return null;
        }

        int N = reader.nextInt();
        if (N < EditBox.MIN_SECTION_SEGMENTS || N > EditBox.MAX_SECTION_SEGMENTS) {
            reader.close();
            return null;
        }

        int M1 = reader.nextInt();
        if (M1 < EditBox.MIN_GENERATRICES || M1 > EditBox.MAX_GENERATRICES) {
            reader.close();
            return null;
        }

        int M2 = reader.nextInt();
        if (M2 < EditBox.MIN_CIRCLE_SEGMENTS || M2 > EditBox.MAX_CIRCLE_SEGMENTS) {
            reader.close();
            return null;
        }

        ArrayList<Point> controlPoints = new ArrayList<>();
        while (reader.hasNextInt()) {
            int x = reader.nextInt();
            if (!reader.hasNextInt()) {
                reader.close();
                return null;
            }
            int y = reader.nextInt();
            controlPoints.add(new Point(x, y));
        }
        if (controlPoints.size() != K) {
            reader.close();
            return null;
        }
        return new Wireframe(new BSpline(controlPoints, N), M1, M2);
    }
}
