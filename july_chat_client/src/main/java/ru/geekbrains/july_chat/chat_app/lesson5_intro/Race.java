package ru.geekbrains.july_chat.chat_app.lesson5_intro;

import java.util.ArrayList;
import java.util.Arrays;

public class Race {
    private ArrayList<Stage> stages;
    private Object mon;
    public ArrayList<Stage> getStages() { return stages; }
    public Race(Stage... stages) {
        this.stages = new ArrayList<>(Arrays.asList(stages));
        this.mon = new Object();
    }

    public Object getMon() {
        return mon;
    }
}
