package com.iapp.angara.util;

import com.iapp.angara.attractions.AttractionType;
import com.iapp.angara.database.FirebaseController;
import com.iapp.angara.ui.DatabaseLoading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Settings {


    public static Mode modeActivity;
    public static AttractionType attractionType;
    public static SoundPlayer soundPlayer;
    public static FirebaseController firebaseController;
    public static DatabaseLoading loading;
    public static ExecutorService threadPool;

    public static boolean mainMusicOn;
    public static String textLog = "";
}
