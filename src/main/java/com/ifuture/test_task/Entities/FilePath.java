package com.ifuture.test_task.Entities;

import java.nio.file.Path;

/**
 * Класс для пути с перезаписанным методом toString.
 * Только последняя часть пути отображается как узел дерева, а не весь путь.
 */
public class FilePath {

    private Path path;
    private String text;

    public FilePath(Path path) {

        this.path = path;
        if (path.getNameCount() == 0) {
            this.text = path.toString();
        } else {
            this.text = path.getName(path.getNameCount() - 1).toString();
        }
    }

    public Path getPath() {
        return path;
    }

    public String toString() {
        return text;
    }
}