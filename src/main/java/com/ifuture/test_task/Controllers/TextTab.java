package com.ifuture.test_task.Controllers;

import com.ifuture.test_task.Entities.FilePath;
import com.ifuture.test_task.Parser.FileParser;
import javafx.embed.swing.SwingNode;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.util.ArrayList;

/**
 * Класс для создания вклдаки с текстом
 */
class TextTab extends Tab {

    private FilePath filePath;
    private ArrayList<Integer> positions;
    private int currentPosition;
    private JTextArea area;
    private String findingText;
    private SwingNode swingNode;


    TextTab(FilePath filePath, String findingText) {
        super.setText(filePath.toString());
        this.filePath = filePath;
        this.positions = new ArrayList<>();
        this.currentPosition = 0;
        this.findingText = findingText;
    }

    void initialize() {
        new Thread(() -> new FileParser().getFindingTextPositions(filePath, findingText, positions)).start();

        VBox vboxTextPane = new VBox();

        swingNode = new SwingNode();
        createAndSetSwingContent(swingNode);

        VBox.setVgrow(swingNode, Priority.ALWAYS);
        Button btnNext = new Button("Next");
        Button btnPrev = new Button("Previous");
        Button btnSelect = new Button("Select All");
        btnNext.setPrefSize(90, 25);
        btnPrev.setPrefSize(90, 25);
        btnPrev.setPrefSize(90, 25);

        btnNext.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> nextPosition());
        btnPrev.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> prevPosition());
        btnSelect.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            selectText();
            area.setFocusable(true);
        });

        ToolBar toolBar = new ToolBar();
        toolBar.getItems().addAll(btnNext, btnPrev, btnSelect);

        vboxTextPane.getChildren().addAll(toolBar, swingNode);
        this.setContent(vboxTextPane);
    }

    private void selectText() {
        swingNode.setFocusTraversable(true);
        swingNode.requestFocus();
        area.requestFocus();
        area.grabFocus();
        area.selectAll();
    }

    private int getCurrentPosition() {
        return this.positions.get(this.currentPosition);
    }

    private void highLightWord() {
        Highlighter highlighter = this.area.getHighlighter();
        Highlighter.HighlightPainter painter =
                new DefaultHighlighter.DefaultHighlightPainter(Color.GREEN);
        int p0 = getCurrentPosition();
        int p1 = p0 + this.findingText.length();
        try {
            highlighter.removeAllHighlights();
            highlighter.addHighlight(p0, p1, painter);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void nextPosition() {
        this.area.setCaretPosition(getNextPosition());
    }

    private void prevPosition() {
        this.area.setCaretPosition(getPrevPosition());
    }

    private int getNextPosition() {
        if (this.currentPosition == this.positions.size() - 1) {
            this.currentPosition = 0;
        } else {
            this.currentPosition++;
        }

        highLightWord();
        return this.positions.get(this.currentPosition);
    }

    private int getPrevPosition() {
        if (this.currentPosition == 0) {
            this.currentPosition = this.positions.size() - 1;
        } else {
            this.currentPosition--;
        }
        highLightWord();
        return this.positions.get(this.currentPosition);
    }

    private void createAndSetSwingContent(final SwingNode swingNode) {
        SwingUtilities.invokeLater(() -> {
            area = new JTextArea();
            JScrollPane scrollPane;
            area.setText(new FileParser().getText(filePath));

            area.setEditable(false);
            scrollPane = new JScrollPane(area);
            swingNode.setContent(scrollPane);
        });
    }
}
