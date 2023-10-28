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
import com.iapp.angara.util.Constants;
import com.iapp.angara.util.SoundPlayer;

public class MenuActivity extends AppCompatActivity {

    private static final String FILE_NAME = "settings";
    private static final String SETTING_MAIN_MUSIC_ON = "mainMusicOn";

    private static boolean applicationInit;

    private ImageButton mainSound;

    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        setContentView(R.layout.activity_menu);
        System.out.println(getTheme().toString());;

        initApp();
        initGraphics();
        updateMusicButton();
    }

    public void goToMap(View view) {
        Constants.soundPlayer.getClick().play();
        Constants.modeActivity = Mode.MAP;
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    public void goToList(View view) {
        Constants.soundPlayer.getClick().play();
        Constants.modeActivity = Mode.LIST;
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }

    public void goToChat(View view) {
        Constants.soundPlayer.getClick().play();
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
    }

    public void turnSound(View view) {
        Constants.soundPlayer.getClick().play();

        if (Constants.mainMusicOn) {
            Constants.soundPlayer.getMain().pause();
            mainSound.setImageResource(R.drawable.ic_music_off);
            Constants.mainMusicOn = false;
        } else {
            Constants.soundPlayer.getMain().resume();
            mainSound.setImageResource(R.drawable.ic_music_on);
            Constants.mainMusicOn = true;
        }
    }

    @Override
    public void finish() {}

    @Override
    protected void onPause() {
        super.onPause();
        saveSetting(SETTING_MAIN_MUSIC_ON, Constants.mainMusicOn);
    }

    private void initApp() {
        sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);

        if (!applicationInit) {
            Constants.mainMusicOn = getSetting(SETTING_MAIN_MUSIC_ON);
            Constants.soundPlayer = new SoundPlayer(this);
            // init
            Constants.getThreadPool();
            if (Constants.mainMusicOn) {
                Constants.soundPlayer.getMain().playConst();
            }
            applicationInit = true;
        }
    }

    private void initGraphics() {
        RelativeLayout menu = findViewById(R.id.menu_layout);
        mainSound = findViewById(R.id.main_sound);
    }

    private void updateMusicButton() {
        if (Constants.mainMusicOn) {
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