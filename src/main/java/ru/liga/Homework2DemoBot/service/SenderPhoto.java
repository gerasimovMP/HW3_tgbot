package ru.liga.Homework2DemoBot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.liga.Homework2DemoBot.Cache.UserCache;
import ru.liga.Homework2DemoBot.DTO.UserDto;
import ru.liga.Homework2DemoBot.Model.BotActions;
import ru.liga.Homework2DemoBot.Model.OldText;


import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Component
public class SenderPhoto {
    @Autowired
    private ProfileService profileService;
    @Autowired
    private ButtonsMaker buttonsMaker;
    @Value("${path.image}")
    private String filePath;


    /**
     * Получение профиля с кнопками меню
     *
     * @param message  Входящее сообщение
     * @param filePath Путь до файла с картинкой
     * @return Сообщение, готовое для отправки
     * @throws IOException
     * @throws URISyntaxException
     */
    public SendPhoto getMyProfile(Message message, String filePath) throws IOException, URISyntaxException {
        List<List<InlineKeyboardButton>> buttons = buttonsMaker.createButtonsForGetMyProfile();
        InputFile inputFile = getInputFile(filePath);
        return SendPhoto.builder()
                .chatId(message.getChatId().toString())
                .photo(inputFile)
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build();
    }

    /**
     * Получение профиля
     *
     * @param message   Входящее сообщение
     * @param personDTO Профиль
     * @return Сообщение, готовое для отправки
     * @throws IOException
     * @throws URISyntaxException
     */
    public SendPhoto getProfile(Message message, UserDto userDto) throws IOException, URISyntaxException {
        ReplyKeyboardMarkup keyboardMarkup = buttonsMaker.createButtonsForGetProfile();
        InputFile inputFile = getInputFile(getProfileText(userDto));
        return SendPhoto.builder()
                .chatId(message.getChatId().toString())
                .photo(inputFile)
                .replyMarkup(keyboardMarkup)
                .build();
    }

    /**
     * Получение проифиля в виде картники
     *
     * @param text Текст, размещенный на картинке
     * @return готовый профиль в виде картинки
     * @throws URISyntaxException
     * @throws IOException
     */
    private InputFile getInputFile(String text) throws URISyntaxException, IOException {
        OldText oldText = profileService.translate(text);
        profileService.profileToPicture(oldText.getText());
        File file = new File(filePath);
        return new InputFile(file);
    }

    /**
     * Получение текста для отоборажения в профиле
     *
     * @param personDTO Профиль
     * @return готовый текст для профиля
     */
    private String getProfileText(UserDto userDto) {
        if (userDto.getStatus() != null) {
            return userDto.getName() + " - " + BotActions.valueOf(userDto.getStatus()).getCaption() + "\n" + userDto.getDescription();
        } else {
            return userDto.getName() + "\n" + userDto.getDescription();
        }
    }
}