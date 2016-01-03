package com.github.iweinzierl.mpd.player;

import android.content.Context;
import android.widget.Toast;

import com.github.iweinzierl.mpd.R;
import com.github.iweinzierl.mpd.async.PlayerControlTask;

public class Player {

    private final Context context;

    public Player(Context context) {
        this.context = context;
    }

    public void play() {
        new PlayerControlTask(context) {
            @Override
            protected void onPostExecute(Status status) {
                super.onPostExecute(status);
                showNotification(getPlayNotification(status));
            }
        }.execute(PlayerControlTask.Command.PLAY);
    }

    public void next() {
        new PlayerControlTask(context) {
            @Override
            protected void onPostExecute(Status status) {
                super.onPostExecute(status);
                showNotification(getNextNotification(status));
            }
        }.execute(PlayerControlTask.Command.NEXT);
    }

    public void previous() {
        new PlayerControlTask(context) {
            @Override
            protected void onPostExecute(Status status) {
                super.onPostExecute(status);
                showNotification(getPreviousNotification(status));
            }
        }.execute(PlayerControlTask.Command.PREVIOUS);
    }

    public void stop() {
        new PlayerControlTask(context) {
            @Override
            protected void onPostExecute(Status status) {
                super.onPostExecute(status);
                showNotification(getStopNotification(status));
            }
        }.execute(PlayerControlTask.Command.STOP);
    }

    private void showNotification(String message) {
        Toast.makeText(
                context,
                message,
                Toast.LENGTH_SHORT).show();
    }

    private String getPlayNotification(PlayerControlTask.Status status) {
        if (status == PlayerControlTask.Status.OK) {
            return context.getString(R.string.player_notification_play_successful);
        } else {
            return context.getString(R.string.player_notification_play_failed);
        }
    }

    private String getNextNotification(PlayerControlTask.Status status) {
        if (status == PlayerControlTask.Status.OK) {
            return context.getString(R.string.player_notification_next_successful);
        } else {
            return context.getString(R.string.player_notification_next_failed);
        }
    }

    private String getPreviousNotification(PlayerControlTask.Status status) {
        if (status == PlayerControlTask.Status.OK) {
            return context.getString(R.string.player_notification_previous_successful);
        } else {
            return context.getString(R.string.player_notification_previous_failed);
        }
    }

    private String getStopNotification(PlayerControlTask.Status status) {
        if (status == PlayerControlTask.Status.OK) {
            return context.getString(R.string.player_notification_stop_successful);
        } else {
            return context.getString(R.string.player_notification_stop_failed);
        }
    }
}
