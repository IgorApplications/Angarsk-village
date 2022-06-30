package com.iapp.angara.main;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.firebase.ui.database.FirebaseListAdapter;
import com.github.library.bubbleview.BubbleTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iapp.angara.R;
import com.iapp.angara.database.Message;
import com.iapp.angara.util.Settings;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class ChatActivity extends AppCompatActivity {

    private final List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.PhoneBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build());

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            this::onSignInResult
    );

    private final AtomicLong countMessages = new AtomicLong(0);
    private ExecutorService service;

    private DatabaseReference databaseReference;
    private FirebaseUser user;

    private RelativeLayout chatLayout;
    private RelativeLayout loadingLayout;

    private ListView listOfMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setSupportActionBar(findViewById(R.id.action_bar));
        setContentView(R.layout.activity_chat);

        service = Executors.newFixedThreadPool(1);
        initGraphics();
        initData();

        updateBackgroundOrientation();
    }

    public void goToMenu(View view) {
        Settings.soundPlayer.getClick().play();
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
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

                    finish();
                })
                .setNegativeButton(getString(R.string.no), (dialogInterface, i) -> {
                    dialogInterface.cancel();
                });

        AlertDialog dialog = builder.create();
        dialog.setTitle(getString(R.string.log_out_of_account));
        dialog.show();
    }

    public void sendMessage(View view) {
        EditText editText = findViewById(R.id.messageField);

        String dialogText = null;
        if (editText.getText().toString().length() < 20) {
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

        Message message = new Message(user.getEmail(), user.getDisplayName(), editText.getText().toString());
        FirebaseDatabase.getInstance().getReference().push().setValue(message);
        editText.setText("");
        countMessages.incrementAndGet();
    }

    @Override
    public void finish() {
        service.shutdownNow();
        super.finish();
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        if (result.getResultCode() == RESULT_OK) {
            user = FirebaseAuth.getInstance().getCurrentUser();
            displayAllMessages();
        } else {
            IdpResponse idpResponse = result.getIdpResponse();
            if (idpResponse != null && idpResponse.getError() != null) {
                Toast.makeText(this, getString(R.string.login_failed), Toast.LENGTH_LONG).show();
            }
            finish();
        }
    }

    private void displayAllMessages() {
        AtomicLong numDownloadedMessages = new AtomicLong(0L);

        FirebaseListAdapter<Message> adapter =
                new FirebaseListAdapter<Message>(this, Message.class,
                        R.layout.list_item, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, Message model, int position) {
                TextView messageUser, messageTime;
                BubbleTextView personMessageText, userMessageText;

                messageUser = v.findViewById(R.id.message_user);
                messageTime = v.findViewById(R.id.message_time);
                personMessageText = v.findViewById(R.id.person_message_text);
                userMessageText = v.findViewById(R.id.user_message_text);

                messageUser.setText(model.getUserName());
                messageTime.setText(defineTimeView(model.getTime()));

                if (model.isMessageOf(user.getDisplayName())) {
                    personMessageText.setText(model.getMessage());
                    personMessageText.setVisibility(View.VISIBLE);
                    userMessageText.setVisibility(View.INVISIBLE);
                } else {
                    userMessageText.setText(model.getMessage());
                    userMessageText.setVisibility(View.VISIBLE);
                    personMessageText.setVisibility(View.INVISIBLE);
                }

                numDownloadedMessages.incrementAndGet();
            }
        };
        listOfMessages.setAdapter(adapter);

        showWaitingMessages(numDownloadedMessages);
    }

    private void showWaitingMessages(AtomicLong numDownloadedMessages) {
        Runnable task = () -> {
            runOnUiThread(() -> loadingLayout.setVisibility(View.VISIBLE));
            while (numDownloadedMessages.get() == countMessages.get()) {
                Thread.yield();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            runOnUiThread(() -> loadingLayout.setVisibility(View.INVISIBLE));
        };
        service.execute(task);
    }

    private void registerUser() {
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTheme(R.style.Theme_Angara)
                .build();
        signInLauncher.launch(signInIntent);
    }

    private void initGraphics() {
        chatLayout = findViewById(R.id.chat);
        loadingLayout = findViewById(R.id.loading);
        listOfMessages = findViewById(R.id.list_of_messages);
    }

    private void initData() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                countMessages.set(dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) registerUser();
        else displayAllMessages();
    }

    private void updateBackgroundOrientation() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            chatLayout.setBackgroundResource(R.drawable.forum_background_v);
        } else {
            chatLayout.setBackgroundResource(R.drawable.forum_background_h);
        }
    }

    private String defineTimeView(long time) {
        long passed = new Date().getTime() - time;

        String res;
        if (getSeconds(passed) < 60) {
            res = getString(R.string.less_minute);
        } else if (getMinutes(passed) < 60) {
            res = DateFormat.format("mm ", new Date(passed)) + getString(R.string.minutes_ago);
        } else {
            res = String.valueOf(DateFormat.format("HH:mm", new Date(time)));
        }
        res = res.replaceAll("^0", "");

        return res;
    }

    private int getSeconds(long time) {
        return (int) (time / 1000);
    }

    private int getMinutes(long time) {
        return (int) (time / 1000 / 60);
    }
}
