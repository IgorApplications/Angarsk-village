package com.iapp.angara.database;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.iapp.angara.ui.OnActionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FirebaseController {

    public static final String CHILD_ACCOUNTS = "accounts";
    public static final String CHILD_REPORTS = "reports";
    public static final String CHILD_MESSAGES = "general_chat";
    private static final String CHANNEL_ID = "FORUM";

    private volatile Account user;
    private final FirebaseUser firebaseUser;
    private final DatabaseReference databaseReference;
    private NotificationManagerCompat notificationManager;

    private final List<Account> accounts = new ArrayList<>();
    private final List<Report> reports = new ArrayList<>();
    private final List<Message> messages = new ArrayList<>();

    private Optional<OnUpdateDatabase<Message>> onUpdateMessages = Optional.empty();
    private Optional<OnUpdateDatabase<Report>> onUpdateReports = Optional.empty();
    private Optional<OnUpdateDatabase<Account>> onUpdateAccounts = Optional.empty();

    private boolean readyAccounts;
    private boolean readyReports;
    private boolean readyMessages;

    public FirebaseController(FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
        databaseReference = FirebaseDatabase.getInstance().getReference();

        initData();
    }

    public Account getUser() {
        return user;
    }

    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public boolean isReady() {
        return isReadyAccounts() && isReadyReports() && isReadyMessages();
    }

    public boolean isReadyAccounts() {
        return readyAccounts;
    }

    public boolean isReadyReports() {
        return readyReports;
    }

    public boolean isReadyMessages() {
        return readyMessages;
    }

    public List<Message> getMessages() {
        return new ArrayList<>(messages);
    }

    public List<Report> getReports() {
        return new ArrayList<>(reports);
    }

    public List<Account> getAccounts() {
        return new ArrayList<>(accounts);
    }

    public void setOnUpdateAccounts(OnUpdateDatabase<Account> onUpdateAccounts) {
        this.onUpdateAccounts = Optional.of(onUpdateAccounts);
    }

    public void setOnUpdateReports(OnUpdateDatabase<Report> onUpdateReports) {
        this.onUpdateReports = Optional.of(onUpdateReports);
    }

    public void setOnUpdateMessages(OnUpdateDatabase<Message> onUpdateMessages) {
        this.onUpdateMessages = Optional.of(onUpdateMessages);
    }

    public Account findAccount(long id) {
        return (Account) find(accounts, id);
    }

    public Report findReport(long id) {
        return (Report) find(reports, id);
    }

    public Message findMessage(long id) {
        return (Message) find(messages, id);
    }

    public void updateAccount(Account account) {
        update(CHILD_ACCOUNTS, account);
    }

    public void updateReport(Report report) {
        update(CHILD_REPORTS, report);
    }

    public void updateMessage(Message message) {
        update(CHILD_MESSAGES, message);
    }

    public void addAccount(Account account) {
        accounts.add(account);
        databaseReference.child(CHILD_ACCOUNTS).push().setValue(account);
    }

    public void sendReport(Report report) {
        reports.add(report);
        databaseReference.child(CHILD_REPORTS).push().setValue(report);
    }

    public void sendMessage(Message message) {
        messages.add(message);
        databaseReference.child(CHILD_MESSAGES).push().setValue(message);
    }

    public void removeAccount(Account account) {
        remove(CHILD_ACCOUNTS, accounts, account);
    }

    public void removeReport(Report report) {
        remove(CHILD_REPORTS, reports, report);
    }

    public void removeMessage(Message message) {
        remove(CHILD_MESSAGES, messages, message);
    }

    public long generateAccountId() {
        return getMaxId(accounts) + 1;
    }

    public long generateReportId() {
        return getMaxId(reports) + 1;
    }

    public long generateMessageId() {
        return getMaxId(messages) + 1;
    }

    private Element find(List<? extends Element> storage, long id) {
        for (Element element : storage) {
            if (element.getId() == id) {
                return element;
            }
        }
        throw new IllegalArgumentException();
    }

    private void update(String child, Element element) {
        Query applesQuery = databaseReference.child(child).orderByChild("id").equalTo(element.getId());

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot applySnapshot : dataSnapshot.getChildren()) {
                    applySnapshot.getRef().setValue(element);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }
        });
    }

    private void remove(String child, List<? extends Element> storage, Element element) {
        Query applesQuery = databaseReference.child(child).orderByChild("id").equalTo(element.getId());

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
                storage.remove(element);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }
        });
    }

    private void initData() {
        downloadData(CHILD_ACCOUNTS, accounts, Account.class, () -> {
            updateUserAccount();
            readyAccounts = true;
            onUpdateAccounts.ifPresent(l -> l.onUpdate(new ArrayList<>(accounts)));
        });
        downloadData(CHILD_MESSAGES, messages, Message.class, () -> {
            readyMessages = true;
            onUpdateMessages.ifPresent(l -> l.onUpdate(new ArrayList<>(messages)));
        });
        downloadData(CHILD_REPORTS, reports, Report.class, () -> {
            readyReports = true;
            onUpdateReports.ifPresent(l -> l.onUpdate(new ArrayList<>(reports)));
        });
    }

    private void updateUserAccount() {
        for (Account account : accounts) {
            if (account.getEmail().equals(firebaseUser.getEmail())) {
                user = account;
                return;
            }
        }

        if (user == null) {
            user = new Account(generateAccountId(), firebaseUser.getDisplayName(), firebaseUser.getEmail());
            addAccount(user);
        }
    }

    private <T> void downloadData(String child, List<T> storage, Class<T> clazz, OnActionListener onFinish) {
       databaseReference.child(child).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                storage.clear();
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    storage.add(singleSnapshot.getValue(clazz));
                }
                if (onFinish != null) onFinish.onAction();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }
       });
    }

    private long getMaxId(List<? extends Element> storage) {
        if (storage.isEmpty()) return 0;

        long maxId = Integer.MIN_VALUE;
        for (Element element : storage) {
            if (element.getId() > maxId) {
                maxId = element.getId();
            }
        }
        return maxId;
    }
}
