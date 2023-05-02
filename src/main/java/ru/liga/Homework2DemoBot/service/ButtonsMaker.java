package ru.liga.Homework2DemoBot.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.liga.Homework2DemoBot.Model.BotActions;
import ru.liga.Homework2DemoBot.Model.BotState;
import ru.liga.Homework2DemoBot.Model.Gender;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ButtonsMaker {

    public List<List<InlineKeyboardButton>> createButtonsForQuestionSex() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(Arrays.asList(
                InlineKeyboardButton.builder()
                        .text(BotActions.MALE.getCaption())
                        .callbackData(Gender.MALE + ":" + BotActions.MALE.getCaption())
                        .build(),
                InlineKeyboardButton.builder()
                        .text(BotActions.FEMALE.getCaption())
                        .callbackData(Gender.FEMALE + ":" + BotActions.FEMALE.getCaption())
                        .build()));
        return buttons;
    }

    public List<List<InlineKeyboardButton>> createButtonsForQuestionTypeSearch() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(Arrays.asList(
                InlineKeyboardButton.builder()
                        .text(BotActions.MALE.getCaption())
                        .callbackData(Gender.MALE + ":" + BotActions.MALE.getCaption())
                        .build(),
                InlineKeyboardButton.builder()
                        .text(BotActions.FEMALE.getCaption())
                        .callbackData(Gender.FEMALE + ":" + BotActions.FEMALE.getCaption())
                        .build(),
                InlineKeyboardButton.builder()
                        .text(BotActions.ALL.getCaption())
                        .callbackData(Gender.ALL + ":" + BotActions.ALL.getCaption())
                        .build()));
        return buttons;
    }

    public List<List<InlineKeyboardButton>> createButtonsForGetMyProfile() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        buttons.add(Arrays.asList(
                InlineKeyboardButton.builder()
                        .text(BotActions.SEARCH.getCaption())
                        .callbackData(BotState.SEARCH.toString())
                        .build(),
                InlineKeyboardButton.builder()
                        .text(BotActions.EDIT.getCaption())
                        .callbackData(BotState.EDIT.toString())
                        .build(),
                InlineKeyboardButton.builder()
                        .text(BotActions.FAVORITES.getCaption())
                        .callbackData(BotState.FAVORITES.toString())
                        .build()));
        return buttons;
    }

    public ReplyKeyboardMarkup createButtonsForGetProfile() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        row.add(BotActions.LEFT.getCaption());
        row.add(BotActions.MENU.getCaption());
        row.add(BotActions.RIGHT.getCaption());

        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }
}