package ru.liga.Homework2DemoBot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.liga.Homework2DemoBot.Cache.UserCache;
import ru.liga.Homework2DemoBot.DTO.UserDto;
import ru.liga.Homework2DemoBot.Model.BotState;
import ru.liga.Homework2DemoBot.Model.Gender;


import java.io.IOException;
import java.net.URISyntaxException;

@Slf4j
@Component
public class HandlerCallback {
    @Autowired
    private UserCache userCache;
    @Autowired
    private UserService userService;

    @Autowired
    private PhotoSender profileService;
    @Autowired
    private SendMessages sendMessages;


    public SendMessage answerCallback(CallbackQuery callbackQuery) throws URISyntaxException {

        Message message = callbackQuery.getMessage();
        String[] param = callbackQuery.getData().split(":");
        Long userId = callbackQuery.getFrom().getId();
        BotState botState = userCache.getUsersCurrentBotState(userId);

        if (botState.equals(BotState.SET_GENDER)) {
            userCache.setNewState(userId, BotState.SET_PROFILE_INFO);
            userCache.setNewGender(userId, Gender.valueOf(param[0]));
            return sendMessages.getSendSuccessSetGender(message.getChatId().toString(), param[1]);
        }
        if (botState.equals(BotState.EDIT)) {
            userCache.setNewState(userId, BotState.SET_GENDER);
            return sendMessages.getSendMessageQuestionGender(message);
        }
        if (botState.equals(BotState.FAVORITES) && userCache.getPages(userId) == 0) {
            userCache.setNewState(userId, BotState.PROFILE_DONE);
            return sendMessages.getSendMessage(message.getChatId().toString(), "любимцев нет");
        }
        return sendMessages.getSendMessage(message.getChatId().toString(), "это не поддерживается");
    }

    public SendPhoto   handleSendPhoto(CallbackQuery callbackQuery) throws IOException, URISyntaxException {
        Message message = callbackQuery.getMessage();
        String[] param = callbackQuery.getData().split(":");
        Long userId = callbackQuery.getFrom().getId();
        BotState botState = userCache.getUsersCurrentBotState(userId);

        if (botState.equals(BotState.SET_TYPE_SEARCH)) {
            return setTypeSearch(message, param[0], userId);
        }

        if (botState.equals(BotState.PROFILE_DONE)) {
            BotState newBotState = BotState.valueOf(param[0]);
            userCache.setNewState(userId, newBotState);
            if (newBotState.equals(BotState.SEARCH)) {
                return startSearching(message, userId);
            }
            if (newBotState.equals(BotState.FAVORITES)) {
                int pagesCounter = userService.getCountFavoritePerson(userId);
                setPagesCache(userId, pagesCounter);
                if (pagesCounter > 0) {
                    UserDto userDto = userService.getFavoritePerson(userId, 1);
                    return profileService.getProfile(message, userDto);
                }
            }
        }
        return null;
    }


    private SendPhoto startSearching(Message message, Long userId) throws URISyntaxException, IOException {
        setPagesCache(userId, userService.getCountSuitablePerson(userId));
        UserDto userDto = userService.getSuitablePerson(userId, 1);
        userCache.setLikedPersonId(userId, userDto.getUserId());
        return profileService.getProfile(message, userDto);
    }

    private SendPhoto setTypeSearch(Message message, String typeSearch, Long userId) throws URISyntaxException, IOException {
        userCache.setNewState(userId, BotState.PROFILE_DONE);
        userCache.setTypeSearch(userId, Gender.valueOf(typeSearch));
        userService.createPerson(userCache.getUsersCurrentUser(userId));
        String text = userCache.getNameAndDescription(userId);
        return profileService.getMyProfile(message, text);
    }

    private void setPagesCache(Long userId, int pagesCounter) {
        userCache.setPages(userId, pagesCounter);
        userCache.resetPagesCounter(userId);
    }
}