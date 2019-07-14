package com.ifruit.test_task.Controllers;

import com.ifruit.test_task.Entities.FilePath;
import com.ifruit.test_task.Main;
import com.ifruit.test_task.Parser.TreeBuilder;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class MainLayoutController {

    private TreeView<FilePath> treeView;
    private TreeItem<FilePath> rootTreeItem;
    private TextField filter;
    public VBox vboxFilePane;
    public ProgressBar progressBar;
    public TabPane textTabPane;
    private Main main;


    public void initialize() {
        // filter
        filter = new TextField();
        filter.setPromptText("Введите искомый текст:");
        filter.textProperty().addListener((observable, oldValue, newValue) -> {
            textTabPane.getTabs().clear();
            filterChanged(newValue);
        });

        treeView = new TreeView<>();
        VBox.setVgrow(treeView, Priority.ALWAYS);
        vboxFilePane.getChildren().addAll(filter, treeView);

        createTree();

        treeView.setRoot(rootTreeItem);

        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            try {
                openFile(newValue);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Открытие файла из дерева
     */
    private void openFile(TreeItem<FilePath> newValue) throws IOException {
        if (newValue != null) {
            if (newValue.getValue().getPath().toFile().isFile()) {
                System.out.println(newValue.getValue().getPath());
                CustomTab tab = new CustomTab(newValue.getValue(), filter.getText());
                tab.initialize();
                textTabPane.getTabs().add(tab);
            }
        }
    }

    /**
     * Итерация по структуре каталогов и создание дерева
     */
    private static void createTree(TreeItem<FilePath> rootItem) {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(rootItem.getValue().getPath())) {

            for (Path path : directoryStream) {

                TreeItem<FilePath> newItem = new TreeItem<>(new FilePath(path));
                newItem.setExpanded(true);

                rootItem.getChildren().add(newItem);

                if (Files.isDirectory(path)) {
                    createTree(newItem);
                }
            }
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
    }

    /**
     * Создание структуры буд фильтров
     */
    void createTree() {
        rootTreeItem = createTreeRoot();
        createTree(rootTreeItem);
        rootTreeItem.getChildren().sort(Comparator.comparing(t -> t.getValue().toString().toLowerCase()));
    }

    /**
     * Показать оригинальное дерево или отфильтрованное дерево в зависимости от фильтра
     */
    private void filterChanged(String filter) {
        progressBar.progressProperty().unbind();
        TreeItem<FilePath> filteredRoot = createTreeRoot();
        TreeBuilder builder = new TreeBuilder(rootTreeItem, filter, filteredRoot, Main.getEXTENSION(), progressBar);
        new Thread(() -> Platform.runLater(
                () -> {
                    if (filter.isEmpty()) {
                        treeView.setRoot(rootTreeItem);
                    } else {
                        builder.run();
                        progressBar.progressProperty().bind(builder.progressProperty());
                        treeView.setRoot(filteredRoot);
                        if (builder.isDone()) {
                            progressBar.progressProperty().unbind();
                            progressBar.setProgress(0);
                        }
                    }
                })).start();
        progressBar.progressProperty().unbind();
        progressBar.setProgress(0);
    }

    /**
     * Создать корневой узел. Используется для исходного дерева и отфильтрованного дерева.
     */
    private TreeItem<FilePath> createTreeRoot() {
        TreeItem<FilePath> root = new TreeItem<>(new FilePath(Paths.get(Main.getRootFolder())));
        root.setExpanded(true);
        return root;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    TreeView<FilePath> getTreeView() {
        return treeView;
    }

    TreeItem<FilePath> getRootTreeItem() {
        return rootTreeItem;
    }

    public Main getMain() {
        return main;
    }
}
