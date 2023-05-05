package ru.liga.Homework2DemoBot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import ru.liga.Homework2DemoBot.Model.OldText;
import ru.liga.Homework2DemoBot.config.RestTemplateConfig;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProfileService {
    @Value("${translate.url}")
    private String translateUrl;
    //    @Value("${profileToPicture.url}")
//    private String profileToPictureUrl;
    @Value("${path.image}")
    private String filePath;
    @Autowired
    private RestTemplateConfig restTemplateConfig;

    /**
     * Сервис вызова перевода текста на старославянский
     */
    public OldText translateToOldSlavonic(String text) throws URISyntaxException {
        HttpHeaders headers = getHttpHeaders();
        URI url = new URI(translateUrl);
        OldText objEmp = new OldText(text);
        HttpEntity<OldText> requestEntity = new HttpEntity<>(objEmp, headers);
        return restTemplateConfig.getRestTemplate().postForObject(url, requestEntity, OldText.class);
    }

    /**
     * Сервис перевода на картинку
     */

    //TODO доделать наложение
    public ByteArrayOutputStream profileToPicture(String text) throws IOException {
        File file = new File(filePath);
        BufferedImage image = ImageIO.read(file);
        Graphics2D g2d = image.createGraphics();
        g2d.setFont(new Font("Old Standard TT", Font.BOLD, 15));


        int width = image.getWidth(); // ширина картинки
        int height = image.getHeight(); // высота картинки


        Font font = g2d.getFont();
        FontMetrics metrics = g2d.getFontMetrics(font);


        List<String> lines = new ArrayList<>();
        String[] words = text.split("\\s+");
        String currentLine = "";
        for (String word : words) {
            if (metrics.stringWidth(currentLine + " " + word) <= width - 20) {
                currentLine += " " + word;
            } else {
                lines.add(currentLine.trim());
                currentLine = word;
            }
        }
        lines.add(currentLine.trim());

        int x = 20;
        int y = 50;
        for (String line : lines) {
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Old Standard TT", Font.BOLD, 15));
            g2d.drawString(line, x, y + metrics.getAscent());
            y += metrics.getHeight();
        }

        g2d.dispose(); // освобождение ресурсов Graphics2D

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", byteArrayOutputStream);

        return byteArrayOutputStream;
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}