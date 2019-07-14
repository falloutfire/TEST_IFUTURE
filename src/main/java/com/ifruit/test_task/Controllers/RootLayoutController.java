package com.ifruit.test_task.Controllers;

import com.ifruit.test_task.Main;
import javafx.event.ActionEvent;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;

public class RootLayoutController {

    private Main main;

    public void initialize() {

    }

    public void onClickFolder(ActionEvent actionEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        configuringDirectoryChooser(directoryChooser);
        File dir = directoryChooser.showDialog(main.getPrimaryStage());
        if (dir != null) {
            main.setROOT_FOLDER(dir.getPath());
            main.getMainLayoutController().createTree();
            main.getMainLayoutController().getTreeView().setRoot(main.getMainLayoutController().getRootTreeItem());
        }
    }

    public void onClickExtensions(ActionEvent actionEvent) throws IOException {
        boolean isReload = main.showExtensionsLayout();
        System.out.println(isReload);
    }

    private void configuringDirectoryChooser(DirectoryChooser directoryChooser) {
        directoryChooser.setTitle("Выбор директории");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    }

    public void setMain(Main main) {
        this.main = main;
    }
}