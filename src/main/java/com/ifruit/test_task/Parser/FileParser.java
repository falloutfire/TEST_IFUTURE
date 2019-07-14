package com.ifruit.test_task.Parser;

import com.ifruit.test_task.Entities.FilePath;

import javax.swing.tree.TreePath;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileParser {

    public List<File> find(String directoryName, String text, String extension) throws IOException {
        List<File> currentFiles = getAllFilesFromDirectory(directoryName, extension);
        List<File> resultFiles = new ArrayList<>();
        for (File file : currentFiles) {
            if (isTextFound(file, text)) {
                resultFiles.add(file);
            }
        }
        return resultFiles;
    }

    public boolean isTextFound(File file, String text) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        Pattern word = Pattern.compile(text.toLowerCase());
        return br.lines().map(String::toLowerCase).anyMatch(s -> {
            Matcher match = word.matcher(s);
            return match.find();
        });
        /*String outstring = null;
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            outstring = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            br.close();
        }*/
    }

    public List<File> getAllFilesFromDirectory(String directoryName, String extension) {
        File directory = new File(directoryName);
        List<File> resultFiles = new ArrayList<>();
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isFile() && file.getName().endsWith(extension)) {
                resultFiles.add(file);
            } else if (file.isDirectory()) {
                resultFiles.addAll(getAllFilesFromDirectory(file.getPath(), extension));
            }
        }
        return resultFiles;
    }

    public void getFindingTextPositions(FilePath file, String findingText, ArrayList<Integer> positions) throws IOException {
        String text = getText(file).toLowerCase();
        Pattern word = Pattern.compile(findingText.toLowerCase());
        Matcher match = word.matcher(text);
        while (match.find()) {
            positions.add(match.start());
        }
    }

    public String getText(FilePath path) throws IOException {
        String outString = null;
        BufferedReader br = new BufferedReader(new FileReader(path.getPath().toFile()));
        try {
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
        } finally {
            br.close();
        }
        return outString;
    }
}
