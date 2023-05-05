package ru.liga.Homework2DemoBot.Model;

public enum BotActions {

    //TODO привести к нормальному виду
    MALE("Male"),
    FEMALE("Female"),
    ALL("all"),
    LEFT("left"),
    RIGHT("right"),
    SEARCH("search"),
    PROFILE("profile"),
    FAVORITES("favorites"),
    EDIT("edit"),
    MENU("Меню");

    private String caption;

    BotActions(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }
}
