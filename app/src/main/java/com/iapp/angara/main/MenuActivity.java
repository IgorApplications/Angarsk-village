package com.iapp.angara.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.iapp.angara.R;
import com.iapp.angara.util.Mode;
import com.iapp.angara.util.Settings;
import com.iapp.angara.util.SoundPlayer;

public class MenuActivity extends AppCompatActivity {

    private static final String FILE_NAME = "settings";
    private static final String SETTING_MAIN_MUSIC_ON = "mainMusicOn";
    private static boolean applicationInit;

    private RelativeLayout menu;
    private ImageButton mainSound;

    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        setContentView(R.layout.activity_menu);

        initApp();
        initGraphics();
        updateBackgroundOrientation();
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
        //TODO
        Toast.makeText(this, getString(R.string.unavailable), Toast.LENGTH_LONG).show();
        if (true) return;

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
    protected void onPause() {
        super.onPause();
        saveSetting(SETTING_MAIN_MUSIC_ON, Settings.mainMusicOn);
    }

    private void initApp() {
        sharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);

        if (!applicationInit) {
            Settings.mainMusicOn = getSetting(SETTING_MAIN_MUSIC_ON);
            Settings.soundPlayer = new SoundPlayer(this);
            if (Settings.mainMusicOn) {
                Settings.soundPlayer.getMain().playConst();
            }
            applicationInit = true;
        }
    }

    private void initGraphics() {
        menu = findViewById(R.id.menu_layout);
        mainSound = findViewById(R.id.main_sound);
    }

    private void updateBackgroundOrientation() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            menu.setBackgroundResource(R.drawable.background_v);
        } else {
            menu.setBackgroundResource(R.drawable.background_h);
        }
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