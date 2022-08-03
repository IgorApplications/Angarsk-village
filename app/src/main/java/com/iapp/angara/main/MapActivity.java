package com.iapp.angara.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.iapp.angara.R;
import com.iapp.angara.attractions.AttractionActivity;
import com.iapp.angara.attractions.AttractionType;
import com.iapp.angara.ui.PlaceMark;
import com.iapp.angara.util.Settings;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapActivity extends AppCompatActivity {

    private static final String API_KEY = "bd0ec51e-6699-4cd3-a44a-f9c55d655419";

    private static final Point CAMERA = new Point(56.221843187195084, 101.68046218289052);
    private static final Point ANGARSK_VILLAGE = new Point(56.22199420657072, 101.68014413061304);
    private static final Point FOURTH_MANOR = new Point(56.221537822079355, 101.68039110151751);
    private static final Point CHURCH_ARCHANGEL = new Point(56.22193909179892, 101.68281103219306);
    private static final Point PRISON = new Point(56.22255093109379, 101.68203973527301);
    private static final Point BLACKSMITH = new Point(56.22113927592336, 101.6798639297276);

    private static boolean initialised;
    private MapView mapView;
    private final List<PlaceMark> placeMarkList = new ArrayList<>();

    public void goToMenu(View view) {
        Settings.soundPlayer.getClick().play();
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (!initialised) {
            MapKitFactory.setApiKey(API_KEY);
            MapKitFactory.initialize(this);
            initialised = true;
        }

        setContentView(R.layout.activity_map);
        mapView = findViewById(R.id.map_view);
        mapView.getMap().move(
                new CameraPosition(CAMERA, 17.1f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);

        initGraphic();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
        MapKitFactory.getInstance().onStart();
    }

    private void initGraphic() {
        PlaceMark angarskVillage = new PlaceMark(this, ANGARSK_VILLAGE, getString(R.string.app_name));
        angarskVillage.addOnClickListener(() -> Settings.soundPlayer.getClick().play());
        angarskVillage.add(mapView);

        PlaceMark fourthManor = new PlaceMark(this, FOURTH_MANOR, getString(R.string.fourth_manor));
        fourthManor.addOnClickListener(() -> {
            Settings.soundPlayer.getClick().play();
            Settings.attractionType = AttractionType.FOURTH_MANOR;
            goToAttraction();
        });
        fourthManor.add(mapView);

        PlaceMark churchArchangel = new PlaceMark(this, CHURCH_ARCHANGEL, getString(R.string.church_archangel));
        churchArchangel.addOnClickListener(() -> {
            Settings.soundPlayer.getClick().play();
            Settings.attractionType = AttractionType.CHURCH_ARCHANGEL;
            goToAttraction();
        });
        churchArchangel.add(mapView);

        PlaceMark prison = new PlaceMark(this, PRISON, getString(R.string.prison));
        prison.addOnClickListener(() -> {
            Settings.soundPlayer.getClick().play();
            Settings.attractionType = AttractionType.PRISON;
            goToAttraction();
        });
        prison.add(mapView);

        PlaceMark blacksmith = new PlaceMark(this, BLACKSMITH, getString(R.string.blacksmith));
        blacksmith.addOnClickListener(() -> {
            Settings.soundPlayer.getClick().play();
            Settings.attractionType = AttractionType.BLACKSMITH;
            goToAttraction();
        });
        blacksmith.add(mapView);

        placeMarkList.addAll(Arrays.asList(angarskVillage, fourthManor, churchArchangel, prison, blacksmith));
    }

    private void goToAttraction() {
        Intent intent = new Intent(this, AttractionActivity.class);
        startActivity(intent);
    }
}