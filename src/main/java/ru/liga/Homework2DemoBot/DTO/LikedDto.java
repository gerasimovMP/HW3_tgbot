package ru.liga.Homework2DemoBot.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikedDto {

    //TODO подправить ДТО под бек
    private Long mainId;
    private Long likedUserId;
}
