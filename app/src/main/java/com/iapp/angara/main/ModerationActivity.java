package com.iapp.angara.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.iapp.angara.R;
import com.iapp.angara.database.Account;
import com.iapp.angara.database.OnUpdateDatabase;
import com.iapp.angara.ui.ElementAdapter;
import com.iapp.angara.ui.OnChangeElement;
import com.iapp.angara.util.SearchUtil;
import com.iapp.angara.util.Settings;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ModerationActivity extends AppCompatActivity {

    private static final String BAN = "ban";
    private static final String UNBAN = "unban";
    private static final String MUTE = "mute";
    private static final String UNMUTE = "unmute";
    private static final String WARN = "warn";

    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy hh:mm");

    private RelativeLayout moderationLayout;
    private ListView accountsView;
    private RelativeLayout loadingLayout;
    private EditText searchField;

    private String request;
    private boolean showedPin;
    private int index, top;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_moderation);

        initGraphics();
        initData();

        updateBackgroundOrientation();
    }

    public void search(View view) {
        Settings.soundPlayer.getClick().play();
        request = searchField.getText().toString();
        initData();
    }

    public void goToChat(View view) {
        Settings.soundPlayer.getClick().play();
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
    }

    private void initData() {
        OnChangeElement<Account> onChange = (view, account) -> {
            saveScrollPosition();

            TextView contentId = view.findViewById(R.id.content_id),
                    contentName = view.findViewById(R.id.content_name),
                    contentEmail = view.findViewById(R.id.content_email),
                    contentMuted = view.findViewById(R.id.content_muted),
                    contentBanned = view.findViewById(R.id.content_banned),
                    contentModerator = view.findViewById(R.id.content_moderator),
                    contentReputation = view.findViewById(R.id.content_reputation);
            Button applyBan = view.findViewById(R.id.apply_ban),
                    applyMute = view.findViewById(R.id.apply_mute),
                    applyWarn = view.findViewById(R.id.apply_warn);

            contentId.setText(String.valueOf(account.getId()));
            contentName.setText(account.getName());
            contentEmail.setText(account.getEmail());
            contentMuted.setText(String.valueOf(account.isMuted()));
            contentBanned.setText(String.valueOf(account.isBanned()));
            contentModerator.setText(String.valueOf(account.isModerator()));
            contentReputation.setText(account.getReputation());

            applyWarn.setOnClickListener(v1 -> showDialogWarn(account));
            if (!account.isBanned()) {
                applyBan.setText(getString(R.string.ban));
                applyBan.setOnClickListener(v1 -> showDialogBan(account, applyBan));
            } else {
                applyBan.setText(getString(R.string.unban));
                applyBan.setOnClickListener(v1 -> showDialogUnban(account, applyBan));
            }

            if (!account.isMuted()) {
                applyMute.setText(getString(R.string.mute));
                applyMute.setOnClickListener(v1 -> showDialogMute(account, applyMute));
            } else {
                applyMute.setText(getString(R.string.unmute));
                applyMute.setOnClickListener(v1 -> showDialogUnmute(account, applyMute));
            }
        };

        ElementAdapter<Account> adapter = new ElementAdapter<>(this,
                R.layout.list_item_moderation, onChange);
        accountsView.setAdapter(adapter);

        Settings.firebaseController.setOnUpdateAccounts((OnUpdateDatabase<Account>) accounts -> {
            adapter.clear();
            updateAccounts(adapter, accounts);

            Runnable task = () -> runOnUiThread(() -> accountsView.setSelectionFromTop(index, top));
            Settings.threadPool.execute(task);
        });
        Settings.loading.showWaiting(this, loadingLayout,
                () -> updateAccounts(adapter, Settings.firebaseController.getAccounts()), false);
    }

    private void saveScrollPosition() {
        index = accountsView.getFirstVisiblePosition() + 1;
        view = accountsView.getChildAt(0);
        top = (view == null) ? 0 : (view.getTop() - accountsView.getPaddingTop());
    }

    private void updateAccounts(ElementAdapter<Account> adapter, List<Account> accounts) {
        List<Account> finedAccounts = SearchUtil.parseAccountSearchParam(this, request, accounts);
        finedAccounts.forEach(adapter::add);
    }

    private void showDialogBan(Account applied, Button applyBan) {
        if (applied.isModerator()) {
            Toast.makeText(this,getString(R.string.punish_mod), Toast.LENGTH_LONG).show();
            return;
        }
        EditText edittext = new EditText(this);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.confirmation))
                .setMessage(getString(R.string.reason_ban))
                .setView(edittext)
                .setNegativeButton(getString(R.string.cancel), (dialog, which) -> {})
                .setPositiveButton(getString(R.string.ban), (dialog, which) -> {
                    applied.ban();
                    updateReputation(applied, BAN, edittext.getText().toString());
                    Settings.firebaseController.updateAccount(applied);

                    applyBan.setText(UNBAN);
                    applyBan.setOnClickListener(v -> showDialogUnban(applied, applyBan));
                })
                .create();

        alertDialog.show();
    }

    private void showDialogMute(Account applied, Button applyMute) {
        if (applied.isModerator()) {
            Toast.makeText(this,getString(R.string.punish_mod), Toast.LENGTH_LONG).show();
            return;
        }
        EditText edittext = new EditText(this);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.confirmation))
                .setMessage(getString(R.string.reason_mute))
                .setView(edittext)
                .setNegativeButton(getString(R.string.cancel), (dialog, which) -> {})
                .setPositiveButton(getString(R.string.mute), (dialog, which) -> {
                    applied.mute();
                    updateReputation(applied, MUTE, edittext.getText().toString());
                    Settings.firebaseController.updateAccount(applied);

                    applyMute.setText(UNMUTE);
                    applyMute.setOnClickListener(v -> showDialogUnmute(applied, applyMute));
                })
                .create();

        alertDialog.show();
    }

    private void showDialogUnban(Account applied, Button applyBan) {
        if (applied.isModerator()) {
            Toast.makeText(this,getString(R.string.punish_mod), Toast.LENGTH_LONG).show();
            return;
        }
        EditText edittext = new EditText(this);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.confirmation))
                .setMessage(getString(R.string.reason_unban))
                .setView(edittext)
                .setNegativeButton(getString(R.string.cancel), (dialog, which) -> {})
                .setPositiveButton(getString(R.string.unban), (dialog, which) -> {
                    applied.unban();
                    updateReputation(applied, UNBAN, edittext.getText().toString());
                    Settings.firebaseController.updateAccount(applied);

                    applyBan.setText(BAN);
                    applyBan.setOnClickListener(v -> showDialogBan(applied, applyBan));
                })
                .create();

        alertDialog.show();
    }

    private void showDialogUnmute(Account applied, Button applyMute) {
        if (applied.isModerator()) {
            Toast.makeText(this,getString(R.string.punish_mod), Toast.LENGTH_LONG).show();
            return;
        }
        EditText edittext = new EditText(this);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.confirmation))
                .setMessage(getString(R.string.reason_unmute))
                .setView(edittext)
                .setNegativeButton(getString(R.string.cancel), (dialog, which) -> {})
                .setPositiveButton(getString(R.string.unmute), (dialog, which) -> {
                    applied.unmute();
                    updateReputation(applied, UNMUTE, edittext.getText().toString());
                    Settings.firebaseController.updateAccount(applied);

                    applyMute.setText(MUTE);
                    applyMute.setOnClickListener(v -> showDialogMute(applied, applyMute));
                })
                .create();

        alertDialog.show();
    }

    private void showDialogWarn(Account applied) {
        if (applied.isModerator()) {
            Toast.makeText(this,getString(R.string.punish_mod), Toast.LENGTH_LONG).show();
            return;
        }
        EditText edittext = new EditText(this);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.confirmation))
                .setMessage(getString(R.string.reason_warn))
                .setView(edittext)
                .setNegativeButton(getString(R.string.cancel), (dialog, which) -> {})
                .setPositiveButton(getString(R.string.apply), (dialog, which) -> {
                    updateReputation(applied, WARN, edittext.getText().toString());
                    Settings.firebaseController.updateAccount(applied);
                })
                .create();

        alertDialog.show();
    }

    private void updateReputation(Account punishable, String action, String cause) {
        String time = formatDate.format(new Date());
        String lastReputation = punishable.getReputation().replaceAll(Account.START_REPUTATION, "");

        punishable.setReputation(String.format("%stype = %s, time = %s, mod = %s, cause = %s\n\n",
               lastReputation, action, time, Settings.firebaseController.getFirebaseUser().getDisplayName(), cause));
    }

    private void initGraphics() {
        moderationLayout = findViewById(R.id.moderation);
        accountsView = findViewById(R.id.list_of_accounts);
        loadingLayout = findViewById(R.id.loading);
        searchField = findViewById(R.id.search_field);
    }

    private void updateBackgroundOrientation() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            moderationLayout.setBackgroundResource(R.drawable.forum_background_v);
        } else {
            moderationLayout.setBackgroundResource(R.drawable.forum_background_h);
        }
    }
}