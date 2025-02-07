package com.github.edurbs.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class TextConverter2 {
public static void main(String[] args) {
       
        List<String> address = new ArrayList<>();
        List<String> scripture = new ArrayList<>();

        String[] lines = readFile("txt/nwtp.txt").split("\n");

        for (String line : lines) {
            if (line.isBlank() || !line.contains("|")) {
                continue;
            }
            String field1 = "";
            String field2 = "";
            line = line.trim();
            field1 = line.substring(0, line.indexOf("|"));
            field2 = line.substring(line.indexOf("|")+1, line.length()-1);            
            address.add(field1);
            scripture.add(field2);
        }
        
        for (int i = 0; i < address.size(); i++) {
            System.out.println(address.get(i) + " => " + scripture.get(i));
        }        
        
    }

    private static String readFile(String filePath) {
        ClassLoader classLoader = TextConverter2.class.getClassLoader();        
        try (InputStream is = classLoader.getResourceAsStream(filePath)) {
            return IOUtils.toString(is, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }            
    }


}
