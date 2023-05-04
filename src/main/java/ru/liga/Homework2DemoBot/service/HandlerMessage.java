package ru.liga.Homework2DemoBot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liga.Homework2DemoBot.Cache.UserCache;
import ru.liga.Homework2DemoBot.DTO.UserDto;
import ru.liga.Homework2DemoBot.Model.BotActions;
import ru.liga.Homework2DemoBot.Model.BotState;

import java.io.IOException;
import java.net.URISyntaxException;

@Slf4j
@Component
public class HandlerMessage {

    @Autowired
    private UserCache userCache;
    @Autowired
    private SendMessages sendMessages;
    @Autowired
    private UserService userService;
    @Autowired
    private GetProfileService profileService;

    public SendMessage handleSendMessage(Update update) throws  URISyntaxException {
        String messageText = update.getMessage().getText();
        Message message = update.getMessage();
        Long userId = message.getFrom().getId();

        log.info("New message from User:{}. userId: {}, chatId: {}, with text: {}",
                message.getFrom().getUserName(),
                userId,
                message.getChatId().toString(),
                message.getText());

        if ("/start".equals(messageText)) {
            UserDto user = userService.getPerson(userId);
            if (!userCache.containsKey(userId) && user == null) {
                return getSendMessageQuestionGender(message);
            }
        }
        BotState botState = userCache.getUsersCurrentBotState(userId);
        if (botState.equals(BotState.SET_PROFILE_INFO)) {
            return setProfileInfo(messageText, message, userId);
        }
        return sendMessages.getSendMessage(message.getChatId().toString(),"Ошибка");
    }

    public SendPhoto handleSendPhoto(Update update) throws IOException, URISyntaxException {
        String messageText = update.getMessage().getText();
        Message message = update.getMessage();
        Long userId = message.getFrom().getId();
        BotState botState = userCache.getUsersCurrentBotState(userId);
        if (botState.equals(BotState.DEFAULT)) {
            UserDto user = userService.getPerson(userId);
            if (user != null) {
                userCache.setUserCache(userId, user);
                return profileService.getMyProfile(message, user.getName() + " " + user.getDescription());
            }
        }

        if (botState.equals(BotState.SEARCH)) {
            if (messageText.equals(BotActions.RIGHT.getCaption())) {
                userService.likePerson(userId, userCache.getLikedPersonId(userId));
                return getNextLikedProfile(message, userId);
            }
            if (messageText.equals(BotActions.LEFT.getCaption())) {
                return getNextLikedProfile(message, userId);
            }
            if (messageText.equals(BotActions.MENU.getCaption())) {
                return getMenuAndProfileWithDescr(message, userId);
            }
        }
        if (botState.equals(BotState.FAVORITES)) {
            if (messageText.equals(BotActions.RIGHT.getCaption())) {
                return getNextFavoriteProfile(message, userId);
            }
            if (messageText.equals(BotActions.MENU.getCaption())) {
                return getMenuAndProfileWithDescr(message, userId);
            }
            if (messageText.equals(BotActions.LEFT.getCaption())) {
                return getPrevFavoriteProfile(message, userId);
            }
        }

        return null;
    }

    private SendMessage setProfileInfo(String messageText, Message message, Long userId)  {
        String[] inputDescrTypeFirst = messageText.split("\n");
        String[] inputDescrTypeSecond = messageText.split(" ");
        if (inputDescrTypeFirst.length > 1) {
            return getSendMessageQuestionTypeSearch(messageText, message, userId, "\n");
        }
        if (inputDescrTypeSecond.length > 1) {
            return getSendMessageQuestionTypeSearch(messageText, message, userId, " ");
        } else {
            return sendMessages.getSendMessage(message.getChatId().toString(),
                    "Введите имя и описание");
        }
    }

    private SendPhoto getMenuAndProfileWithDescr(Message message, Long userId) throws IOException, URISyntaxException {
        userCache.setNewState(userId, BotState.PROFILE_DONE);
        userCache.resetPagesCounter(userId);
        return profileService.getMyProfile(message, userCache.getNameAndDescription(userId));
    }

    private SendPhoto getNextLikedProfile(Message message, Long userId) throws URISyntaxException, IOException {
        int pagesCounter = userCache.incrementPagesCounter(userId);
        UserDto personDTO = userService.getSuitablePerson(userId, pagesCounter);
        userCache.setLikedPersonId(userId, personDTO.getUserId());
        return profileService.getProfile(message, personDTO);
    }

    private SendPhoto getNextFavoriteProfile(Message message, Long userId) throws URISyntaxException, IOException {
        int pagesCounter = userCache.incrementPagesCounter(userId);
        return profileService.getProfile(message, userService.getFavoritePerson(userId, pagesCounter));
    }

    private SendPhoto getPrevFavoriteProfile(Message message, Long userId) throws URISyntaxException, IOException {
        int pagesCounter = userCache.minusPagesCounter(userId);
        return profileService.getProfile(message, userService.getFavoritePerson(userId, pagesCounter));
    }

    private SendMessage getSendMessageQuestionTypeSearch(String messageText, Message message, Long userId, String reg)  {
        userCache.setNameAndDescription(messageText, userId, reg);
        userCache.setNewState(userId, BotState.SET_TYPE_SEARCH);
        return sendMessages.getSendMessageQuestionTypeSearch(message);
    }

    private SendMessage getSendMessageQuestionGender(Message message) {
        userCache.addPersonCache(message.getChat().getId(), BotState.SET_GENDER);
        return sendMessages.getSendMessageQuestionGender(message);
    }
}