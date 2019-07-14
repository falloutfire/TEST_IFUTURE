package com.ifruit.test_task.Parser;

import com.ifruit.test_task.Entities.FilePath;
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

    public String[] getSplitedPath(File file) {
        String[] splitedPath = new String[file.toPath().getNameCount() + 1];
        splitedPath[0] = file.toPath().getRoot().toString();
        for (int i = 0; i < splitedPath.length - 1; i++) {
            splitedPath[i + 1] = file.toPath().getName(i).toString() + "/";
        }
        splitedPath[splitedPath.length - 1] = splitedPath[splitedPath.length - 1].substring(0, splitedPath[splitedPath.length - 1].length() - 1);
        return splitedPath;
    }

    @Override
    protected Void call() throws Exception {
        filter(root, filter, filteredRoot);
        return null;
    }

    /**
     * Create new filtered tree structure
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
     * Comparator for tree filter
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
        String ext = i > 0 ? name.substring(i + 1) : "";
        return ext;
    }
}
