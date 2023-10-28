package com.iapp.angara.main;

import android.content.Intent;
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
import com.iapp.angara.util.Constants;

public class ListActivity extends AppCompatActivity {

    private static int scrollY;
    private ScrollView listScroll;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_list);

        RelativeLayout listLayout = findViewById(R.id.list_of_accounts);
        listScroll = findViewById(R.id.list_scroll);

        listScroll.post(() -> listScroll.scrollTo(0, scrollY));
    }

    public void goToMenu(View view) {
        Constants.soundPlayer.getClick().play();
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    public void goToPrison(View view) {
        Constants.soundPlayer.getClick().play();
        Constants.attractionType = AttractionType.PRISON;
        goToAttraction();
    }

    public void goToFourthManor(View view) {
        Constants.soundPlayer.getClick().play();
        Constants.attractionType = AttractionType.FOURTH_MANOR;
        goToAttraction();
    }

    public void goToChurchArchangel(View view) {
        Constants.soundPlayer.getClick().play();
        Constants.attractionType = AttractionType.CHURCH_ARCHANGEL;
        goToAttraction();
    }

    public void goToBlacksmith(View view) {
        Constants.soundPlayer.getClick().play();
        Constants.attractionType = AttractionType.BLACKSMITH;
        goToAttraction();
    }

    private void goToAttraction() {
        scrollY = listScroll.getScrollY();
        Intent intent = new Intent(this, AttractionActivity.class);
        startActivity(intent);
    }
}
