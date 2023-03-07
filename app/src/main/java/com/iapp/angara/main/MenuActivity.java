package com.iapp.angara.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.iapp.angara.R;
import com.iapp.angara.util.Mode;
import com.iapp.angara.util.Settings;
import com.iapp.angara.util.SoundPlayer;

import java.util.concurrent.Executors;

public class MenuActivity extends AppCompatActivity {

    private static final String FILE_NAME = "settings";
    private static final String SETTING_MAIN_MUSIC_ON = "mainMusicOn";

    private static boolean applicationInit;

    private ImageButton mainSound;

    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        initApp();
        initGraphics();
        updateMusicButton();
    }

    public void goToMap(View view) {
        Settings.soundPlayer.getClick().play();
        Settings.modeActivity = Mode.MAP;
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    public void goToList(View view) {
        Settings.soundPlayer.getClick().play();
        Settings.modeActivity = Mode.LIST;
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }

    public void goToChat(View view) {
        Settings.soundPlayer.getClick().play();
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
    }

    public void turnSound(View view) {
        Settings.soundPlayer.getClick().play();

        if (Settings.mainMusicOn) {
            Settings.soundPlayer.getMain().pause();
            mainSound.setImageResource(R.drawable.ic_music_off);
            Settings.mainMusicOn = false;
        } else {
            Settings.soundPlayer.getMain().resume();
            mainSound.setImageResource(R.drawable.ic_music_on);
            Settings.mainMusicOn = true;
        }
    }

    @Override
    public void finish() {}

    @Override
    protected void onPause() {
        super.onPause();
        saveSetting(SETTING_MAIN_MUSIC_ON, Settings.mainMusicOn);
    }

    private void initApp() {
        sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);

        if (!applicationInit) {
            Settings.mainMusicOn = getSetting(SETTING_MAIN_MUSIC_ON);
            Settings.soundPlayer = new SoundPlayer(this);
            // init
            Settings.getThreadPool();
            if (Settings.mainMusicOn) {
                Settings.soundPlayer.getMain().playConst();
            }
            applicationInit = true;
        }
    }

    private void initGraphics() {
        RelativeLayout menu = findViewById(R.id.menu_layout);
        mainSound = findViewById(R.id.main_sound);
    }

    private void updateMusicButton() {
        if (Settings.mainMusicOn) {
            mainSound.setImageResource(R.drawable.ic_music_on);
        } else {
            mainSound.setImageResource(R.drawable.ic_music_off);
        }
    }

    private void saveSetting(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private boolean getSetting(String key) {
        return sharedPreferences.getBoolean(key, false);
    }
}