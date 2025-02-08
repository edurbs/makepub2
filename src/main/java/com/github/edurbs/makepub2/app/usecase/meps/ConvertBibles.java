package com.github.edurbs.makepub2.app.usecase.meps;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.edurbs.makepub2.infra.infra.entity.Bible;
import com.github.edurbs.makepub2.infra.repository.BibleRepository;

@Service
public class ConvertBibles {
    
    private BibleRepository bibleRepository;    

    public ConvertBibles(BibleRepository bibleRepository) {
        this.bibleRepository = bibleRepository;
    }

    @Transactional
    public void execute() {
        List<String> bibles = new ArrayList<>();
        bibles.add("nwtp");
        bibles.add("sbi1");
        bibles.add("nwt");

        for (String bible : bibles) {
            List<String[]> bibleConverted = new TextConverter().execute("txt/"+bible+".txt");
            save(bibleConverted, bible);
        }
    }

    private void save(List<String[]> bibleConverted, String version) {
        for (String[] string : bibleConverted) {
            Bible bible = new Bible();
            bible.setVersion(version);
            bible.setBook(string[0]);
            bible.setChapter(string[1]);
            bible.setVerse(string[2]);
            bible.setText(string[3]); 
            bibleRepository.save(bible);           
        }
    }

}
