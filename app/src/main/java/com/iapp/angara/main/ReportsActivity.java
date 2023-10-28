package com.iapp.angara.main;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.iapp.angara.R;
import com.iapp.angara.database.Account;
import com.iapp.angara.database.Report;
import com.iapp.angara.ui.ElementAdapter;
import com.iapp.angara.ui.OnActionListener;
import com.iapp.angara.ui.OnChangeElement;
import com.iapp.angara.util.SearchUtil;
import com.iapp.angara.util.Constants;

import java.util.List;

public class ReportsActivity extends AppCompatActivity {

    private RelativeLayout reportsLayout;
    private RelativeLayout loadingLayout;
    private RelativeLayout header;

    private ListView reportsView;
    private EditText searchField;
    private TextInputLayout searchInput;
    private FloatingActionButton searchButton;

    private String request;
    private boolean headerRemoved;
    private int index, top;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_reports);

        initGraphics();
        initData();
    }

    public void goToChat(View view) {
        Constants.soundPlayer.getClick().play();
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
    }

    public void search(View view) {
        Constants.soundPlayer.getClick().play();
        request = searchField.getText().toString();
        initData();
    }

    private void showDialogConfirmation(Report report, boolean accept) {
        String confirmation;
        if (accept) {
            confirmation = getString(R.string.approve_report);
        } else {
            confirmation = getString(R.string.dismiss_report);
        }

        AlertDialog confirmDialog =
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.confirmation))
                        .setMessage(confirmation)
                        .setCancelable(true)
                        .setNegativeButton(getString(R.string.cancel), (dialog, which) -> {})
                        .setPositiveButton(getString(R.string.accept), (dialog, which) -> {
                            report.setReviewed(true);
                            if (accept) {
                                report.setReceived(true);
                                report.setRejected(false);
                            } else {
                                report.setReceived(false);
                                report.setRejected(true);
                            }
                            Constants.firebaseController.updateReport(report);
                        })
                        .create();
        confirmDialog.show();
    }

    private void initData() {
        OnActionListener listenerReports = () -> {

            if (Constants.firebaseController.getUser().isModerator() && !headerRemoved) {
                header.addView(searchInput);
                header.addView(searchButton);
                headerRemoved = true;
            }

            OnChangeElement<Report> onChange = (view, report) -> {

                saveScrollPosition();

                TextView contentReportId = view.findViewById(R.id.content_report_id),
                        senderId = view.findViewById(R.id.content_sender_id),
                        senderName = view.findViewById(R.id.content_sender_name),
                        guiltyId = view.findViewById(R.id.content_guilty_id),
                        guiltyName = view.findViewById(R.id.content_guilty_name),
                        cause = view.findViewById(R.id.content_cause),
                        message = view.findViewById(R.id.content_message),
                        status = view.findViewById(R.id.content_status);
                Button accept = view.findViewById(R.id.accept),
                        reject = view.findViewById(R.id.reject);

                contentReportId.setText(String.valueOf(report.getId()));
                senderId.setText(String.valueOf(report.getSenderId()));
                senderName.setText(report.getSenderName());
                guiltyId.setText(String.valueOf(report.getGuiltyId()));
                guiltyName.setText(report.getGuiltyName());
                cause.setText(report.getCause());
                message.setText(report.getMessage());

                if (!report.isReviewed()) {
                    status.setText(getString(R.string.under_consideration));
                    status.setTextColor(Color.YELLOW);
                } else if (report.isReceived()) {
                    status.setText(getString(R.string.received));
                    status.setTextColor(Color.GREEN);
                } else if (report.isRejected()) {
                    status.setText(getString(R.string.rejected));
                    status.setTextColor(Color.RED);
                }

                if (accept != null && reject != null) {
                    accept.setOnClickListener(v1 -> showDialogConfirmation(report, true));
                    reject.setOnClickListener(v1 -> showDialogConfirmation(report, false));
                }
            };

            ElementAdapter<Report> adapter = new ElementAdapter<>(this,
                    Constants.firebaseController.getUser().isModerator() ? R.layout.list_item_mod_reports : R.layout.list_item_reports, onChange);
            reportsView.setAdapter(adapter);

            Constants.firebaseController.setOnUpdateReports(reports -> {
                adapter.clear();
                updateReports(adapter, reports);

                Runnable task = () -> runOnUiThread(() -> reportsView.setSelectionFromTop(index, top));
                Constants.getThreadPool().execute(task);
            });
            Constants.loading.showWaiting(this,
                    loadingLayout, () -> updateReports(adapter, Constants.firebaseController.getReports()), false);
        };

        Constants.loading.showWaitingAccounts(this, loadingLayout, listenerReports, true);
    }

    private void saveScrollPosition() {
        index = reportsView.getFirstVisiblePosition() + 1;
        view = reportsView.getChildAt(0);
        top = (view == null) ? 0 : (view.getTop() - reportsView.getPaddingTop());
    }

    private void updateReports(ElementAdapter<Report> adapter, List<Report> reports) {
        if (!Constants.firebaseController.getUser().isModerator()) {

            reports = SearchUtil.parseReportSearchParam(this,
                    getString(R.string.sender_id) + Constants.firebaseController.getUser().getId(), reports);
        }

        reports = SearchUtil.parseReportSearchParam(this, request, reports);
        for (Report report : reports) {
            adapter.add(report);
        }
    }

    private void initGraphics() {
        reportsLayout = findViewById(R.id.reports);
        reportsView = findViewById(R.id.list_of_reports);
        loadingLayout = findViewById(R.id.loading);
        searchField = findViewById(R.id.search_field);
        searchButton = findViewById(R.id.search_button);
        searchInput = findViewById(R.id.search_input);
        header = findViewById(R.id.header);

        header.removeView(searchButton);
        header.removeView(searchInput);
    }
}