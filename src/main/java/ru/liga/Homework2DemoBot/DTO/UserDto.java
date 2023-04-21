package ru.liga.Homework2DemoBot.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.liga.Homework2DemoBot.Model.Gender;
@ToString
@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    private long id;
    private long userId;
    private Gender gender;
    private String name;
    private String description;
    private Gender searchGender;
}
