package ru.liga.Homework2DemoBot.service;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.liga.Homework2DemoBot.Model.Actions;

import java.util.ArrayList;
import java.util.List;

public class Buttons {
    public ReplyKeyboardMarkup createButtonsForGetProfile() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        row.add(Actions.LEFT.getCaption());
        row.add(Actions.MENU.getCaption());
        row.add(Actions.RIGHT.getCaption());

        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }
}
