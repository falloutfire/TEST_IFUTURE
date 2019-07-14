package com.ifuture.test_task.Parser;

import com.ifuture.test_task.Entities.FilePath;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileParser {

    boolean isTextFound(File file, String text) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        Pattern word = Pattern.compile(text.toLowerCase());
        return br.lines().map(String::toLowerCase).anyMatch(s -> {
            Matcher match = word.matcher(s);
            return match.find();
        });
    }

    public void getFindingTextPositions(FilePath file, String findingText, ArrayList<Integer> positions) {
        String text = getText(file).toLowerCase();
        Pattern word = Pattern.compile(findingText.toLowerCase());
        Matcher match = word.matcher(text);
        while (match.find()) {
            positions.add(match.start());
        }
    }

    public String getText(FilePath path) {
        String outString = null;
        try (BufferedReader br = new BufferedReader(new FileReader(path.getPath().toFile()))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            outString = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outString;
    }
}
