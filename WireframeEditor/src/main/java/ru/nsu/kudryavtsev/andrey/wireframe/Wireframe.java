package ru.nsu.kudryavtsev.andrey.wireframe;

import lombok.Getter;
import ru.nsu.kudryavtsev.andrey.editdialog.BSpline;

public class Wireframe {
    public static final int DEFAULT_M1 = 30;
    public static final int DEFAULT_M2 = 70;
    @Getter
    private final BSpline generatrix;
    @Getter
    private int M1;
    @Getter
    private int M2;

    public Wireframe(BSpline generatrix, int M1, int M2) {
        this.generatrix = generatrix;
        this.M1 = M1;
        this.M2 = M2;
    }

    public static Wireframe createDefaultWireframe() {
        return new Wireframe(BSpline.createDefaultBSpline(), DEFAULT_M1, DEFAULT_M2);
    }
}
