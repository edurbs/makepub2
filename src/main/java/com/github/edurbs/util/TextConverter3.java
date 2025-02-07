package com.github.edurbs.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class TextConverter3 {

    public static void main(String[] args) {
        // text file is on resources folder
        // there are 2 fields delimited with |
        // I need to read the file and extract the first 10 lines to two lists of strings

        List<String> address = new ArrayList<>();
        List<String> scripture = new ArrayList<>();

        // read the files from resources
        String[] lines = readFile("txt/sbi1_XV2.txt").split("\n");

        String field1 = "";
        StringBuilder field2 = new StringBuilder();
        boolean field2Complete = true;
        // get the first field from the beginning of the line until the next | delimiter
        for (String line : lines) {
            line = line.trim();
            if(line.contains(":Title")) {
                continue;
            }
            int delimiters = countDelimiters(line, '|');
            if(delimiters == 2 && line.endsWith("|")){
                // line has 2 fields
                // field1 from start until the | delimiter
                // field2 from the | delimiter until the end of the line
                field1 = line.substring(0, line.indexOf("|"));
                field2.append(line.substring(line.indexOf("|")+1, line.length()-1));
                field2Complete = true;
            }else if(delimiters == 1 && !line.endsWith("|")){
                // line has 2 fields, but the 2th is incomplete
                // field1 from start until the | delimiter
                // field2 from the | delimiter until the end of the line
                field1 = line.substring(0, line.indexOf("|"));
                field2.append(line.substring(line.indexOf("|")+1));
                field2Complete = false;
            }else if(delimiters == 1 && line.endsWith("|")){
                // line has only the 2th field and it is complete
                field2.append(" ");
                field2.append(line.substring(0,line.length()-1));
                field2Complete = true;
            }else if(delimiters==0){
                // lines in a part of the 2th field
                field2.append(" ");
                field2.append(line);
            } 
            
            if (field2Complete && !field1.isBlank() && !field2.toString().isBlank()) {
                address.add(field1);
                scripture.add(field2.toString());
                field1 = "";
                field2 = new StringBuilder();
            }
        }        
        
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
