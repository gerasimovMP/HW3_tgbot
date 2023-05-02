package ru.liga.Homework2DemoBot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import ru.liga.Homework2DemoBot.Model.OldText;
import ru.liga.Homework2DemoBot.config.Rest.RestTemplateConfig;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class ProfileService {
    @Value("${translate.url}")
    private String translateUrl;
    @Value("${profileToPicture.url}")
    private String profileToPictureUrl;
    @Value("${path.image}")
    private String filePath;
    @Autowired
    private RestTemplateConfig restTemplateConfig;

    /**
     * Сервис вызова перевода текста в старославянский
     *
     * @param text Входной обычный текст
     * @return Итоговый старославянский
     * @throws URISyntaxException
     */
    public OldText translate(String text) throws URISyntaxException {
        HttpHeaders headers = getHttpHeaders();
        URI url = new URI(translateUrl);
       OldText objEmp = new OldText(text);
        HttpEntity<OldText> requestEntity = new HttpEntity<>(objEmp, headers);
        return restTemplateConfig.getRestTemplate().postForObject(url, requestEntity, OldText.class);
    }

    /**
     * Сервис для размещения текста на картинке
     *
     * @param text входной текст
     * @throws URISyntaxException
     * @throws IOException
     */
    public void profileToPicture(String text) throws URISyntaxException, IOException {
        HttpHeaders headers = getHttpHeaders();
        URI url = new URI(profileToPictureUrl);
        TextToPictureDTO objEmp = new TextToPictureDTO(text);
        HttpEntity<TextToPictureDTO> requestEntity = new HttpEntity<>(objEmp, headers);
        byte[] responseEntity = restTemplate.getRestTemplate().postForObject(url, requestEntity, byte[].class);
        Files.write(Paths.get(filePath), responseEntity);
    }

    /**
     * Получение хедера
     *
     * @return готовые хедеры
     */
    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
