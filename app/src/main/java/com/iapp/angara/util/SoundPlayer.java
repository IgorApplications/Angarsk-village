package com.iapp.angara.util;

import android.content.Context;

import com.iapp.angara.R;

public class SoundPlayer {

    private Sound main;
    private Sound click;

    private Sound ruPrison;
    private Sound ruFourthManor;
    private Sound ruChurchArchangel;
    private Sound ruBlacksmith;

    private Sound enPrison;
    private Sound enFourthManor;
    private Sound enChurchArchangel;
    private Sound enBlacksmith;

    protected SoundPlayer() {}

    public SoundPlayer(Context context) {
        click = new Sound(context, R.raw.click);
        main = new Sound(context, R.raw.main_music);

        ruPrison = new Sound(context, R.raw.ru_prison);
        ruFourthManor = new Sound(context, R.raw.ru_fourth_manor);
        ruChurchArchangel = new Sound(context, R.raw.ru_church_archangel);
        ruBlacksmith = new Sound(context, R.raw.ru_blacksmith);

        enPrison = new Sound(context, R.raw.en_prison);
        enFourthManor = new Sound(context, R.raw.en_fourth_manor);
        enChurchArchangel = new Sound(context, R.raw.en_church_archangel);
        enBlacksmith = new Sound(context, R.raw.en_blacksmith);
    }

    public Sound getMain() {
        return main;
    }

    public Sound getClick() {
        return click;
    }

    public Sound getRuPrison() {
        return ruPrison;
    }

    public Sound getRuFourthManor() {
        return ruFourthManor;
    }

    public Sound getRuChurchArchangel() {
        return ruChurchArchangel;
    }

    public Sound getRuBlacksmith() {
        return ruBlacksmith;
    }

    public Sound getEnPrison() {
        return enPrison;
    }

    public Sound getEnFourthManor() {
        return enFourthManor;
    }

    public Sound getEnChurchArchangel() {
        return enChurchArchangel;
    }

    public Sound getEnBlacksmith() {
        return enBlacksmith;
    }
}
