package com.iapp.angara.util;

import android.content.Context;

import com.google.firebase.database.Query;
import com.iapp.angara.R;
import com.iapp.angara.database.Account;
import com.iapp.angara.database.Report;

import java.util.List;
import java.util.stream.Collectors;

public class SearchUtil {

    private static final String REGEX_SEPARATOR = "\\s*:\\s*";

    public static List<Account> parseAccountSearchParam(Context context, String request, List<Account> accounts) {
        if (request == null) return accounts;
        String[] searchParameters = request.split(REGEX_SEPARATOR);
        if (searchParameters.length < 2) return accounts;

        searchParameters[0] = searchParameters[0] + ":";
        if (searchParameters[0].equals(context.getString(R.string.identifier))) {
            searchParameters[0] = Account.ID;
            return accounts.stream()
                    .filter(account->
                            account.getId() == Long.parseLong(searchParameters[1]))
                    .collect(Collectors.toList());
        }

        if (searchParameters[0].equals(context.getString(R.string.name))) {
            searchParameters[0] = Account.NAME;
            return accounts.stream()
                    .filter(account ->
                            account.getName().equals(searchParameters[1]))
                    .collect(Collectors.toList());
        }

        if (searchParameters[0].equals(context.getString(R.string.emai))) {
            searchParameters[0] = Account.EMAIL;
            return accounts.stream()
                    .filter(account ->
                            account.getEmail().equals(searchParameters[1]))
                    .collect(Collectors.toList());
        }

        if (searchParameters[0].equals(context.getString(R.string.muted))) {
            searchParameters[0] = Account.MUTED;
            return accounts.stream()
                    .filter(account ->
                            account.isMuted() == Boolean.parseBoolean(searchParameters[1]))
                    .collect(Collectors.toList());
        }

        if (searchParameters[0].equals(context.getString(R.string.banned))) {
            searchParameters[0] = Account.BANNED;
            return accounts.stream()
                    .filter(account ->
                            account.isBanned() == Boolean.parseBoolean(searchParameters[1]))
                    .collect(Collectors.toList());
        }

        if (searchParameters[0].equals(context.getString(R.string.moderator))) {
            searchParameters[0] = Account.MODERATOR;
            return accounts.stream()
                    .filter(account ->
                            account.isModerator() == Boolean.parseBoolean(searchParameters[1]))
                    .collect(Collectors.toList());
        }

        if (searchParameters[0].equals(context.getString(R.string.reputation))) {
            searchParameters[0] = Account.REPUTATION;
            return accounts.stream()
                    .filter(account ->
                            account.getReputation().equals(searchParameters[1]))
                    .collect(Collectors.toList());
        }

        return accounts;
    }

    public static List<Report> parseReportSearchParam(Context context, String request, List<Report> reports) {
        if (request == null) return reports;
        String[] searchParameters = request.split(REGEX_SEPARATOR);
        if (searchParameters.length < 2) return reports;

        searchParameters[0] = searchParameters[0] + ":";
        if (searchParameters[0].equals(context.getString(R.string.identifier))) {
            searchParameters[0] = Report.ID;
            return reports.stream()
                    .filter(report -> report.getId() == Long.parseLong(searchParameters[1]))
                    .collect(Collectors.toList());
        }

        if (searchParameters[0].equals(context.getString(R.string.sender_id))) {
            searchParameters[0] = Report.SENDER_ID;
            return reports.stream()
                    .filter(report -> report.getSenderId() == Long.parseLong(searchParameters[1]))
                    .collect(Collectors.toList());
        }

        if (searchParameters[0].equals(context.getString(R.string.sender_name))) {
            searchParameters[0] = Report.SENDER_NAME;
            return reports.stream()
                    .filter(report -> report.getSenderName().equals(searchParameters[1]))
                    .collect(Collectors.toList());
        }

        if (searchParameters[0].equals(context.getString(R.string.guility_id))) {
            searchParameters[0] = Report.GUILTY_ID;
            return reports.stream()
                    .filter(report -> report.getGuiltyId() == Long.parseLong(searchParameters[1]))
                    .collect(Collectors.toList());
        }

        if (searchParameters[0].equals(context.getString(R.string.guilty_name))) {
            searchParameters[0] = Report.GUILTY_NAME;
            return reports.stream()
                    .filter(report -> report.getGuiltyName().equals(searchParameters[1]))
                    .collect(Collectors.toList());
        }

        if (searchParameters[0].equals(context.getString(R.string.cause))) {
            searchParameters[0] = Report.CAUSE;
            return reports.stream()
                    .filter(report -> report.getCause().equals(searchParameters[1]))
                    .collect(Collectors.toList());
        }

        if (searchParameters[0].equals(context.getString(R.string.message))) {
            searchParameters[0] = Report.MESSAGE;
            return reports.stream()
                    .filter(report -> report.getMessage().equals(searchParameters[1]))
                    .collect(Collectors.toList());
        }

        if (searchParameters[0].equals(context.getString(R.string.report_status))) {
            if (searchParameters[1].equals(context.getString(R.string.under_consideration))) {
                return reports.stream()
                        .filter(report -> !report.isReviewed())
                        .collect(Collectors.toList());
            }
            if (searchParameters[1].equals(context.getString(R.string.received))) {
                return reports.stream()
                        .filter(Report::isReceived)
                        .collect(Collectors.toList());
            }
            if (searchParameters[1].equals(context.getString(R.string.rejected))) {
                return reports.stream()
                        .filter(Report::isRejected)
                        .collect(Collectors.toList());
            }
        }

        return reports;
    }
}
