package com.github.edurbs.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class TextConverter3 {

    public List<String[]> execute(String file) {
        List<String> address = new ArrayList<>();
        List<String> scripture = new ArrayList<>();
        String[] lines = readFile(file).split("\n");
        String field1 = "";
        StringBuilder field2 = new StringBuilder();
        boolean field2Complete = true;
        for (String line : lines) {
            line = line.trim();
            if(lineIsATitle(line)) {
                continue;
            }
            int delimiters = countDelimiters(line, '|');
            if(lineHasTwoCompleteFields(line, delimiters)){                
                field2Complete = true;
                // field1 from start until the | delimiter
                field1 = line.substring(0, line.indexOf("|"));
                // field2 from the | delimiter until the end of the line
                field2.append(line.substring(line.indexOf("|")+1, line.length()-1));
            }else if(lineHasTwoIncompleteFields(line, delimiters)){                
                field2Complete = false;
                // field1 from start until the | delimiter
                field1 = line.substring(0, line.indexOf("|"));
                // field2 from the | delimiter until the end of the line
                field2.append(line.substring(line.indexOf("|")+1));
            }else if(lineHasOnly2thCompleteField(line, delimiters)){
                field2.append(" ");
                field2.append(line.substring(0,line.length()-1));
                field2Complete = true;
            }else if(lineHasOnly2thIncompleteField(delimiters)){
                field2.append(" ");
                field2.append(line);
            } 
            
            if (lineCanBeAdded(field1, field2, field2Complete)) {
                address.add(field1);
                scripture.add(field2.toString());
                field1 = "";
                field2 = new StringBuilder();
            }
        }
        List<String[]> field1Splitted = split(address);
        List<String[]> bibleConverted = new ArrayList<>();
        for (int i = 0; i < field1Splitted.size(); i++) {
            bibleConverted.add(new String[]{
                field1Splitted.get(i)[0], 
                field1Splitted.get(i)[1], 
                field1Splitted.get(i)[2], 
                scripture.get(i)
            });
        }
        return bibleConverted;
        
    }

    private List<String[]> split(List<String> addresses) {
        List<String[]> result = new ArrayList<>();

        for(String address : addresses){
            int lastSpaceIndex = address.lastIndexOf(" ");
            String bookName = address.substring(0, lastSpaceIndex).trim();
            String chapterNumber;
            String verseNumber;
            if(bookWithChapters(address)){
                int colonIndex = address.indexOf(":");                
                chapterNumber = address.substring(lastSpaceIndex, colonIndex).trim();
                verseNumber = address.substring(colonIndex+1).trim();
            }else{
                chapterNumber = "1";
                verseNumber = address.substring(lastSpaceIndex).trim();
            }
            result.add(new String[]{bookName, chapterNumber, verseNumber});
        }
        return result;
    }

    private boolean bookWithChapters(String address) {
        return address.contains(":");
    }

    private boolean lineIsATitle(String line) {
        return line.contains(":Title");
    }

    private boolean lineHasOnly2thIncompleteField(int delimiters) {
        return delimiters==0;
    }

    private boolean lineHasOnly2thCompleteField(String line, int delimiters) {
        return delimiters == 1 && line.endsWith("|");
    }

    private boolean lineHasTwoIncompleteFields(String line, int delimiters) {
        return delimiters == 1 && !line.endsWith("|");
    }

    private boolean lineHasTwoCompleteFields(String line, int delimiters) {
        return delimiters == 2 && line.endsWith("|");
    }

    private boolean lineCanBeAdded(String field1, StringBuilder field2, boolean field2Complete) {
        return field2Complete && !field1.isBlank() && !field2.toString().isBlank();
    }

    private String readFile(String filePath) {
        ClassLoader classLoader = TextConverter3.class.getClassLoader();
        
        try (InputStream is = classLoader.getResourceAsStream(filePath)) {
            return IOUtils.toString(is, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }            
    }

    private int countDelimiters(String line, char delimiter) {
        int count = 0;
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == delimiter) {
                count++;
            }
        }
        return count;
    }

}
