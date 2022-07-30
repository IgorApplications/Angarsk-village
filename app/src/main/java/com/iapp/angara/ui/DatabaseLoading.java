package com.iapp.angara.ui;

import android.app.Activity;
import android.view.View;

import com.iapp.angara.util.Settings;

public class DatabaseLoading {


    public void showWaiting(Activity activity, View loading,
                            OnActionListener onActionListener, boolean endVisible) {
        Runnable task = () -> {
            activity.runOnUiThread(() -> loading.setVisibility(View.VISIBLE));

            while (!Settings.firebaseController.isReady()) {
                Thread.yield();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            activity.runOnUiThread(() -> {
                onActionListener.onAction();
                if (!endVisible) loading.setVisibility(View.INVISIBLE);
            });
        };
        Settings.threadPool.execute(task);
    }

    public void showWaitingAccounts(Activity activity, View loading,
                                    OnActionListener onActionListener, boolean endVisible) {
        Runnable task = () -> {
            activity.runOnUiThread(() -> loading.setVisibility(View.VISIBLE));

            while (!Settings.firebaseController.isReadyAccounts()) {
                Thread.yield();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            activity.runOnUiThread(() -> {
                onActionListener.onAction();
                if (!endVisible) loading.setVisibility(View.INVISIBLE);
            });
        };
        Settings.threadPool.execute(task);
    }

    public void showWaitingMessages(Activity activity, View loading,
                                    OnActionListener onClickListener, boolean endVisible) {
        Runnable task = () -> {
            activity.runOnUiThread(() -> loading.setVisibility(View.VISIBLE));

            while (!Settings.firebaseController.isReadyMessages()) {
                Thread.yield();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            activity.runOnUiThread(() -> {
                onClickListener.onAction();
                if (!endVisible) loading.setVisibility(View.INVISIBLE);
            });
        };
        Settings.threadPool.execute(task);
    }

    public void showWaitingReports(Activity activity, View loading,
                                   OnActionListener onClickListener, boolean endVisible) {
        Runnable task = () -> {
            activity.runOnUiThread(() -> loading.setVisibility(View.VISIBLE));

            while (!Settings.firebaseController.isReadyReports()) {
                Thread.yield();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            activity.runOnUiThread(() -> {
                onClickListener.onAction();
                if (!endVisible) loading.setVisibility(View.INVISIBLE);
            });
        };
        Settings.threadPool.execute(task);
    }
}
