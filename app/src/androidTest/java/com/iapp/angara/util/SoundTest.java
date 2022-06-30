package com.iappe.angara.util;

public class SoundTest extends Sound {

    @Override
    public void replay() { }

    @Override
    public void play() {}

    @Override
    public void stop() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void playConst() {}

    @Override
    public void playNext(Sound nextSound) {}

    @Override
    public void playNextConst(Sound nextSound) {}

    @Override
    public void removeNextSound() {}

    @Override
    public boolean isPlaying() {
        return false;
    }
}
