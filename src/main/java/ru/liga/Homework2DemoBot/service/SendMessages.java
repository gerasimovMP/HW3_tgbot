package ru.liga.Homework2DemoBot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.liga.Homework2DemoBot.Cache.UserCache;

import java.util.List;

@Component
public class SendMessages {
    @Autowired
    private ButtonsMaker buttonsMaker;

    public SendMessage getSendMessageQuestionGender(Message message) {
        List<List<InlineKeyboardButton>> buttons = buttonsMaker.createButtonsForQuestionSex();
        return SendMessage.builder()
                .chatId(message.getChatId().toString())
                .text("Вы сударь иль сударыня?")
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build();
    }


    public SendMessage getSendMessageQuestionTypeSearch(Message message) {
        List<List<InlineKeyboardButton>> buttons = buttonsMaker.createButtonsForQuestionTypeSearch();
        return SendMessage.builder()
                .chatId(message.getChatId().toString())
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .text("\n Выберете теперь кого ищем!").build();
    }


    public SendMessage getSendSuccessSetGender(String chatId, String gender) {
        return SendMessage.builder().chatId(chatId).text("Поздравляю " + gender + ", теперь введите вашу инфу и описание").build();
    }

    public SendMessage getSendMessage(String chatId, String text) {
        return SendMessage.builder().chatId(chatId).text(text).build();
    }
}