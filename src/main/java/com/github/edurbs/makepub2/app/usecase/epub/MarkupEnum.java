package com.github.edurbs.makepub2.app.usecase.epub;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MarkupEnum {

    PERGUNTA_RECAPITULACAO("-", """
            <div class="boxContent dc-ttClassStyle--unset"><ul><li><p>""",
            "</p></li></ul></div>",
            true, false,   false, false),
    NUMERO_ESTUDO("!", """
            <div id="tt2" class="dc-bleedToArticleEdge dc-paddingToContentEdge du-bgColor--orange-700 du-padding-vertical--2"> <p class="contextTtl du-color--white"><strong>""", "</strong></p> </div><div style=\"border: double; color: #ac4700; font-weight: bolder; font-size: 1.5em; font-family: Arial, sans-serif; margin-top: 20px; text-align: center; margin-left: 25%; margin-right: 25%;\"> TRADUÇÃO LOCAL<br/>  NÃO OFICIAL </div>",
            true, false, false,  false),
    TITULO_SECUNDARIO("#",
            "<p><strong>",
            "</strong></p>",
            true, false, false,  false),
    TEXTO_TEMATICO("$", """
            <div id="tt7" class="du-color--textSubdued du-margin-vertical--8"> <p id="p4" data-pid="4" class="themeScrp"><em>""",
            "</em></p></div>",
            true, false, false,  false),
    TITULO_PRINCIPAL("%", """
            <h1 class="du-color--coolGray-700 du-margin-top--8" id="p3" data-pid="3"><strong>""", "</strong></h1>",
            true, false, false,  false),
    SUBTITULO("@", """
            <h2 class="du-color--coolGray-700 du-textAlign--center">""",
            "</h2>", true, false, false, false),
    ITALICO("_", "<i>",
            "</i>", false, false, false, false),
    INICIO_OBJETIVO("{", """
            <div id="tt9" class="du-color--textSubdued du-borderStyle-inlineStart--solid du-borderWidth--2 du-borderColor--orange-700 du-margin-vertical--8 du-margin-children--0 du-padding-inlineStart--4 du-padding-vertical--1">""",
            "", true, false, false, false),
    DESCRICAO_IMAGEM("|","<figure><figcaption class=\"figcaption\"><p>",
            "</p></figcaption> </figure>", true, false, false, false),
    FIM_OBJETIVO("}","</div>",
            "", true, false, false, false),
    NEGRITO("~","<b>",
            "</b>", false, false, false, false),
    SIMBOLO_NOTA_RODAPE("*", """
            <b><sup><a epub:type="noteref" href="#{idFootNote}">*</a></sup></b>""",
            "",
            false, true, false, false),
    TEXTO_NOTA_RODAPE("£", """
            <aside id="{idFootNote}" epub:type="footnote"><p>*""",
            "</p></aside>", true, false, true, false),
    PERGUNTA("=", "<p class=\"qu\">",
            "</p>", true, false, false, true),
    PARAGRAFO("§", "<p>",
            "</p>", true, false, false, false),
    LINHA_DIVISORIA("¬", "<hr></hr>",
            "", true, false, false, false),
    INICIO_RECAPITULACAO("¹", """
            <div class="blockTeach dc-ttClassStyle--unset"><aside><div id="p29" data-pid="29" class="boxTtl dc-ttClassStyle--unset du-borderStyle-top--solid du-borderColor--orange-700 du-borderWidth--2 du-margin-top--6 du-margin-children-vertical--6">""",
            "", true, false, false, false),
    FIM_RECAPITULACAO("²", "</div> </aside></div>",
            "", true, false, false, false),
    INICIO_QUADRO("ª", """
            <div class="boxSupplement du-bgColor--warmGray-50 du-borderStyle-top--solid du-borderWidth--10 du-borderColor--orange-700 boxContent"> <aside>""",
            "", true, false, false, false),
    FIM_QUADRO("º","</aside> </div>",
            "", true, false, false, false)
    ;

    
    private final String id;
    
    private final String htmlStart;
    
    private final String htmlEnd;
    private final boolean isParagraph;
    private final boolean isFootnoteSymbol;
    private final boolean isFootnoteText;
    private final boolean isQuestion;

    public boolean isFootnote(){
        return isFootnoteSymbol() || isFootnoteText();
    }
}

