package ru.liga.Homework2DemoBot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
public class SendMessages {
    @Autowired
    private ButtonsMaker buttonsMaker;

    public SendMessage getSendMessageQuestionGender(Message message) {
        List<List<InlineKeyboardButton>> buttons = buttonsMaker.createButtonsForAskGender();
        return SendMessage.builder()
                .chatId(message.getChatId().toString())
                .text("Вы сударь иль сударыня?")
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build();
    }


    public SendMessage getSendMessageQuestionTypeSearch(Message message) {
        List<List<InlineKeyboardButton>> buttons = buttonsMaker.createButtonsForGenderSearch();
        return SendMessage.builder()
                .chatId(message.getChatId().toString())
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .text("\n Кого ищем?").build();
    }


    public SendMessage getSendSuccessSetGender(String chatId, String gender) {
        return SendMessage.builder().chatId(chatId).text(" " + gender + "введите описание").build();
    }

    public SendMessage getSendMessage(String chatId, String text) {
        return SendMessage.builder().chatId(chatId).text(text).build();
    }
}