package com.iappe.angara.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.widget.EditText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.iapp.angara.R;
import com.iapp.angara.util.Access;
import com.iapp.angara.util.Settings;
import com.iapp.angara.util.SoundPlayerTest;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;

public class ChatActivityTest {

    @Rule
    public ActivityScenarioRule<ChatActivity> activityRule =
            new ActivityScenarioRule<>(ChatActivity.class);

    @BeforeClass
    public static void init() {
        Settings.soundPlayer = new SoundPlayerTest();
    }

    @Test
    public void onCreate() {
        activityRule.getScenario().onActivity(activity -> {
            assertNotNull(Access.getField(activity,"service"));
            assertNotNull(Access.getField(activity, "chatLayout"));
            assertNotNull(Access.getField(activity, "loadingLayout"));
            assertNotNull(Access.getField(activity, "listOfMessages"));
            assertNotNull(Access.getField(activity, "databaseReference"));
            assertNotNull(Access.getField(activity, "countMessages"));
            assertNotNull(Access.getField(activity, "user"));
        });
    }

    @Test
    public void goToMenu() {
        assertNotNull(Settings.soundPlayer);
        assertNotNull(Settings.soundPlayer.getClick());
    }

    @Test
    public void logOut() {
        activityRule.getScenario().onActivity(activity -> {
            assertNotNull(Settings.soundPlayer);
            assertNotNull(Settings.soundPlayer.getClick());
            assertNotNull(activity.getString(R.string.message_log_out));
            assertNotNull(activity.getString(R.string.yes));
            assertNotNull(activity.getString(R.string.no));
            assertNotNull(activity.getString(R.string.log_out_of_account));
        });
    }

    @Test
    public void sendMessage() {
        activityRule.getScenario().onActivity(activity -> {
            assertNotNull(activity.findViewById(R.id.messageField));
            assertNotNull(activity.getString(R.string.min_characters_message));
            assertNotNull(activity.getString(R.string.max_characters_message));
            assertNotNull(activity.getString(R.string.accept));
            assertNotNull(activity.getString(R.string.wrong_message));
        });

        StringBuilder bigMessage = new StringBuilder();
        for (int i = 0; i < 100; i++) bigMessage.append("test");
        StringBuilder normalMessage = new StringBuilder();
        for (int i = 0; i < 10; i++) normalMessage.append("test");
        checkMessageSent(bigMessage.toString(), 0);
        checkMessageSent("test", 0);
        checkMessageSent(normalMessage.toString(), 1);
    }

    private void checkMessageSent(String message, int changingNumMes) {
        activityRule.getScenario().onActivity(activity -> {
            EditText editText = activity.findViewById(R.id.messageField);
            editText.setText(message);

            long startNumMes = Access.<AtomicLong>getField(activity, "countMessages").get();
            activity.sendMessage(null);
            long afterNumMes = Access.<AtomicLong>getField(activity, "countMessages").get();
            assertEquals(startNumMes + changingNumMes, afterNumMes);
        });
    }

    @Test
    public void finish() {
        activityRule.getScenario().onActivity(activity -> {
            activity.finish();
            assertTrue(Access.<ExecutorService>getField(activity, "service").isShutdown());
        });
    }
}
