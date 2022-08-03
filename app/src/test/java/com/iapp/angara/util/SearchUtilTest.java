package com.iapp.angara.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import android.content.Context;

import com.iapp.angara.R;
import com.iapp.angara.database.Account;
import com.iapp.angara.database.Report;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class SearchUtilTest {

    @Test
    public void parseAccountSearchParam() {
        Context context = Mockito.mock(Context.class);
        Mockito.when(context.getString(R.string.identifier)).thenReturn("Identifier:");
        Mockito.when(context.getString(R.string.name)).thenReturn("Name:");
        Mockito.when(context.getString(R.string.email)).thenReturn("Email:");
        Mockito.when(context.getString(R.string.muted)).thenReturn("Muted:");
        Mockito.when(context.getString(R.string.banned)).thenReturn("Banned:");
        Mockito.when(context.getString(R.string.moderator)).thenReturn("Moderator:");
        Mockito.when(context.getString(R.string.reputation)).thenReturn("Reputation:");

        List<Account> accounts = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                accounts.add(new Account(j, "name" + j, "email" + j));
            }
        }

        assertEquals(SearchUtil.parseAccountSearchParam(context, "Identifier:5", accounts).size(), 10);
        assertEquals(SearchUtil.parseAccountSearchParam(context, "Name:name5", accounts).size(), 10);
        assertEquals(SearchUtil.parseAccountSearchParam(context, "Email:email5", accounts).size(), 10);

        assertEquals(SearchUtil.parseAccountSearchParam(context, "Muted:false", accounts).size(), 100);
        assertEquals(SearchUtil.parseAccountSearchParam(context, "Banned:false", accounts).size(), 100);
        assertEquals(SearchUtil.parseAccountSearchParam(context, "Moderator:false", accounts).size(), 100);
        assertEquals(SearchUtil.parseAccountSearchParam(context, "Reputation:No violations", accounts).size(), 100);

        Account account = accounts.get(20);
        account.mute();
        account.ban();
        account.makeModerator();
        account.setReputation("Good person");

        assertEquals(SearchUtil.parseAccountSearchParam(context, "Muted:true", accounts).size(), 1);
        assertEquals(SearchUtil.parseAccountSearchParam(context, "Banned:true", accounts).size(), 1);
        assertEquals(SearchUtil.parseAccountSearchParam(context, "Moderator:true", accounts).size(), 1);
        assertEquals(SearchUtil.parseAccountSearchParam(context, "Reputation:Good person", accounts).size(), 1);
    }

    @Test
    public void parseReportSearchParam() {
        Context context = Mockito.mock(Context.class);
        Mockito.when(context.getString(R.string.identifier)).thenReturn("Identifier:");
        Mockito.when(context.getString(R.string.sender_id)).thenReturn("Sender Id:");
        Mockito.when(context.getString(R.string.sender_name)).thenReturn("Sender name:");
        Mockito.when(context.getString(R.string.guilty_id)).thenReturn("Guilty Id:");
        Mockito.when(context.getString(R.string.guilty_name)).thenReturn("Guilty name:");
        Mockito.when(context.getString(R.string.cause)).thenReturn("Cause:");
        Mockito.when(context.getString(R.string.message)).thenReturn("Message:");
        Mockito.when(context.getString(R.string.report_status)).thenReturn("Report status:");
        Mockito.when(context.getString(R.string.under_consideration)).thenReturn("Under consideration");
        Mockito.when(context.getString(R.string.received)).thenReturn("Received");
        Mockito.when(context.getString(R.string.rejected)).thenReturn("Rejected");
        Mockito.when(context.getString(R.string.report_status)).thenReturn("Report status:");

        List<Report> reports = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                reports.add(new Report(j, j, "senderName" + j, j,
                        "guiltyName" + j, "cause" + j, "message" + j));
            }
        }

        assertEquals(SearchUtil.parseReportSearchParam(context, "Identifier:5", reports).size(), 10);
        assertEquals(SearchUtil.parseReportSearchParam(context, "Sender Id:5", reports).size(), 10);
        assertEquals(SearchUtil.parseReportSearchParam(context, "Sender name:senderName5", reports).size(), 10);
        assertEquals(SearchUtil.parseReportSearchParam(context, "Guilty Id:5", reports).size(), 10);
        assertEquals(SearchUtil.parseReportSearchParam(context, "Guilty name:guiltyName5", reports).size(), 10);
        assertEquals(SearchUtil.parseReportSearchParam(context, "Cause:cause5", reports).size(), 10);
        assertEquals(SearchUtil.parseReportSearchParam(context, "Message:message5", reports).size(), 10);

        Report report10 = reports.get(10);
        report10.setReviewed(true);
        report10.setReceived(true);
        reports.set(10, report10);
        assertEquals(SearchUtil.parseReportSearchParam(context, "Report status:Received", reports).size(), 1);

        Report report11 = reports.get(11);
        report11.setReviewed(true);
        report11.setRejected(true);
        reports.set(11, report11);
        assertEquals(SearchUtil.parseReportSearchParam(context, "Report status:Rejected", reports).size(), 1);
        assertEquals(SearchUtil.parseReportSearchParam(context, "Report status:Under consideration", reports).size(), 98);
    }
}