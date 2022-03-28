package ru.nsu.kudryavtsev.andrey.filters;

public interface KernelOperation {
    int makeOperation(double[][] kernel, int x, int y);
}
