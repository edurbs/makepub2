package com.github.edurbs.makepub2.app.usecase.scripture;

import org.springframework.stereotype.Component;

import com.github.edurbs.makepub2.app.domain.Book;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MakeRegex {
    private static final Pattern pattern = Pattern.compile(getRegex(), Pattern.UNICODE_CASE|Pattern.CASE_INSENSITIVE);

    
    public Matcher getMatcher( String text) {
        String cleanedText = cleanText(text);
        return pattern.matcher(cleanedText);
    }

    
    private String cleanText( String text) {
        return text.replace("\u00A0", " "); // non-breaking space
    }

    
    private static String getRegex(){
        StringBuilder booksForRegex = new StringBuilder();
        for(Book book : Book.values()) {
            booksForRegex.append(book.getFullName()).append("|");
            booksForRegex.append(book.getAbbreviation1()).append("|");
        }
        return "((%s)\\s)?\\d+:\\d+(?:[-,]\\s*\\d+)*"
                .formatted(booksForRegex.toString().trim());
    }
}
