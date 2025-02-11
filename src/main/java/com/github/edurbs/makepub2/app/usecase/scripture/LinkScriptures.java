package com.github.edurbs.makepub2.app.usecase.scripture;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.edurbs.makepub2.app.domain.ScriptureAddress;
import com.github.edurbs.makepub2.app.gateway.UUIDGenerator;
import com.github.edurbs.makepub2.infra.infra.entity.Bible;
import com.github.edurbs.makepub2.infra.repository.BibleRepository;
import com.vaadin.flow.function.SerializableConsumer;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LinkScriptures {

    private final MakeRegex makeRegex;
    private final UUIDGenerator uuidGenerator;

    @Autowired
    private final ConvertScripture convertScripture;

    @Autowired
    private BibleRepository bibleRepository;    

    public String execute( String originalText, SerializableConsumer<String[]> progressBar) {
        StringBuilder linkedHtml = new StringBuilder();
        Matcher matcher = makeRegex.getMatcher(originalText);
        String lastBookName="";
        StringBuilder generatedScriptureContents = new StringBuilder();
        boolean first = true;
        int textSize = originalText.length();
        while (matcher.find()) {
            int currentPosition = matcher.end();
            double percent = ( (double) currentPosition / textSize  );
            String formattedPercent = String.format(Locale.US, "%.1f", percent);
            progressBar.accept(new String[]{formattedPercent, "Adicionando os textos bíblicos... "+matcher.group()});
            
            var extractor = new ScriptureAddressExtractor(matcher, lastBookName);
            extractor.execute();
            String addressPrefix = extractor.getAddressPrefix();
            String allScriptures = extractor.getAllVerses();
            String uuid = uuidGenerator.generate();
            List<ScriptureAddress> scriptureAddresses = new DetectScripture(matcher, lastBookName).execute();
            String scriptureAddressText = addressPrefix + allScriptures;
            if(first) {
                first = false;
            }else{
                scriptureAddressText = ""+scriptureAddressText;
            }
            linkScripture(scriptureAddressText, matcher, uuid, linkedHtml);
            generatedScriptureContents.append(generateScriptureContents(uuid, scriptureAddressText, scriptureAddresses));
            lastBookName = extractor.getBookName();
        }
        matcher.appendTail(linkedHtml);
        linkedHtml.append("\n<div class=\"groupExt\">\n<div class=\"groupExtScrpCite\">");
        linkedHtml.append(generatedScriptureContents);
        linkedHtml.append("\n</div>\n</div>");
        String stringLinkedHtml = linkedHtml.toString();
        stringLinkedHtml = stringLinkedHtml.replace("</body>", "");
        stringLinkedHtml = stringLinkedHtml.replace("</html>", "");
        return stringLinkedHtml + "\n</body>\n</html>";
    }

    
    private String getScriptureFromBible( ScriptureAddress address) {
        if(address.book()==null) {
            return "";
        }
        String scripture = "";
        scripture = getBible(address, "sbi1");
        scripture = convertScripture.execute(scripture);
        if(scripture.isBlank()) {
            scripture = getBible(address, "nwtp");
        }
        if(scripture.isBlank()) {
            scripture = getBible(address, "nwt");
        }
        return scripture;
    }

    private String getBible(ScriptureAddress address, String version) {
        Bible bible = null;
        String finalScriptureText = "";
        if(address.verse()<address.endVerse()) {
            StringBuilder text = new StringBuilder();
            for(int i=address.verse();i<=address.endVerse();i++) {
                bible = bibleRepository.findByVersionAndBookAndChapterAndVerse(version, address.book().getFullName(), String.valueOf(address.chapter()), String.valueOf(i));
                if(bible!=null){
                    text.append(bible.getVerse()).append(" ");
                    text.append(bible.getText()).append(" ");
                }
            }
            finalScriptureText = text.toString();
        }else{
            bible = bibleRepository.findByVersionAndBookAndChapterAndVerse(version, address.book().getFullName(), String.valueOf(address.chapter()), String.valueOf(address.verse()));
            if(bible!=null) {
                finalScriptureText = bible.getText();
            }
        }
        return finalScriptureText;
    }

    
    private String generateScriptureContents( String uuid, String scriptureAddressText,  List<ScriptureAddress> scriptureAddresses) {
        StringBuilder footnotes = new StringBuilder();
        StringBuilder contents = new StringBuilder();
        for(ScriptureAddress scriptureAddress : scriptureAddresses) {
            contents.append(getScriptureFromBible(scriptureAddress));
            contents.append(" ");
        }
        scriptureAddressText = scriptureAddressText.trim();
        footnotes.append("""
                <p class="extScrpCiteTxt">
                <strong>(%s)</strong>
                %s
                </p>""".formatted(scriptureAddressText, contents.toString().trim())
        );
        return """
                \n<aside epub:type="footnote">
                <div epub:type="footnote" class="extScrpCite" id="%s">
                %s
                </div>
                </aside>""".formatted(uuid, footnotes);
    }

    private void linkScripture( String scriptureAddress,  Matcher matcher,  final String uuid, StringBuilder linkedHtml) {
        String optionsForTagA = "epub:type=\"noteref\"";
        String linkedScripture = "<a %s href=\"#%s\">%s</a>".formatted(optionsForTagA, uuid, scriptureAddress);
        matcher.appendReplacement(linkedHtml, linkedScripture);

    }
}
