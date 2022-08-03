package com.iapp.angara.database;

import static org.junit.Assert.*;

import com.iapp.angara.AccessUtil;

import org.junit.Test;;

import java.util.List;

public class FirebaseControllerTest {

    @Test
    public void findAccount() {
        FirebaseController firebaseController = AccessUtil.newInstance(FirebaseController.class);
        List<Account> accounts = AccessUtil.getField(firebaseController, "accounts");
        for (int i = 0; i < 10; i++) {
            accounts.add(new Account(i, "name" + i, "email" + i));
        }

        assertEquals(new Account(9, "name9", "email9"), firebaseController.findAccount(9));
        assertThrows(IllegalArgumentException.class, () -> firebaseController.findAccount(10));
    }

    @Test
    public void findReport() {
        FirebaseController firebaseController = AccessUtil.newInstance(FirebaseController.class);
        List<Report> reports = AccessUtil.getField(firebaseController, "reports");
        for (int i = 0; i < 10; i++) {
            reports.add(new Report(i, i, "senderName" + i, i,
                            "guiltyName" + i, "cause" + i, "message" + i));
        }

        assertEquals(new Report(9, 9, "senderName9", 9,
                "guiltyName9",  "cause9", "message9"),
                firebaseController.findReport(9));
        assertThrows(IllegalArgumentException.class, () -> firebaseController.findReport(10));
    }

    @Test
    public void findMessage() {
        FirebaseController firebaseController = AccessUtil.newInstance(FirebaseController.class);
        List<Message> messages = AccessUtil.getField(firebaseController, "messages");
        for (int i = 0; i < 10; i++) {
            messages.add(new Message(i, i, "email" + i, "userName" + i, "message" + i));
        }

        assertEquals(new Message(9, 9, "email9", "userName9", "message9"),
                firebaseController.findMessage(9));
        assertThrows(IllegalArgumentException.class, () -> firebaseController.findMessage(10));
    }

    @Test
    public void generateAccountId() {
        FirebaseController firebaseController = AccessUtil.newInstance(FirebaseController.class);
        List<Account> accounts = AccessUtil.getField(firebaseController, "accounts");

        assertEquals(1, firebaseController.generateAccountId());
        for (int i = 0; i < 10; i++) {
            accounts.add(new Account(i, "name" + i, "email" + i));
        }
        assertEquals(10, firebaseController.generateAccountId());
        accounts.add(new Account(100, "name100", "email100"));
        assertEquals(101, firebaseController.generateAccountId());
        accounts.add(new Account(Long.MAX_VALUE, "name_max", "email_max"));
        assertEquals(Long.MIN_VALUE, firebaseController.generateAccountId());
    }

    @Test
    public void generateReportId() {
        FirebaseController firebaseController = AccessUtil.newInstance(FirebaseController.class);
        List<Report> reports = AccessUtil.getField(firebaseController, "reports");

        assertEquals(1, firebaseController.generateReportId());
        for (int i = 0; i < 10; i++) {
            reports.add(new Report(i, i, "senderName" + i, i,
                    "guiltyName" + i, "cause" + i, "message" + i));
        }
        assertEquals(10, firebaseController.generateReportId());
        reports.add(new Report(100, 100, "senderName100", 100,
                "guiltyName100", "cause100", "message100"));
        assertEquals(101, firebaseController.generateReportId());
        reports.add(new Report(Long.MAX_VALUE,Long.MAX_VALUE, "senderName_max", Long.MAX_VALUE,
                "guiltyName_max", "cause_max", "message_max"));
        assertEquals(Long.MIN_VALUE, firebaseController.generateReportId());
    }

    @Test
    public void generateMessageId() {
        FirebaseController firebaseController = AccessUtil.newInstance(FirebaseController.class);
        List<Message> messages = AccessUtil.getField(firebaseController, "messages");

        assertEquals(1, firebaseController.generateMessageId());
        for (int i = 0; i < 10; i++) {
            messages.add(new Message(i, i, "email" + i, "userName" + i, "message" + i));
        }
        assertEquals(10, firebaseController.generateMessageId());
        messages.add(new Message(100, 100, "email100", "userName100", "message100"));
        assertEquals(101, firebaseController.generateMessageId());
        messages.add(new Message(Long.MAX_VALUE, Long.MAX_VALUE, "email_max", "userName_max", "message_max"));
        assertEquals(Long.MIN_VALUE, firebaseController.generateMessageId());
    }
}