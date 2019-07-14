package com.ifruit.test_task.Controllers;

import com.ifruit.test_task.Entities.FilePath;
import com.ifruit.test_task.Parser.FileParser;
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
import java.io.IOException;
import java.util.ArrayList;

public class CustomTab extends Tab {

    private FilePath filePath;
    private VBox vboxTextPane;
    private ArrayList<Integer> positions;
    private int currentPosition;
    //private TextArea area;
    private JTextArea area;
    private String findingText;


    public CustomTab(FilePath filePath, String findingText) {
        super.setText(filePath.toString());
        this.filePath = filePath;
        this.positions = new ArrayList<>();
        this.currentPosition = 0;
        this.findingText = findingText;
    }

    public void initialize() throws IOException {
        /*StyleClassedTextArea bigTextArea = new StyleClassedTextArea();
        bigTextArea.appendText(new FileParser().getText(filePath));*/

        new FileParser().getFindingTextPositions(filePath, findingText, positions);
        vboxTextPane = new VBox();

        final SwingNode swingNode = new SwingNode();
        createAndSetSwingContent(swingNode);

        VBox.setVgrow(swingNode, Priority.ALWAYS);
        Button btnNext = new Button("Next");
        Button btnPrev = new Button("Previous");
        btnNext.setPrefSize(80, 25);
        btnPrev.setPrefSize(80, 25);

        btnNext.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            nextPosition();
        });

        btnPrev.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            prevPosition();
        });

        //vboxTextPane.setPadding(new Insets(10));
        //vboxTextPane.setSpacing(10);

        ToolBar toolBar = new ToolBar();
        toolBar.getItems().addAll(btnNext, btnPrev);

        vboxTextPane.getChildren().addAll(toolBar, swingNode);
        this.setContent(vboxTextPane);
    }


    public int getCurrentPosition() {
        return this.positions.get(this.currentPosition);
    }

    public void highLightWord() {
        Highlighter highlighter = this.area.getHighlighter();
        Highlighter.HighlightPainter painter =
                new DefaultHighlighter.DefaultHighlightPainter(Color.pink);
        int p0 = getCurrentPosition();
        int p1 = p0 + this.findingText.length();
        try {
            highlighter.removeAllHighlights();
            highlighter.addHighlight(p0, p1, painter);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void nextPosition() {
        this.area.setCaretPosition(getNextPosition());
    }

    public void prevPosition() {
        this.area.setCaretPosition(getPrevPosition());
    }

    public int getNextPosition() {
        if (this.currentPosition == this.positions.size() - 1) {
            this.currentPosition = 0;
        } else {
            this.currentPosition++;
        }

        highLightWord();
        return this.positions.get(this.currentPosition);
    }

    public int getPrevPosition() {
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
            try {
                area = new JTextArea();
                JScrollPane scrollPane;

                area.setText(new FileParser().getText(filePath));

                area.setEditable(false);
                scrollPane = new JScrollPane(area);
                swingNode.setContent(scrollPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
