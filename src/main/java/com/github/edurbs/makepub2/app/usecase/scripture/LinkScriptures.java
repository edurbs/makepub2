package com.github.edurbs.makepub2.app.usecase.scripture;

import java.util.List;
import java.util.regex.Matcher;

import org.springframework.stereotype.Component;

import com.github.edurbs.makepub2.app.domain.ScriptureAddress;
import com.github.edurbs.makepub2.app.gateway.UUIDGenerator;
import com.github.edurbs.makepub2.app.usecase.types.BibleReader;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LinkScriptures {

    private final MakeRegex makeRegex;
    private final UUIDGenerator uuidGenerator;
    private final BibleReader scriptureEarthReader;
    private final BibleReader nwtpReader;
    private final BibleReader tnmReader;

    public String execute( String originalText) {
        StringBuilder linkedHtml = new StringBuilder();
        Matcher matcher = makeRegex.getMatcher(originalText);
        String lastBookName="";
        StringBuilder generatedScriptureContents = new StringBuilder();
        boolean first = true;
        while (matcher.find()) {
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
                scriptureAddressText = " "+scriptureAddressText;
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
        // TODO read sbi1 from DB
        // String scripture = scriptureEarthReader.getScripture(address);

        // TODO if not blank, convert the spelling
        
        if(scripture.isBlank()) {
            // TODO read nwtp from DB
            // scripture = nwtpReader.getScripture(address);
        }
        if(scripture.isBlank()) {
            // TODO read nwt from DB
            // scripture = tnmReader.getScripture(address);
        }
        return scripture;
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
        String linkedScripture = """
                 <a %s href="#%s">%s</a>""".formatted(optionsForTagA, uuid, scriptureAddress);
        matcher.appendReplacement(linkedHtml, linkedScripture);

    }
}
