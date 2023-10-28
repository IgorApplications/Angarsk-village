package com.iapp.angara.util;

import com.iapp.angara.attractions.AttractionType;
import com.iapp.angara.database.FirebaseController;
import com.iapp.angara.ui.DatabaseLoading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Constants {


    public static Mode modeActivity;
    public static AttractionType attractionType;
    public static SoundPlayer soundPlayer;
    public static FirebaseController firebaseController;
    public static DatabaseLoading loading;
    private static ExecutorService threadPool;

    public static ExecutorService getThreadPool() {
        if (threadPool == null) threadPool = Executors.newFixedThreadPool(3);
        return threadPool;
    }

    public static boolean mainMusicOn;
}
