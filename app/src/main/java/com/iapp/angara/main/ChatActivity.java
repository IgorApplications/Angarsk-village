package com.iapp.angara.main;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.github.library.bubbleview.BubbleTextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.iapp.angara.R;
import com.iapp.angara.database.Account;
import com.iapp.angara.database.FirebaseController;
import com.iapp.angara.database.Message;
import com.iapp.angara.database.Report;
import com.iapp.angara.ui.DatabaseLoading;
import com.iapp.angara.ui.ElementAdapter;
import com.iapp.angara.ui.NavigateImageView;
import com.iapp.angara.ui.OnChangeElement;
import com.iapp.angara.util.Settings;
import com.iapp.angara.util.TimeUtil;

import java.util.Arrays;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private final List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build());

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            this::onSignInResult
    );

    private static boolean descriptionDisplay;

    private RelativeLayout chatLayout;
    private RelativeLayout loadingLayout;

    private ListView listOfMessages;
    private ImageButton moderation;
    private FloatingActionButton sendButton;

    private int pinnedIndex;
    private Message pinnedMessage;
    private boolean showedPin;
    private int index, top;
    private boolean sentMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setSupportActionBar(findViewById(R.id.action_bar));
        setContentView(R.layout.activity_chat);

        initGraphics();
        logUser();
        updateBackgroundOrientation();
    }

    public void goToMenu(View view) {
        Settings.soundPlayer.getClick().play();
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    public void goToReports(View view) {
        Settings.soundPlayer.getClick().play();
        Intent intent = new Intent(this, ReportsActivity.class);
        startActivity(intent);
    }

    public void goToModeration(View view) {
        Settings.soundPlayer.getClick().play();
        Intent intent = new Intent(this, ModerationActivity.class);
        startActivity(intent);
    }

    public void hidePin(View view) {
        updatePinned(pinnedMessage,false);
        makeTransition(R.id.pin_scene_root, R.layout.empty);
        showedPin = false;
    }

    public void makePinClick(View view) {
        if (pinnedMessage == null) return;
        listOfMessages.setSelection(pinnedIndex);
    }

    public void logOut(View view) {
        Settings.soundPlayer.getClick().play();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(getString(R.string.message_log_out))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), (dialogInterface, i) -> {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.signOut();
                    AuthUI.getInstance().signOut(this);
                    Settings.firebaseController = null;

                    Intent intent = new Intent(this, MenuActivity.class);
                    startActivity(intent);
                })
                .setNegativeButton(getString(R.string.no), (dialogInterface, i) -> {
                    dialogInterface.cancel();
                });

        AlertDialog dialog = builder.create();
        dialog.setTitle(getString(R.string.log_out_of_account));
        dialog.show();
    }

    public void showRules(View view) {
        Settings.soundPlayer.getClick().play();
        showDescriptionDialog();
    }

    public void sendMessage(View view) {
        if (!Settings.firebaseController.isReady() || checkAccount()) return;
        if (Settings.firebaseController.getUser().isMuted()) {
            Toast.makeText(this, getString(R.string.mute_alert), Toast.LENGTH_LONG).show();
            return;
        }
        EditText editText = findViewById(R.id.messageField);

        String dialogText = null;
        if (editText.getText().toString().length() < 5) {
            dialogText = getString(R.string.min_characters_message);
        } else if (editText.getText().toString().length() >= 200) {
            dialogText = getString(R.string.max_characters_message);
        }

        if (dialogText != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage(dialogText)
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.accept), (dialogInterface, i) -> {});
            AlertDialog dialog = builder.create();
            dialog.setTitle(getString(R.string.wrong_message));
            dialog.show();
            return;
        }

        sentMessage = true;
        Account user = Settings.firebaseController.getUser();
        Message message = new Message(Settings.firebaseController.generateMessageId(), Settings.firebaseController.getUser().getId(),
                user.getEmail(), user.getName(), editText.getText().toString());
        Settings.firebaseController.sendMessage(message);


        editText.setText("");
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        if (result.getResultCode() == RESULT_OK) {
            if (Settings.firebaseController == null) {
                Settings.firebaseController = new FirebaseController(FirebaseAuth.getInstance().getCurrentUser());
            }
            displayAllData();
        } else {
            makeTransition(R.id.blackout_root, R.layout.blackout);
            sendButton.setClickable(false);

            IdpResponse idpResponse = result.getIdpResponse();
            String log, title;
            if (idpResponse == null || idpResponse.getError() == null) {
                title = "Result code = " + result.getResultCode();
                log = getString(R.string.canceled_registartion);
            } else {
                title = "IOException";
                log = idpResponse.toString();
            }

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle(title)
                    .setMessage(log)
                    .setCancelable(false)
                    .setNegativeButton(R.string.again, (dialog1, which) -> {
                        makeTransition(R.id.blackout_root, R.layout.empty);
                        logUser();
                    })
                    .setPositiveButton(R.string.to_the_menu, (dialog1, which) -> {
                        Intent intent = new Intent(this, MenuActivity.class);
                        startActivity(intent);
                    })
                    .create();
            dialog.show();
        }
    }

    private void logUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Intent signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setTheme(R.style.Theme_Angara)
                    .build();
            signInLauncher.launch(signInIntent);
        } else {
            if (Settings.firebaseController == null) {
                Settings.firebaseController = new FirebaseController(FirebaseAuth.getInstance().getCurrentUser());
            }
            displayAllData();
        }
    }

    private void displayAllData() {
        OnChangeElement<Message> onChange = (v, model) -> {
            if (checkAccount()) return;
            saveScrollPosition();

            TextView messageUser, messageTime;
            BubbleTextView personMessageText, userMessageText;
            ImageButton helpButton, deleteButton, pinButton;

            helpButton = v.findViewById(R.id.help);
            deleteButton = v.findViewById(R.id.delete);
            pinButton = v.findViewById(R.id.pin);

            messageUser = v.findViewById(R.id.message_user);
            messageTime = v.findViewById(R.id.message_time);
            personMessageText = v.findViewById(R.id.person_message_text);
            userMessageText = v.findViewById(R.id.user_message_text);

            messageUser.setText(model.getUserName());
            messageTime.setText(TimeUtil.defineTimeView(this, model.getTime()));

            if (Settings.firebaseController.findAccount(model.getUserId()).isModerator()) {
                messageUser.setTextColor(Color.RED);
            } else {
                messageUser.setTextColor(Color.BLACK);
            }

            helpButton.setOnClickListener(v1 -> showReportDialog(model));
            deleteButton.setOnClickListener(v1 -> showDeleteDialog(model));
            pinButton.setOnClickListener(v1 -> pushPin(model));

            if (Settings.firebaseController.getUser().isModerator()) {
                deleteButton.setVisibility(View.VISIBLE);
                pinButton.setVisibility(View.VISIBLE);
            } else {
                deleteButton.setVisibility(View.INVISIBLE);
                pinButton.setVisibility(View.INVISIBLE);
            }

            if (model.getUserId() == Settings.firebaseController.getUser().getId()) {
                personMessageText.setText(model.getMessage());
                personMessageText.setVisibility(View.VISIBLE);
                userMessageText.setVisibility(View.INVISIBLE);
                helpButton.setVisibility(View.INVISIBLE);
                deleteButton.setVisibility(View.VISIBLE);
            } else {
                userMessageText.setText(model.getMessage());
                userMessageText.setVisibility(View.VISIBLE);
                personMessageText.setVisibility(View.INVISIBLE);
                helpButton.setVisibility(View.VISIBLE);
            }

            messageUser.setVisibility(View.VISIBLE);
            messageTime.setVisibility(View.VISIBLE);
        };
        ElementAdapter<Message> adapter = new ElementAdapter<>(this,
                R.layout.list_item_chat, onChange);
        listOfMessages.setAdapter(adapter);

        Settings.firebaseController.setOnUpdateMessages(elements -> {
            adapter.clear();
            updateMessages(adapter, elements);
            updatePin(elements);

            if (sentMessage) {
                sentMessage = false;
                return;
            }

            Runnable task = () -> runOnUiThread(() -> {
                listOfMessages.setSelectionFromTop(++index, top);
            });
            Settings.threadPool.execute(task);
        });

        sendButton.setClickable(false);
        Settings.loading.showWaiting(this, loadingLayout, () -> {
            adapter.clear();
            checkAccount();
            updateMessages(adapter, Settings.firebaseController.getMessages());
            updatePin(Settings.firebaseController.getMessages());
            sendButton.setClickable(true);
        }, false);
    }

    private void saveScrollPosition() {
        index = listOfMessages.getFirstVisiblePosition();
        View view = listOfMessages.getChildAt(0);
        top = (view == null) ? 0 : (view.getTop() - listOfMessages.getPaddingTop());
    }

    private void showReportDialog(Message message) {
        String[] items = {"Insult", "Spam", "Slender", "Personal information", "Discrimination", "Another"};
        String[] itemsUI =
                {getString(R.string.insult), getString(R.string.spam),
                getString(R.string.slander), getString(R.string.personal_information),
                getString(R.string.discrimination), getString(R.string.another)};

        AlertDialog selectionDialog =
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.reports))
                        .setItems(itemsUI, (dialog, which) -> {
                            Account user = Settings.firebaseController.getUser();
                            sendReport(new Report(Settings.firebaseController.generateReportId(), user.getId(), user.getName(),
                                    message.getUserId(), message.getUserName(), items[which], message.getMessage()));
                        })
                        .create();
        selectionDialog.show();
    }

    private void showDeleteDialog(Message message) {
        AlertDialog confirmDialog =
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.confirmation))
                        .setMessage(getString(R.string.confirmation_delete))
                        .setNegativeButton(getString(R.string.cancel), (dialog, which) -> {})
                        .setPositiveButton(getString(R.string.delete), (dialog, which) -> {
                            if (message.isPinned()) showedPin = false;
                            Settings.firebaseController.removeMessage(message);
                        })
                        .create();
        confirmDialog.show();
    }

    private void sendReport(Report report) {
        AlertDialog confirmDialog =
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.confirmation))
                        .setMessage(getString(R.string.confirmation_report))
                        .setNegativeButton(getString(R.string.cancel), (dialog, which) -> {})
                        .setPositiveButton(getString(R.string.send), (dialog, which) -> {
                            Settings.firebaseController.sendReport(report);
                            Toast.makeText(this, getString(R.string.thanks_help), Toast.LENGTH_SHORT).show();
                        })
                        .create();
        confirmDialog.show();
    }

    private void updateMessages(ElementAdapter<Message> adapter, List<Message> messages) {;
        messages.forEach(adapter::add);
    }

    private void updatePin(List<Message> messages) {
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).isPinned()) {
                pinnedIndex = i;
                showPin(messages.get(i));
                return;
            }
        }
        makeTransition(R.id.pin_scene_root, R.layout.empty);
    }

    private void pushPin(Message message) {
        if (showedPin) {
            Toast.makeText(this, getString(R.string.have_pinned_message), Toast.LENGTH_SHORT).show();
            return;
        }
        updatePinned(message, true);
        showPin(message);
    }

    private void showPin(Message message) {
        makeTransition(R.id.pin_scene_root, R.layout.pin);
        pinnedMessage = message;
        ImageButton closePin = findViewById(R.id.hide_pin);
        if (Settings.firebaseController.getUser().isModerator()) closePin.setVisibility(View.VISIBLE);
        TextView textPin = findViewById(R.id.text_pin);
        textPin.setText(message.getMessage());
        showedPin = true;
    }

    private void updatePinned(Message message, boolean pinnedStatus) {
        message.setPinned(pinnedStatus);
        Settings.firebaseController.updateMessage(message);
    }

    private void showDescriptionDialog() {
        makeTransition(R.id.blackout_root, R.layout.blackout);
        makeTransition(R.id.description_scene_root, R.layout.dialog);

        initDialogTextData();
        descriptionDisplay = true;
    }

    private void closeRules() {
        Settings.soundPlayer.getClick().play();

        sendButton.setClickable(true);
        makeTransition(R.id.blackout_root, R.layout.empty);
        makeTransition(R.id.description_scene_root, R.layout.empty);
        descriptionDisplay = false;
    }

    private boolean checkAccount() {
        if (Settings.firebaseController.getUser().isModerator()) {
            moderation.setVisibility(View.VISIBLE);
        }

        if (Settings.firebaseController.getUser().isBanned()) {
            Toast.makeText(this, getString(R.string.ban_alert), Toast.LENGTH_LONG).show();
            goToMenu(null);
            return true;
        }
        return false;
    }

    private void initDialogTextData() {
        NavigateImageView close = findViewById(R.id.close);
        close.setColorDown(Color.argb(155, 0, 0, 0));
        close.setOnClickListener(v -> closeRules());
        sendButton.setClickable(false);

        TextView titleView = findViewById(R.id.title);
        TextView descriptionView = findViewById(R.id.description_text);

        titleView.setText(getString(R.string.rules));
        descriptionView.setText(getString(R.string.rules_description));
    }

    private void initGraphics() {
        chatLayout = findViewById(R.id.chat);
        loadingLayout = findViewById(R.id.loading);
        listOfMessages = findViewById(R.id.list_of_messages);
        moderation = findViewById(R.id.moderation);
        sendButton = findViewById(R.id.send_button);

        if (Settings.loading == null) {
            Settings.loading = new DatabaseLoading();
        }
    }

    private void updateBackgroundOrientation() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            chatLayout.setBackgroundResource(R.drawable.forum_background_v);
        } else {
            chatLayout.setBackgroundResource(R.drawable.forum_background_h);
        }

        if (descriptionDisplay) {
            showDescriptionDialog();
        }
    }

    private void makeTransition(int rootId, int layout) {
        ViewGroup root = findViewById(rootId);
        Scene another = Scene.getSceneForLayout(root, layout, this);

        Transition fadeTransit = TransitionInflater.from(this).inflateTransition(R.transition.fade_transition);
        TransitionManager.go(another, fadeTransit);
    }
}
