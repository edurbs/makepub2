package com.github.edurbs.makepub2.app.usecase.types;

import com.github.edurbs.makepub2.app.domain.ScriptureAddress;

public interface BibleReader {
     String getScripture(String book, int chapter, int verse);
     String getScripture(String book, int chapter, int startVerse, int endVerse);
     String getScripture(ScriptureAddress scriptureAddress);
}
