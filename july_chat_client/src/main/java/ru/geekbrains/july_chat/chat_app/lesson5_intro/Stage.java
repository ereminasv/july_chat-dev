package ru.geekbrains.july_chat.chat_app.lesson5_intro;

public abstract class Stage {
    protected int length;
    protected String description;
    public String getDescription() {
        return description;
    }
    public abstract void go(Car c);
}

