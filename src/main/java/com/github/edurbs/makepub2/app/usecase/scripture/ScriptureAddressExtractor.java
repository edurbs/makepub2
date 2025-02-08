package com.github.edurbs.makepub2.app.usecase.scripture;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.regex.Matcher;

@RequiredArgsConstructor
@Getter
public class ScriptureAddressExtractor {

    private final Matcher matcher;
    @NonNull
    private String lastBookName;

    private String allVerses = "";
    private String addressPrefix = "";
    private int chapter = 0;
    private String bookName = "";

    public void execute() {
        final String fullScriptureAddress;
        final String scriptureAddress;
        bookName = matcher.group(2).trim();
        if(!bookName.isBlank()){
            lastBookName = bookName;
            fullScriptureAddress = matcher.group(0);
            scriptureAddress = fullScriptureAddress.replace(bookName, "");
        }else{
            bookName = lastBookName;
            fullScriptureAddress = matcher.group(0);
            scriptureAddress = fullScriptureAddress;
        }
        chapter = Integer.parseInt(scriptureAddress.split(":")[0].trim());
        addressPrefix = bookName + " " + chapter + ":";
        if(bookName.isBlank()){
            addressPrefix = " " + chapter + ":";
        }
        allVerses = scriptureAddress.split(":")[1];
    }
}
