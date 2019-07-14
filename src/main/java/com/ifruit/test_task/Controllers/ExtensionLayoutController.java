package com.ifruit.test_task.Controllers;

import com.ifruit.test_task.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ExtensionLayoutController {

    @FXML
    private TextField extField;
    private Stage dialogStage;
    private Main main;
    private boolean okClicked = false;

    public void initialize() {
        extField.setText(Main.getEXTENSION());
    }

    public void onClickSave(ActionEvent actionEvent) {
        if (extField.getText() != null) {
            main.setEXTENSION(extField.getText());
        } else {
            main.setEXTENSION("log");
        }
        okClicked = true;
        dialogStage.close();
    }

    public void onClickCancel(ActionEvent actionEvent) {
        dialogStage.close();
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isOkClicked() {
        return okClicked;
    }
}
