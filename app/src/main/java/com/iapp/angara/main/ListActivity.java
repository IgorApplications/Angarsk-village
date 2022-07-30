package com.iapp.angara.main;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import com.iapp.angara.R;
import com.iapp.angara.attractions.AttractionActivity;
import com.iapp.angara.attractions.AttractionType;
import com.iapp.angara.util.Settings;

public class ListActivity extends AppCompatActivity {

    private static int scrollY;

    private RelativeLayout listLayout;
    private ScrollView listScroll;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_list);

        listLayout = findViewById(R.id.list_of_accounts);
        listScroll = findViewById(R.id.list_scroll);
        updateBackgroundOrientation();

        listScroll.post(() -> listScroll.scrollTo(0, scrollY));
    }

    public void goToMenu(View view) {
        Settings.soundPlayer.getClick().play();
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    public void goToPrison(View view) {
        Settings.soundPlayer.getClick().play();
        Settings.attractionType = AttractionType.PRISON;
        goToAttraction();
    }

    public void goToFourthManor(View view) {
        Settings.soundPlayer.getClick().play();
        Settings.attractionType = AttractionType.FOURTH_MANOR;
        goToAttraction();
    }

    public void goToChurchArchangel(View view) {
        Settings.soundPlayer.getClick().play();
        Settings.attractionType = AttractionType.CHURCH_ARCHANGEL;
        goToAttraction();
    }

    public void goToBlacksmith(View view) {
        Settings.soundPlayer.getClick().play();
        Settings.attractionType = AttractionType.BLACKSMITH;
        goToAttraction();
    }

    private void goToAttraction() {
        scrollY = listScroll.getScrollY();
        Intent intent = new Intent(this, AttractionActivity.class);
        startActivity(intent);
    }

    private void updateBackgroundOrientation() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            listLayout.setBackgroundResource(R.drawable.background_v);
        } else {
            listLayout.setBackgroundResource(R.drawable.background_h);
        }
    }
}
