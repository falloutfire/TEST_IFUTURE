package com.ifuture.test_task.Controllers;

import com.ifuture.test_task.Entities.FilePath;
import com.ifuture.test_task.Main;
import com.ifuture.test_task.Parser.TreeBuilder;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class MainLayoutController {

    private TextField filter;
    @FXML
    private VBox vboxFilePane;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private TabPane textTabPane;
    @FXML
    private SplitPane splitPane;
    private TreeView<FilePath> treeView;
    private TreeItem<FilePath> rootTreeItem;
    private Main main;

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

    public void initialize() {
        splitPane.setDividerPositions(0.391);
        AnchorPane pane = (AnchorPane) splitPane.getItems().get(0);
        pane.maxWidthProperty().bind(splitPane.widthProperty().multiply(0.391));
        pane.minWidthProperty().bind(splitPane.widthProperty().multiply(0.391));

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

        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> openFile(newValue));
    }

    /**
     * Открытие файла из дерева
     */
    private void openFile(TreeItem<FilePath> newValue) {
        if (newValue != null) {
            if (newValue.getValue().getPath().toFile().isFile()) {
                System.out.println(newValue.getValue().getPath());
                TextTab tab = new TextTab(newValue.getValue(), filter.getText());
                tab.initialize();
                textTabPane.getTabs().add(tab);
            }
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
        TreeItem<FilePath> filteredRoot = createTreeRoot();
        TreeBuilder builder = new TreeBuilder(rootTreeItem, filter, filteredRoot, Main.getEXTENSION(), progressBar);
        new Thread(() -> {
            if (filter.isEmpty()) {
                Platform.runLater(() -> treeView.setRoot(rootTreeItem));
            } else {
                builder.run();
                Platform.runLater(() -> treeView.setRoot(filteredRoot));
            }
        }).start();
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
}
