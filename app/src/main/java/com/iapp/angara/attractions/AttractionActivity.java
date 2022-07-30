package com.iapp.angara.attractions;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import com.iapp.angara.R;
import com.iapp.angara.main.ListActivity;
import com.iapp.angara.main.MapActivity;
import com.iapp.angara.ui.NavigateImageView;
import com.iapp.angara.ui.OnSwipeListener;
import com.iapp.angara.util.Mode;
import com.iapp.angara.util.Settings;
import com.iapp.angara.util.Sound;

import java.util.Locale;

public class AttractionActivity extends AppCompatActivity {

    private static final int[] layoutId = new int[] {R.id.first_background_layout, R.id.second_background_layout};

    private static final int[] layoutPath = new int[] {R.layout.first_background, R.layout.second_background};

    private static final int[][] prisonBackgrounds = new int[][] {
            {R.drawable.prison1_v, R.drawable.prison1_h},
            {R.drawable.priosn2_v, R.drawable.priosn2_h}
    };

    private static final int[][] fourthBackgrounds = new int[][] {
            {R.drawable.manor_fourth1_v, R.drawable.manor_fourth1_h},
            {R.drawable.manor_fourth2_v, R.drawable.manor_fourth2_h},
            {R.drawable.manor_fourth3_v, R.drawable.manor_fourth3_h}
    };

    private static final int[][] churchArchangelBackgrounds = new int[][] {
            {R.drawable.church_archangel1_v, R.drawable.church_archangel1_h},
            {R.drawable.church_archangel2_v, R.drawable.church_archangel2_h},
            {R.drawable.church_archangel3_v, R.drawable.church_archangel3_h}
    };

    private static final int[][] blacksmithBackgrounds = new int[][] {
            {R.drawable.blacksmith1_v, R.drawable.blacksmith1_h},
            {R.drawable.blacksmith2_v, R.drawable.blacksmith2_h}
    };

    private boolean initialised;
    private int indexBackgroundLayout;

    private RelativeLayout backgroundLayout;
    private Button description;
    private Button voice;
    private NavigateImageView navigateLeft;
    private NavigateImageView navigateRight;

    private int[][] backgrounds;
    private int backgroundPosition;
    private String descriptionText;
    private String title;
    private Sound audioDescription;
    private static boolean descriptionDisplay;

    private GestureDetectorCompat detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_attraction);

        initGraphics();
        updateNavigationButtons();
        updateBackgroundOrientation();
    }

    public void goToBack(View view) {
        Settings.soundPlayer.getClick().play();

        audioDescription.stop();
        if (Settings.mainMusicOn) {
            Settings.soundPlayer.getMain().playConst();
        }

        if (Settings.modeActivity == Mode.LIST) startActivity(new Intent(this, ListActivity.class));
        else startActivity(new Intent(this, MapActivity.class));
    }

    public void playSound(View view) {
        Settings.soundPlayer.getClick().play();

        if (audioDescription.isPlaying()) {
            audioDescription.stop();
            if (Settings.mainMusicOn) {
                Settings.soundPlayer.getMain().playConst();
            }
        } else {
            Settings.soundPlayer.getMain().stop();
            audioDescription.play();
        }

        updateVoiceButton();
    }

    public void moveLeft(View view) {
        Settings.soundPlayer.getClick().play();
        backgroundPosition--;
        moveBackground();
    }

    public void moveRight(View view) {
        Settings.soundPlayer.getClick().play();
        backgroundPosition++;
        moveBackground();
    }

    @Override
    public void finish() {
        audioDescription.stop();
        if (Settings.mainMusicOn) {
            Settings.soundPlayer.getMain().playConst();
        }
        super.finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private void showDescriptionDialog() {
        makeTransition(R.id.blackout_root, R.layout.blackout);
        makeTransition(R.id.description_scene_root, R.layout.dialog);

        initDialogTextData();
        descriptionDisplay = true;
    }

    private void closeDescription() {
        Settings.soundPlayer.getClick().play();

        makeTransition(R.id.blackout_root, R.layout.empty);
        makeTransition(R.id.description_scene_root, R.layout.empty);
        descriptionDisplay = false;
    }

    private void moveBackground() {
        Pair<Integer, Integer> nextBackground = getNextBackgroundLayout();
        makeTransition(R.id.background_scene_root, nextBackground.second);
        backgroundLayout = findViewById(nextBackground.first);

        updateBackgroundOrientation();
        updateNavigationButtons();
    }

    private void initDialogTextData() {
        NavigateImageView close = findViewById(R.id.close);
        close.setColorDown(Color.argb(155, 0, 0, 0));
        close.setOnClickListener(v -> closeDescription());

        TextView titleView = findViewById(R.id.title);
        TextView descriptionView = findViewById(R.id.description_text);

        titleView.setText(title);
        descriptionView.setText(descriptionText);
    }

    private void initGraphics() {
        if (!initialised) {
            voice = findViewById(R.id.voice);
            backgroundLayout = findViewById(getNextBackgroundLayout().first);
            navigateLeft = findViewById(R.id.navigate_left);
            navigateRight = findViewById(R.id.navigate_right);
            description = findViewById(R.id.description);
            description.setOnClickListener(v -> {
                Settings.soundPlayer.getClick().play();
                showDescriptionDialog();
            });

            initData();
            detector = new GestureDetectorCompat(this, new AttractionOnSwipeListener());
            initialised = true;
        }
    }

    private void initData() {
        Language language = getSystemLanguage();

        switch (Settings.attractionType) {
            case PRISON:
                backgrounds = prisonBackgrounds;
                if (language == Language.RUSSIAN) audioDescription = Settings.soundPlayer.getRuPrison();
                else audioDescription = Settings.soundPlayer.getEnBlacksmith();
                title = getString(R.string.prison);
                descriptionText = getString(R.string.prison_description);
                break;

            case FOURTH_MANOR:
                backgrounds = fourthBackgrounds;
                if (language == Language.RUSSIAN) audioDescription = Settings.soundPlayer.getRuFourthManor();
                else audioDescription = Settings.soundPlayer.getEnFourthManor();
                title = getString(R.string.fourth_manor);
                descriptionText = getString(R.string.fourth_manor_description);
                break;

            case CHURCH_ARCHANGEL:
                backgrounds = churchArchangelBackgrounds;
                if (language == Language.RUSSIAN) audioDescription = Settings.soundPlayer.getRuChurchArchangel();
                else audioDescription = Settings.soundPlayer.getEnChurchArchangel();
                title = getString(R.string.church_archangel);
                descriptionText = getString(R.string.church_archangel_description);
                break;

            case BLACKSMITH:
                backgrounds = blacksmithBackgrounds;
                if (language == Language.RUSSIAN) audioDescription = Settings.soundPlayer.getRuBlacksmith();
                else audioDescription = Settings.soundPlayer.getEnBlacksmith();
                title = getString(R.string.blacksmith);
                descriptionText = getString(R.string.blacksmith_description);
                break;

            default:
                throw new IllegalArgumentException("Error when trying to determine attraction type");
        }
    }
    
    private void updateVoiceButton() {
        if (audioDescription.isPlaying()) {
            voice.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_voice_on, 0);
        } else {
            voice.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_voice_off, 0);
        }
    }

    private void updateBackgroundOrientation() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            backgroundLayout.setBackgroundResource(backgrounds[backgroundPosition][0]);
        } else {
            backgroundLayout.setBackgroundResource(backgrounds[backgroundPosition][1]);
        }

        if (descriptionDisplay) {
            showDescriptionDialog();
        }
    }

    private void updateNavigationButtons() {
        if (backgroundPosition != 0) navigateLeft.setVisibility(View.VISIBLE);
        else navigateLeft.setVisibility(View.INVISIBLE);

        if (backgroundPosition != backgrounds.length - 1) navigateRight.setVisibility(View.VISIBLE);
        else navigateRight.setVisibility(View.INVISIBLE);
    }

    private Pair<Integer, Integer> getNextBackgroundLayout() {
        Pair<Integer, Integer> nextBackground = new Pair<>(layoutId[indexBackgroundLayout], layoutPath[indexBackgroundLayout]);
        indexBackgroundLayout++;
        if (indexBackgroundLayout == 2) indexBackgroundLayout = 0;
        return nextBackground;
    }

    private void makeTransition(int rootId, int layout) {
        ViewGroup root = findViewById(rootId);
        Scene another = Scene.getSceneForLayout(root, layout, this);

        Transition fadeTransit = TransitionInflater.from(this).inflateTransition(R.transition.fade_transition);
        TransitionManager.go(another, fadeTransit);
    }

    private Language getSystemLanguage() {
        switch (Locale.getDefault().getLanguage()) {
            case "ru": return Language.RUSSIAN;
            default: return Language.ENGLISH;
        }
    }

    private class AttractionOnSwipeListener extends OnSwipeListener {

        @Override
        public boolean onSwipe(Direction direction) {
            if (direction == Direction.RIGHT) {
                if (backgroundPosition != 0) {
                    backgroundPosition--;
                    moveBackground();
                    return true;
                }
            } else if (direction == Direction.LEFT) {
                if (backgroundPosition != backgrounds.length - 1) {
                    backgroundPosition++;
                    moveBackground();
                    return true;
                }
            }
            return false;
        }
    }

    private enum Language {
        ENGLISH,
        RUSSIAN
    }
}