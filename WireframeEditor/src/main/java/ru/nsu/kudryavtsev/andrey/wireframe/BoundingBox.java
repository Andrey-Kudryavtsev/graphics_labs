package ru.nsu.kudryavtsev.andrey.wireframe;

import lombok.Getter;

import java.awt.*;

public class BoundingBox {
    @Getter
    private Point3D p1;
    @Getter
    private Point3D p2;

    public BoundingBox(Point3D p1, Point3D p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public Point3D getCenter() {
        return new Point3D((p2.x+p1.x)/2, (p2.y+p1.y)/2, (p2.z+p1.z)/2);
    }
}
