package ru.nsu.kudryavtsev.andrey.wireframe;

import java.awt.*;

public class Point3D {
    public int x;
    public int y;
    public int z;

    public Point3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void move(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void translate(int dx, int dy, int dz) {
        this.x += dx;
        this.y += dy;
        this.z += dz;
    }
}
