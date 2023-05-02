package ru.liga.Homework2DemoBot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import ru.liga.Homework2DemoBot.DTO.LikedDto;
import ru.liga.Homework2DemoBot.DTO.UserDto;
import ru.liga.Homework2DemoBot.Model.User;
import ru.liga.Homework2DemoBot.config.Rest.RestTemplateConfig;


import java.net.URI;
import java.net.URISyntaxException;

@Service
public class UserService {
    @Value("${person.url}")
    private String personsUrl;
    @Value("${favorite.url}")
    private String favoriteUrl;
    @Autowired
    private RestTemplateConfig restTemplateConfig;

    /**
     * Сервис создания профиля
     *
     * @param person создаваемый Person
     * @return созданная запись
     * @throws URISyntaxException
     */
    public UserDto createPerson(User user) throws URISyntaxException {
        HttpHeaders headers = getHttpHeaders();
        URI url = new URI(personsUrl);
        UserDto objEmp = new UserDto(user);

        HttpEntity<UserDto> requestEntity = new HttpEntity<>(objEmp, headers);
        return restTemplateConfig.getRestTemplate().postForObject(url, requestEntity, UserDto.class);
    }

    /**
     * Сервис получения профиля
     *
     * @param userId Id текущего пользователя из Телеграмма
     * @return Полученный профиль
     * @throws URISyntaxException
     */
    public UserDto getPerson(Long userId) throws URISyntaxException {
        URI url = new URI(personsUrl + userId);
        return restTemplateConfig.getRestTemplate().getForObject(url, UserDto.class);
    }

    /**
     * Сервис получения профиля, который можно лайкнуть
     *
     * @param userId Id текущего пользователя из Телеграмма
     * @param page   номер страницы
     * @return Полученный профиль
     * @throws URISyntaxException
     */
    public UserDto getSuitablePerson(Long userId, int page) throws URISyntaxException {
        URI url = new URI(personsUrl + userId + "/suitable/" + page);
        return restTemplateConfig.getRestTemplate().getForObject(url, UserDto.class);
    }

    /**
     * Сервис получения любимца
     *
     * @param userId Id текущего пользователя из Телеграмма
     * @param page   номер страницы
     * @return Полученный профиль любимца
     * @throws URISyntaxException
     */
    public UserDto getFavoritePerson(Long userId, int page) throws URISyntaxException {
        URI url = new URI(personsUrl + userId + "/favorite/" + page);
        return restTemplateConfig.getRestTemplate().getForObject(url, UserDto.class);
    }

    /**
     * Сервис получения количества профилей, которые можно лайкнуть
     *
     * @param userId Id текущего пользователя из Телеграмма
     * @return Кол-во анкет
     * @throws URISyntaxException
     */
    public Integer getCountSuitablePerson(Long userId) throws URISyntaxException {
        URI url = new URI(personsUrl + userId + "/suitable/count");
        return restTemplateConfig.getRestTemplate().getForObject(url, Integer.class);
    }

    /**
     * Сервис получения количества любимцев
     *
     * @param userId Id текущего пользователя из Телеграмма
     * @return Кол-во анкет
     * @throws URISyntaxException
     */
    public Integer getCountFavoritePerson(Long userId) throws URISyntaxException {
        URI url = new URI(personsUrl + userId + "/favorite/count");
        return restTemplateConfig.getRestTemplate().getForObject(url, Integer.class);
    }

    /**
     * Сервис проставления лайка
     *
     * @param userId Id текущего пользователя из Телеграмма
     * @param likedPersonId Id профиля, которому ставим лайка
     * @throws URISyntaxException
     */
    public void likePerson(Long userId, Long likedPersonId) throws URISyntaxException {
        HttpHeaders headers = getHttpHeaders();
        URI url = new URI(favoriteUrl);
        HttpEntity<LikedDto> requestEntity = new HttpEntity<>(new LikedDto(userId, likedPersonId), headers);
        restTemplateConfig.getRestTemplate().postForObject(url, requestEntity, LikedDto.class);
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
