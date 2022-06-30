package com.iappe.angara.attractions;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.iapp.angara.attractions.AttractionActivity;
import com.iapp.angara.attractions.AttractionType;
import com.iapp.angara.util.Access;
import com.iapp.angara.util.Settings;
import com.iapp.angara.util.Sound;
import com.iapp.angara.util.SoundPlayerTest;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

public class AttractionActivityTest {

    @Rule
    public ActivityScenarioRule<AttractionActivity> activityRule =
            new ActivityScenarioRule<>(AttractionActivity.class);

    @BeforeClass
    public static void init() {
        Settings.attractionType = AttractionType.PRISON;
        Settings.soundPlayer = new SoundPlayerTest();
    }

    @Test
    public void onCreate() {
        activityRule.getScenario().onActivity(activity -> {
            assertEquals(Access.getField(activity, "initialised"), true);
            assertNotNull(Access.getField(activity, "voice"));
            assertNotNull(Access.getField(activity, "backgroundLayout"));
            assertNotNull(Access.getField(activity, "navigateLeft"));
            assertNotNull(Access.getField(activity, "navigateRight"));
            assertNotNull(Settings.attractionType);
            assertNotNull(Access.getField(activity, "backgrounds"));
            assertNotNull(Access.getField(activity, "audioDescription"));
            assertNotNull(Access.getField(activity, "title"));
            assertNotNull(Access.getField(activity, "descriptionText"));
            assertEquals(Access.getField(activity, "backgroundPosition"), Integer.valueOf(0));
            assertNotNull(Access.getField(activity, "detector"));
        });
    }

    @Test
    public void goToBack() {
        activityRule.getScenario().onActivity(activity -> {
            activity.goToBack(null);
            assertNotNull(Settings.soundPlayer);
            assertNotNull(Settings.soundPlayer.getClick());
            assertFalse(((Sound) Access.getField(activity, "audioDescription")).isPlaying());
            assertEquals(Settings.mainMusicOn, Settings.soundPlayer.getMain().isPlaying());
        });
    }

    @Test
    public void showDescription() {
        activityRule.getScenario().onActivity(activity -> {
            activity.showDescription(null);
            assertNotNull(Settings.soundPlayer);
            assertNotNull(Settings.soundPlayer.getClick());
        });
    }

    @Test
    public void closeDescription() {
        activityRule.getScenario().onActivity(activity -> {
            activity.closeDescription(null);
            assertNotNull(Settings.soundPlayer);
            assertNotNull(Settings.soundPlayer.getClick());
        });
    }

    @Test
    public void playSound() {
        activityRule.getScenario().onActivity(activity -> {
            activity.playSound(null);
            assertNotNull(Settings.soundPlayer);
            assertNotNull(Settings.soundPlayer.getClick());
            assertNotNull(Settings.soundPlayer.getMain());
            assertEquals(Settings.mainMusicOn, Settings.soundPlayer.getMain().isPlaying());
        });
    }

    @Test
    public void moveLeft() {
        moveRight();
        activityRule.getScenario().onActivity(activity -> {
            int startPosition = Access.getField(activity,"backgroundPosition");
            activity.moveLeft(null);
            assertEquals(Integer.valueOf(--startPosition), Access.getField(activity, "backgroundPosition"));
            assertNotNull(Settings.soundPlayer);
            assertNotNull(Settings.soundPlayer.getClick());
        });
    }

    @Test
    public void moveRight() {
        activityRule.getScenario().onActivity(activity -> {
            int startPosition = Access.getField(activity,"backgroundPosition");
            activity.moveRight(null);
            assertEquals(Integer.valueOf(++startPosition), Access.getField(activity, "backgroundPosition"));
            assertNotNull(Settings.soundPlayer);
            assertNotNull(Settings.soundPlayer.getClick());
        });
    }

    @Test
    public void finish() {
        activityRule.getScenario().onActivity(activity -> {
            assertEquals(Settings.mainMusicOn, Settings.soundPlayer.getMain().isPlaying());
        });
    }
}
