package ru.nsu.kudryavtsev.andrey.wireframe;

import lombok.Getter;
import lombok.Setter;

public class Camera {
    @Getter @Setter
    private Point3D position;

    public Camera(Point3D position) {
        this.position = position;
    }
}
