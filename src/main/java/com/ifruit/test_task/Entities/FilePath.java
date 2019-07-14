package com.ifruit.test_task.Entities;

import java.nio.file.Path;

/**
 * Оболочка для пути с перезаписанным методом toString. Мы хотим видеть только последнюю часть пути как узел дерева, а не весь путь.
 */
public class FilePath {

    Path path;
    String text;

    public FilePath(Path path) {

        this.path = path;

        // display text: the last path part
        // consider root, e. g. c:\
        if (path.getNameCount() == 0) {
            this.text = path.toString();
        }
        // consider folder structure
        else {
            this.text = path.getName(path.getNameCount() - 1).toString();
        }

    }

    public Path getPath() {
        return path;
    }

    public String toString() {

        // hint: if you'd like to see the entire path, use this:
        // return path.toString();

        // show only last path part
        return text;

    }
}