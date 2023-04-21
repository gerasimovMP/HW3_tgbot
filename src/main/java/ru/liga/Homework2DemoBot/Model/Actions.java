package ru.liga.Homework2DemoBot.Model;

public enum Actions {
    LEFT("Влево"),
    RIGHT("Вправо"),
    MENU("Меню");

    private String caption;

    Actions(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }
}
