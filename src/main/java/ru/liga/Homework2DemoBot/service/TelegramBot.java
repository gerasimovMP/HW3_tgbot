package ru.liga.Homework2DemoBot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.liga.Homework2DemoBot.config.BotConfig;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;

    //static final String SWIPE_RIGHT_BUTTON = "Вправо - ЛАЙК";
    //static final String SWIPE_LEFT_BUTTON = "Влево - ОТКАЗ";

    //static final String GO_TO_MENU_BUTTON = "Вернуться в меню";

    public TelegramBot(BotConfig config) {
        this.config = config;
        List<BotCommand> listofCommands = new ArrayList<>();
        listofCommands.add(new BotCommand("/search", "Поиск"));
        listofCommands.add(new BotCommand("/mydata", "Анкета"));
        listofCommands.add(new BotCommand("/lovers", "Любимцы"));

        try {
            this.execute(new SetMyCommands(listofCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error command list" + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText(messageText);
            try {
                switch (messageText) {
                    case "/search":
                        // message.setText("Поиск!");
                        search(chatId);
                        //execute(message);
                        break;
                    case "/mydata":
                        message.setText("Анкета!");
                        execute(message);
                        break;
                    case "/lovers":
                        findLovers(chatId);
//                        message.setText("Любимицы!");
////                        execute(message);
                        break;
                    default:
                        message.setText("Command wasn't recognized");
                        execute(message);
                }
            } catch (TelegramApiException e) {
                log.info(e.getMessage());
            }
        }
    }


    private void startCommandReceived(long chatId) {


        String answer = "Hi, " + chatId;
        log.info("Replied to user " + chatId);


        sendMessage(chatId, answer);
    }


    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);


        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("sendMessage Error" + e.getMessage());
        }
    }


    private void search(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("ПОИСК");

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        var swipeRightButton = new InlineKeyboardButton();
        swipeRightButton.setText("ЛАЙК");
        swipeRightButton.setCallbackData("SWIPE_RIGHT_BUTTON");


        var swipeLeftButton = new InlineKeyboardButton();
        swipeLeftButton.setText("ОТКАЗ");
        swipeLeftButton.setCallbackData("SWIPE_LEFT_BUTTON");


        var goToMenu = new InlineKeyboardButton();
        goToMenu.setText("ВОЗВРАТ В МЕНЮ");
        goToMenu.setCallbackData("GO_TO_MENU_BUTTON");

        rowInLine.add(goToMenu);
        rowInLine.add(swipeLeftButton);
        rowInLine.add(swipeRightButton);

        rowsInLine.add(rowInLine);

        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }


    }


    private void findLovers(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("ЛЮБИМЦЫ");

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        var nextButton = new InlineKeyboardButton();
        nextButton.setText("Следующая");
        nextButton.setCallbackData("SWIPE_RIGHT_BUTTON");


        var goBackButton = new InlineKeyboardButton();
        goBackButton.setText("Предыдущая");
        goBackButton.setCallbackData("SWIPE_LEFT_BUTTON");


        var goToMenu = new InlineKeyboardButton();
        goToMenu.setText("ВОЗВРАТ В МЕНЮ");
        goToMenu.setCallbackData("GO_TO_MENU_BUTTON");

        rowInLine.add(goToMenu);
        rowInLine.add(goBackButton);
        rowInLine.add(nextButton);

        rowsInLine.add(rowInLine);

        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }


    }
}