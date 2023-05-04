package ru.liga.Homework2DemoBot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.liga.Homework2DemoBot.DTO.LikedDto;
import ru.liga.Homework2DemoBot.DTO.UserDto;
import ru.liga.Homework2DemoBot.Model.User;
import ru.liga.Homework2DemoBot.config.RestTemplateConfig;


import java.net.URI;
import java.net.URISyntaxException;

@Service
public class UserService {
    @Value("${user.url}")
    private String userUrl;
    @Value("${liked.url}")
    private String likedUrl;
    @Autowired
    private RestTemplateConfig restTemplateConfig;

    /**
     * Сервис создания профиля
     */
    public UserDto createPerson(User user) throws URISyntaxException {
        HttpHeaders headers = getHttpHeaders();
        URI url = new URI(userUrl);
        UserDto objEmp = new UserDto(user);

        HttpEntity<UserDto> requestEntity = new HttpEntity<>(objEmp, headers);
        return restTemplateConfig.getRestTemplate().postForObject(url, requestEntity, UserDto.class);
    }

    /**
     * Сервис получения профиля
     */
    public UserDto getPerson(Long userId) throws URISyntaxException {
        URI url = new URI(userUrl + userId);
        return restTemplateConfig.getRestTemplate().getForObject(url, UserDto.class);
    }

    /**
     * Сервис получения профиля, который можно лайкнуть
     */
    public UserDto getSuitablePerson(Long userId, int page) throws URISyntaxException {
        URI url = new URI(userUrl + userId + "/suitable/" + page);
        return restTemplateConfig.getRestTemplate().getForObject(url, UserDto.class);
    }

    /**
     * Сервис получения любимца
     */
    public UserDto getFavoritePerson(Long userId, int page) throws URISyntaxException {
        URI url = new URI(userUrl + userId + "/favorite/" + page);
        return restTemplateConfig.getRestTemplate().getForObject(url, UserDto.class);
    }

    /**
     * Сервис получения количества профилей, которые можно лайкнуть
     */
    public Integer getCountSuitablePerson(Long userId) throws URISyntaxException {
        URI url = new URI(userUrl + userId + "/suitable/count");
        return restTemplateConfig.getRestTemplate().getForObject(url, Integer.class);
    }

    /**
     * Сервис получения количества любимцев
     */
    public Integer getCountFavoritePerson(Long userId) throws URISyntaxException {
        URI url = new URI(userUrl + userId + "/favorite/count");
        return restTemplateConfig.getRestTemplate().getForObject(url, Integer.class);
    }

    /**
     * Сервис проставления лайка
     */
    public void likePerson(Long userId, Long likedPersonId) throws URISyntaxException {
        HttpHeaders headers = getHttpHeaders();
        URI url = new URI(likedUrl);
        HttpEntity<LikedDto> requestEntity = new HttpEntity<>(new LikedDto(userId, likedPersonId), headers);
        restTemplateConfig.getRestTemplate().postForObject(url, requestEntity, LikedDto.class);
    }

    /**
     * Получение хедера
     */
    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
