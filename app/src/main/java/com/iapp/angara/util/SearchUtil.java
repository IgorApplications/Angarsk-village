package com.iapp.angara.util;

import android.content.Context;

import com.iapp.angara.R;
import com.iapp.angara.database.Account;
import com.iapp.angara.database.Report;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Igor Ivanov
 * @version 1.0
 * Utility class for querying and searching
 * reports and accounts
 * */
public final class SearchUtil {

    /**
     * RegEx delimiter "parameter : value"
     * Does not depend on spaces
     * */
    private static final String REGEX_SEPARATOR = "\\s*:\\s*";

    /**
     * Methods for executing search
     * @param context from which the method is invoked
     * @param request in format "parameter : value"
     * parameter: R.string.identifier,
     *            R.string.name,
     *            R.string.email,
     *            R.string.muted,
     *            R.string.banned,
     *            R.string.moderator,
     *            R.string.reputation
     * value    : any
     * @param accounts is search data storage
     * @return results matching query
     * If request is not valid, then the
     * link to the data storage
     * */
    public static List<Account> parseAccountSearchParam(Context context, String request, List<Account> accounts) {
        if (request == null) return accounts;
        String[] searchParameters = request.split(REGEX_SEPARATOR);
        if (searchParameters.length < 2) return accounts;

        searchParameters[0] = searchParameters[0] + ":";
        if (searchParameters[0].equals(context.getString(R.string.identifier))) {
            searchParameters[0] = Account.ID;
            return filter(accounts, account -> account.getId() == Long.parseLong(searchParameters[1]));

        }

        if (searchParameters[0].equals(context.getString(R.string.name))) {
            searchParameters[0] = Account.NAME;
            return filter(accounts, account -> account.getName().equals(searchParameters[1]));

        }

        if (searchParameters[0].equals(context.getString(R.string.email))) {
            searchParameters[0] = Account.EMAIL;
            return filter(accounts, account -> account.getEmail().equals(searchParameters[1]));

        }

        if (searchParameters[0].equals(context.getString(R.string.muted))) {
            searchParameters[0] = Account.MUTED;
            return filter(accounts, account -> account.isMuted() == Boolean.parseBoolean(searchParameters[1]));

        }

        if (searchParameters[0].equals(context.getString(R.string.banned))) {
            searchParameters[0] = Account.BANNED;
            return filter(accounts, account -> account.isBanned() == Boolean.parseBoolean(searchParameters[1]));

        }

        if (searchParameters[0].equals(context.getString(R.string.moderator))) {
            searchParameters[0] = Account.MODERATOR;
            return filter(accounts, account -> account.isModerator() == Boolean.parseBoolean(searchParameters[1]));

        }

        if (searchParameters[0].equals(context.getString(R.string.reputation))) {
            searchParameters[0] = Account.REPUTATION;
            return filter(accounts, account -> account.getReputation().equals(searchParameters[1]));

        }

        return accounts;
    }

    /**
     * Methods for executing search
     * @param context from which the method is invoked
     * @param request in format "parameter : value"
     * parameter: R.string.identifier,
     *            R.string.sender_id,
     *            R.string.sender_name,
     *            R.string.guilty_id,
     *            R.string.guilty_name,
     *            R.string.cause,
     *            R.string.message,
     *            R.string.report_status,
     *            R.string.report_status
     *
     * R.string.report_status : R.string.under_consideration,
     *                          R.string.received,
     *                          R.string.rejected
     * value                   : any
     * @param reports is search data storage
     * @return results matching query
     * If request is not valid, then the
     * link to the data storage
     * */
    public static List<Report> parseReportSearchParam(Context context, String request, List<Report> reports) {
        if (request == null) return reports;
        String[] searchParameters = request.split(REGEX_SEPARATOR);
        if (searchParameters.length < 2) return reports;

        searchParameters[0] = searchParameters[0] + ":";
        if (searchParameters[0].equals(context.getString(R.string.identifier))) {
            searchParameters[0] = Report.ID;

            return filter(reports, report -> report.getId() == Long.parseLong(searchParameters[1]));

        }

        if (searchParameters[0].equals(context.getString(R.string.sender_id))) {
            searchParameters[0] = Report.SENDER_ID;

            return filter(reports, report -> report.getSenderId() == Long.parseLong(searchParameters[1]));

        }

        if (searchParameters[0].equals(context.getString(R.string.sender_name))) {
            searchParameters[0] = Report.SENDER_NAME;

            return filter(reports, report -> report.getSenderName().equals(searchParameters[1]));

        }

        if (searchParameters[0].equals(context.getString(R.string.guilty_id))) {
            searchParameters[0] = Report.GUILTY_ID;

            return filter(reports, report -> report.getGuiltyId() == Long.parseLong(searchParameters[1]));

        }

        if (searchParameters[0].equals(context.getString(R.string.guilty_name))) {
            searchParameters[0] = Report.GUILTY_NAME;

            return filter(reports, report -> report.getGuiltyName().equals(searchParameters[1]));

        }

        if (searchParameters[0].equals(context.getString(R.string.cause))) {
            searchParameters[0] = Report.CAUSE;

            return filter(reports, report -> report.getCause().equals(searchParameters[1]));

        }

        if (searchParameters[0].equals(context.getString(R.string.message))) {
            searchParameters[0] = Report.MESSAGE;

            return filter(reports, report -> report.getMessage().equals(searchParameters[1]));

        }

        if (searchParameters[0].equals(context.getString(R.string.report_status))) {
            if (searchParameters[1].equals(context.getString(R.string.under_consideration))) {

                return filter(reports, report -> !report.isReviewed());

            }
            if (searchParameters[1].equals(context.getString(R.string.received))) {
                return filter(reports, Report::isReceived);

            }
            if (searchParameters[1].equals(context.getString(R.string.rejected))) {
                return filter(reports, Report::isRejected);

            }
        }

        return reports;
    }

    private static <E> List<E> filter(List<E> source, Filter<E> filter) {
        List<E> newList = new ArrayList<>();
        for (E e : source) {
            if (filter.test(e)) {
                newList.add(e);
            }
        }
        return newList;
    }
}
