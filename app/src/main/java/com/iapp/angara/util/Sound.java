package com.iapp.angara.util;

import android.content.Context;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Class for complex sound reproduction
 * @author IgorApplications <igorapplications@gmail.com>
 * @version 1.0
 * @see MediaPlayer
 */
public class Sound {

    /** Android audio equipment **/
    private MediaPlayer media;
    /** Flag to define a temporary pause **/
    private boolean pause;

    /**
     * The constructor is intended for complex
     * inheritance from the given class
     */
    protected Sound() {}

    /**
     * Constructor intended for user initialisation
     * of an instance of this class
     * @param context current activity
     * @param path location of sound file
     */
    public Sound(Context context, int path) {
        media = MediaPlayer.create(context, path);
    }

    /**
     * Method to play audio again
     * @throws IOException when preloading an audio file
     */
    public void replay() {
        if (isPlaying()) stop();
        play();
    }

    /**
     * Method to play sound file
     * for the first time
     */
    public void play() {
        if (pause) return;
        media.start();
    }

    /**
     * Method to stop playing a file,
     * move the playback timestamp to the beginning
     */
    public void stop() {
        if (pause) return;
        media.stop();
        prepare();
    }

    /**
     * The method stop playback,
     * saves the timestamp,
     * block all playback methods
     */
    public void pause() {
        media.pause();
        pause = true;
    }

    /**
     * The method continues playback of the sound from
     * the timestamp, removes all blocking from all playback methods
     */
    public void resume() {
        media.start();
        pause = false;
    }

    /**
     * Method for play audio file continuously.
     * When one complete sound playback is completed,
     * the file starts again
     */
    public void playConst() {
        if (pause) return;
        media.start();
        media.setOnCompletionListener(mp -> media.start());
    }

    /**
     * Method for playing a sound file,
     * placing the next sound file in the queue
     * @param nextSound next file in queue
     */
    public void playNext(Sound nextSound) {
        if (pause) return;
        media.setOnCompletionListener(mp -> {
            nextSound.play();
            media.setOnCompletionListener(null);
        });
    }

    /**
     * Method to queue the next file.
     * The next file will be played continuously.
     * @param nextSound next file in queue
     */
    public void playNextConst(Sound nextSound) {
        if (pause) return;
        media.setOnCompletionListener(mp -> {
            nextSound.playConst();
            media.setOnCompletionListener(null);
        });
    }

    /**
     * Removing the next element in the queue
     */
    public void removeNextSound() {
        media.setOnCompletionListener(null);
    }

    /**
     * Methods for determining the playbacks
     * status of an audio file
     * @return sound file playbacks status
     */
    public boolean isPlaying() {
        return media.isPlaying();
    }

    private void prepare() {
        try {
            media.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
