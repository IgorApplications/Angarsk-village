package com.iappe.angara.main;

import static androidx.test.espresso.Espresso.onView;
import static org.junit.Assert.assertNotNull;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.iapp.angara.R;
import com.iapp.angara.util.Access;
import com.iapp.angara.util.Mode;
import com.iapp.angara.util.Settings;

import org.junit.Rule;
import org.junit.Test;

public class MenuActivityTest {

    @Rule
    public ActivityScenarioRule<MenuActivity> activityRule =
            new ActivityScenarioRule<>(MenuActivity.class);

    @Test
    public void onCreate() {
        activityRule.getScenario().onActivity(activity -> {
            assertNotNull(Access.getField(activity,"sharedPreferences"));
            assertEquals(Access.getField(activity, "applicationInit"), true);
            assertNotNull(Settings.soundPlayer);
            assertNotNull(Settings.soundPlayer.getMain());
            assertEquals(Settings.mainMusicOn, Settings.soundPlayer.getMain().isPlaying());
            assertNotNull(Access.getField(activity, "menu"));
            assertNotNull(Access.getField(activity, "mainSound"));
        });
    }

    @Test
    public void goToMap() {
        activityRule.getScenario().onActivity(activity -> activity.goToMap(null));
        assertNotNull(Settings.soundPlayer);
        assertNotNull(Settings.soundPlayer.getClick());
        assertEquals(Settings.modeActivity, Mode.MAP);
    }

    @Test
    public void goToList() {
        activityRule.getScenario().onActivity(activity -> activity.goToList(null));
        assertNotNull(Settings.soundPlayer);
        assertNotNull(Settings.soundPlayer.getClick());
        assertEquals(Settings.modeActivity, Mode.LIST);
    }

    @Test
    public void goToChat() {
        activityRule.getScenario().onActivity(activity -> activity.goToChat(null));
        assertNotNull(Settings.soundPlayer);
        assertNotNull(Settings.soundPlayer.getClick());
    }

    @Test
    public void turnSound() {
        onView(withId(R.id.main_sound))
                .perform()
                .check((view, noViewFoundException) -> {
                    assertNotNull(Settings.soundPlayer);
                    assertNotNull(Settings.soundPlayer.getClick());

                    assertNotNull(Settings.soundPlayer);
                    assertNotNull(Settings.soundPlayer.getMain());

                    assertEquals(Settings.mainMusicOn, Settings.soundPlayer.getMain().isPlaying());
                });
    }

    @Test
    public void onPause() {
        activityRule.getScenario().onActivity(activity -> {
            activity.onPause();
            assertEquals(Access.invoke(activity, "getSetting",
                            new Class[]{String.class},
                            new Object[]{Access.getField(activity, "SETTING_MAIN_MUSIC_ON")}),
                    Settings.mainMusicOn);
        });
    }
}
