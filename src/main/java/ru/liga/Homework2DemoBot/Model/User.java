package ru.liga.Homework2DemoBot.Model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@Builder
@ToString
public class User {
    private long id;
    private long userId;
    private Gender gender;
    private String name;
    private StringBuilder description;
    private Gender searchGender;

    private BotState botState;
    private int pageCounter;
    private int pages;
    private Long likedUserId;

}
