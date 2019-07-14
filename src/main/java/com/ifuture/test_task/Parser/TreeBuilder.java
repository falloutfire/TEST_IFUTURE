package com.ifuture.test_task.Parser;

import com.ifuture.test_task.Entities.FilePath;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TreeItem;

import java.io.File;
import java.io.IOException;

public class TreeBuilder extends Task<Void> {

    private String EXTENSION;
    private TreeItem<FilePath> root;
    private TreeItem<FilePath> filteredRoot;
    private ProgressBar progressBar;
    private String filter;


    public TreeBuilder(TreeItem<FilePath> root, String filter, TreeItem<FilePath> filteredRoot, String EXTENSION, ProgressBar progressBar) {
        this.root = root;
        this.filter = filter;
        this.filteredRoot = filteredRoot;
        this.EXTENSION = EXTENSION;
        this.progressBar = progressBar;
    }

    @Override
    protected Void call() throws Exception {
        progressBar.progressProperty().unbind();
        progressBar.progressProperty().bind(this.progressProperty());
        filter(root, filter, filteredRoot);
        progressBar.progressProperty().unbind();
        progressBar.setProgress(0);
        return null;
    }

    /**
     * Создать новое фильтрованное дерево
     */
    private void filter(TreeItem<FilePath> root, String filter, TreeItem<FilePath> filteredRoot) throws IOException {

        for (TreeItem<FilePath> child : root.getChildren()) {

            TreeItem<FilePath> filteredChild = new TreeItem<>(child.getValue());
            filteredChild.setExpanded(true);

            filter(child, filter, filteredChild);

            if (!filteredChild.getChildren().isEmpty() || isMatch(filteredChild.getValue(), filter)) {
                filteredRoot.getChildren().add(filteredChild);
            }
        }
    }

    /**
     * Фильтр
     */
    private boolean isMatch(FilePath value, String filter) throws IOException {
        if (value.getPath().toFile().isFile() && getFileExtension(value.getPath().toFile()).equals(EXTENSION)) {
            return new FileParser().isTextFound(value.getPath().toFile(), filter);
        } else {
            return false;
        }
    }

    private String getFileExtension(File file) {
        if (file == null) {
            return "";
        }
        String name = file.getName();
        int i = name.lastIndexOf('.');
        return i > 0 ? name.substring(i + 1) : "";
    }
}
