package ru.liga.Homework2DemoBot.Model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private long id;
    private long userId;
    private Gender gender;
    private String name;
    private String description;
    private Gender searchGender;

}
