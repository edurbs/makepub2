package com.github.edurbs.makepub2.app.usecase.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

@Getter
@RequiredArgsConstructor
public enum EpubMap {

    MIME("mimetype", "application/epub+zip"),
    CALIBRE("META-INF/calibre_bookmarks.txt", """
            encoding=json+base64:
            W3sicG9zIjogImVwdWJjZmkoLzQvMi80LzExOC8yOjApIiwgInBvc190eXBlIjogImVwdWJjZmkiLCAi
            dGltZXN0YW1wIjogIjIwMjQtMDUtMThUMjM6NTc6MDIuNDMyNjE3KzAwOjAwIiwgInR5cGUiOiAibGFz
            dC1yZWFkIn1d"""),
    CONTAINER("META-INF/container.xml", """
            <?xml version="1.0" encoding="UTF-8"?>
            <container version="1.0" xmlns="urn:oasis:names:tc:opendocument:xmlns:container">
                <rootfiles>
                    <rootfile full-path="OEBPS/content.opf" media-type="application/oebps-package+xml"/>
               </rootfiles>
            </container>
            """),
    COVER("OEBPS/Text/cover.xhtml", """
            <?xml version="1.0" encoding="UTF-8" standalone="no" ?>
            <!DOCTYPE html>
            <html xmlns="http://www.w3.org/1999/xhtml" xmlns:epub="http://www.idpf.org/2007/ops">
            <head>
              <title>Cover</title>
            </head>
            <body>
              <div style="height: 100vh; text-align: center; padding: 0pt; margin: 0pt;">
                <svg xmlns="http://www.w3.org/2000/svg" height="100%" preserveAspectRatio="xMidYMid meet" version="1.1" viewBox="0 0 1654 2339" width="100%" xmlns:xlink="http://www.w3.org/1999/xlink">
                  <image width="1654" height="2339" xlink:href="../Images/cover.png"/>
                </svg>
              </div>
            </body>
            </html>
            """),
    NAV("OEBPS/Text/nav.xhtml","""
            <?xml version="1.0" encoding="utf-8"?>
            <!DOCTYPE html>
            <html xmlns="http://www.w3.org/1999/xhtml" xmlns:epub="http://www.idpf.org/2007/ops" lang="en" xml:lang="en">
            <head>
              <title>ePub NAV</title>
              <meta charset="utf-8"/>
              <link href="../Styles/sgc-nav.css" rel="stylesheet" type="text/css"/>
            </head>
            <body epub:type="frontmatter">
              <nav epub:type="toc" id="toc" role="doc-toc">
                <h1>Sumário</h1>
                <ol>
                  <li>
                    <a href="Section0001.xhtml">Início</a>
                  </li>
                </ol>
              </nav>
              <nav epub:type="landmarks" id="landmarks" hidden="">
                <h1>Landmarks</h1>
                <ol>
                  <li>
                    <a epub:type="toc" href="#toc">Sumário</a>
                  </li>
                  <li>
                    <a epub:type="cover" href="cover.xhtml">Cover</a>
                  </li>
                </ol>
              </nav>
            </body>
            </html>
            """),
    TEXT("OEBPS/Text/Section0001.xhtml", """
            <?xml version="1.0" encoding="utf-8"?>
            <!DOCTYPE html>
            <html dir="ltr" class="dir-ltr ml-E ms-ROMAN" xmlns="http://www.w3.org/1999/xhtml" xmlns:epub="http://www.idpf.org/2007/ops" xml:lang="en">\s
            <head>\s
            <meta charset="utf-8" />
            <link rel="stylesheet" href="../Styles/sgc-nav.css" type="text/css" />
            <title>%s</title>
            <script>    \s
            function saveAnnotation(textAreaId){
               var annotation = document.getElementById(textAreaId).value;
               localStorage.setItem(textAreaId, annotation);
            }
            window.onload = function(){
               var textAreas = document.getElementsByTagName('textarea');
               for (var i = 0; i &lt; textAreas.length; i++){
                  var textAreaId = textAreas[i].id;
                  var savedAnnotation = localStorage.getItem(textAreaId);
                  if (savedAnnotation) {
                     document.getElementById(textAreaId).value = savedAnnotation;
                  }
                  document.getElementById(textAreaId).addEventListener('input', function() {
                     saveAnnotation(this.id);        \s
                  });
               }
            };
            </script>
            </head>
            <body dir="ltr" xml:lang="en" class="jwac dir-ltr ml-E ms-ROMAN docClass-40 pub-w docId-2024404">"""),
    IMAGE("OEBPS/Images/cover.png", ""),
    STYLE("OEBPS/Styles/sgc-nav.css", loadCss()),
    CONTENT("OEBPS/content.opf", """
            <?xml version="1.0" encoding="utf-8"?>
            <package version="3.0" unique-identifier="BookId" xmlns="http://www.idpf.org/2007/opf">
              <metadata xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:opf="http://www.idpf.org/2007/opf">
                <dc:language>xav</dc:language>
                <dc:title>%s: %s</dc:title>
                <meta property="dcterms:modified">2024-07-21T15:57:48Z</meta>
                <meta name="cover" content="capa_6.png"/>
              </metadata>
              <manifest>
                <item id="Section0001.xhtml" href="Text/Section0001.xhtml" media-type="application/xhtml+xml"/>
                <item id="Section0003.xhtml" href="Text/Section0003.xhtml" media-type="application/xhtml+xml"/>
                <item id="sgc-nav.css" href="Styles/sgc-nav.css" media-type="text/css"/>
                <item id="nav.xhtml" href="Text/nav.xhtml" media-type="application/xhtml+xml" properties="nav"/>
                <item id="cover.png" href="Images/cover.png" media-type="image/png" properties="cover-image"/>
                <item id="cover.xhtml" href="Text/cover.xhtml" media-type="application/xhtml+xml" properties="svg"/>
                <item id="NotoSans-Regular.ttf" href="Fonts/NotoSans-Regular.ttf" media-type="font/ttf"/>
              </manifest>
              <spine>
                <itemref idref="cover.xhtml"/>
                <itemref idref="Section0001.xhtml"/>
                <itemref idref="Section0002.xhtml"/>
                <itemref idref="Section0003.xhtml"/>
                <itemref idref="nav.xhtml" linear="no"/>
              </spine>
            </package>
            """);

    
    private static String loadCss() {
        try (InputStream inputStream = EpubMap.class.getClassLoader().getResourceAsStream("epub/epubs.css")) {
            if (inputStream == null) {
                throw new IllegalArgumentException("CSS not found");
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private final String path;
    private final String defaultText;

}
