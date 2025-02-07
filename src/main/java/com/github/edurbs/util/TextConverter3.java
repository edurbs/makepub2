package com.github.edurbs.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class TextConverter3 {

    public static void main(String[] args) {
        List<String> address = new ArrayList<>();
        List<String> scripture = new ArrayList<>();
        String[] lines = readFile("txt/sbi1_XV2.txt").split("\n");
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
        
    }

    private static boolean lineIsATitle(String line) {
        return line.contains(":Title");
    }

    private static boolean lineHasOnly2thIncompleteField(int delimiters) {
        return delimiters==0;
    }

    private static boolean lineHasOnly2thCompleteField(String line, int delimiters) {
        return delimiters == 1 && line.endsWith("|");
    }

    private static boolean lineHasTwoIncompleteFields(String line, int delimiters) {
        return delimiters == 1 && !line.endsWith("|");
    }

    private static boolean lineHasTwoCompleteFields(String line, int delimiters) {
        return delimiters == 2 && line.endsWith("|");
    }

    private static boolean lineCanBeAdded(String field1, StringBuilder field2, boolean field2Complete) {
        return field2Complete && !field1.isBlank() && !field2.toString().isBlank();
    }

    private static String readFile(String filePath) {
        ClassLoader classLoader = TextConverter3.class.getClassLoader();
        
        try (InputStream is = classLoader.getResourceAsStream(filePath)) {
            return IOUtils.toString(is, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }            
    }

    private static int countDelimiters(String line, char delimiter) {
        int count = 0;
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == delimiter) {
                count++;
            }
        }
        return count;
    }

}
