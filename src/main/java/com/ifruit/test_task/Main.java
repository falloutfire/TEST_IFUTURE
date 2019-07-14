package com.ifruit.test_task;

import com.ifruit.test_task.Controllers.ExtensionLayoutController;
import com.ifruit.test_task.Controllers.MainLayoutController;
import com.ifruit.test_task.Controllers.RootLayoutController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Main extends Application {

    private static String ROOT_FOLDER = "";
    private static String EXTENSION = "txt";
    private Stage primaryStage;
    private BorderPane rootLayout;
    private MainLayoutController mainLayoutController;
    private RootLayoutController rootLayoutController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Test");
        primaryStage.sizeToScene();
        setInitRoot();
        showRootLayout();
        showMainLayout();
    }

    private void showRootLayout() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Layouts/RootLayout.fxml"));
        rootLayout = loader.load();
        rootLayoutController = loader.getController();
        rootLayoutController.setMain(this);
        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showMainLayout() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Layouts/MainLayout.fxml"));
        AnchorPane mainPane = loader.load();
        mainLayoutController = loader.getController();
        mainLayoutController.setMain(this);
        rootLayout.setCenter(mainPane);
    }

    public boolean showExtensionsLayout() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/Layouts/ExtensionLayout.fxml"));
        AnchorPane pane = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Настроки");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        Scene scene = new Scene(pane);
        dialogStage.setScene(scene);
        dialogStage.setResizable(false);

        ExtensionLayoutController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setMain(this);

        dialogStage.showAndWait();
        return controller.isOkClicked();
    }

    public void showAlert(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.setHeaderText(header);
        alert.setTitle(title);
        alert.showAndWait();
    }

    private void setInitRoot() {
        ROOT_FOLDER = new File(System.getProperty("user.home")).getPath();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static String getRootFolder() {
        return ROOT_FOLDER;
    }

    public static String getEXTENSION() {
        return EXTENSION;
    }

    public void setEXTENSION(String EXTENSION) {
        Main.EXTENSION = EXTENSION;
    }

    public void setROOT_FOLDER(String ROOT_FOLDER) {
        Main.ROOT_FOLDER = ROOT_FOLDER;
    }

    public MainLayoutController getMainLayoutController() {
        return mainLayoutController;
    }

    public RootLayoutController getRootLayoutController() {
        return rootLayoutController;
    }
}