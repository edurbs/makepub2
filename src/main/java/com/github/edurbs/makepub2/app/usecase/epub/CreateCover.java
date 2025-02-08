package com.github.edurbs.makepub2.app.usecase.epub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CreateCover {

    private static final Logger log = LoggerFactory.getLogger(CreateCover.class);

    // TODO adicionar carimbo de tradução local
    
    public byte[] execute( String titulo,  String subtitulo,  String periodo,  String estudo) {
        int largura = 800;  // Largura da imagem
        int altura = 1132;  // Altura da imagem
        BufferedImage imagem = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = imagem.createGraphics();

        // Ativar antialiasing para melhor qualidade de texto
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Preencher o fundo com branco
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, largura, altura);

        // Desenhar o fundo roxo para o título
        g2d.setColor(new Color(101, 45, 98));  // Cor roxa
        g2d.fillRect(0, 190, largura, 120);

        // Desenhar o título
        g2d.setColor(Color.WHITE);
        Font fonteTitulo = new Font(null, Font.BOLD, 60);
        g2d.setFont(fonteTitulo);
        desenharTextoCentralizado(g2d, titulo, largura, 275);

        // Desenhar o subtítulo quebrado em múltiplas linhas
        Font fonteSubtitulo = new Font(null, Font.BOLD, 40);
        g2d.setFont(fonteSubtitulo);
        g2d.setColor(new Color(101, 45, 98));
        List<String> linhas = quebrarTextoEmLinhas(g2d, subtitulo, largura-200);  // Limite de largura para subtítulo

        // Ajustar a altura do subtítulo baseado no número de linhas
        int alturaInicialSubtitulo = 400 - (linhas.size() - 1) * 20;  // Ajusta a altura central

        for (int i = 0; i < linhas.size(); i++) {
            desenharTextoCentralizado(g2d, linhas.get(i), largura, alturaInicialSubtitulo + (i * 40));
        }

        // Desenhar o período na parte inferior
        g2d.setColor(new Color(101, 45, 98));
        Font fontePeriodo = new Font(null, Font.PLAIN, 40);
        g2d.setFont(fontePeriodo);
        desenharTextoCentralizado(g2d, estudo, largura, 1040);
        desenharTextoCentralizado(g2d, periodo, largura, 1080);

        // Liberar os recursos gráficos
        g2d.dispose();

        // Converter a imagem para byte[]
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(imagem, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            log.error("Error", e);
        }

        return new byte[0];
    }

    private void desenharTextoCentralizado( Graphics2D g2d,  String texto, int largura, int y) {
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        int x = (largura - metrics.stringWidth(texto)) / 2;
        g2d.drawString(texto, x, y);
    }

    private List<String> quebrarTextoEmLinhas( Graphics2D g2d,  String texto, int larguraMaxima) {
        List<String> linhas = new ArrayList<>();
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());

        String[] palavras = texto.split(" ");
        StringBuilder linhaAtual = new StringBuilder();

        for (String palavra : palavras) {
            String linhaTeste = linhaAtual.isEmpty() ? palavra : linhaAtual + " " + palavra;
            int larguraLinhaTeste = metrics.stringWidth(linhaTeste);

            if (larguraLinhaTeste > larguraMaxima) {
                linhas.add(linhaAtual.toString());
                linhaAtual = new StringBuilder(palavra);
            } else {
                linhaAtual.append(!linhaTeste.isEmpty() ? " " + palavra : palavra);
            }
        }
        if (!linhaAtual.isEmpty()) {
            linhas.add(linhaAtual.toString());
        }
        return linhas;
    }

}
