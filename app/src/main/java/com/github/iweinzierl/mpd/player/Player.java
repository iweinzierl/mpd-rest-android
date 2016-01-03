package com.github.iweinzierl.mpd.player;

import android.content.Context;
import android.widget.Toast;

import com.github.iweinzierl.mpd.R;
import com.github.iweinzierl.mpd.async.PlayerControlTask;

import java.util.HashSet;
import java.util.Set;

public class Player {

    public interface PlayerListener {
        void onPlayerPaused();

        void onPlayerStarted();

        void onPlayerStopped();

        void onJumpedToPreviousSong();

        void onJumpedToNextSong();
    }

    private final Context context;

    private final Set<PlayerListener> playerListeners;

    public Player(Context context) {
        this.context = context;
        playerListeners = new HashSet<>();
    }

    public void play() {
        new PlayerControlTask(context) {
            @Override
            protected void onPostExecute(Status status) {
                super.onPostExecute(status);
                showNotification(getPlayNotification(status));
                notifyPlayerStarted();
            }
        }.execute(PlayerControlTask.Command.PLAY);
    }

    public void pause() {
        new PlayerControlTask(context) {
            @Override
            protected void onPostExecute(Status status) {
                super.onPostExecute(status);
                showNotification(getPauseNotification(status));
                notifyPlayerPaused();
            }
        }.execute(PlayerControlTask.Command.PAUSE);
    }

    public void next() {
        new PlayerControlTask(context) {
            @Override
            protected void onPostExecute(Status status) {
                super.onPostExecute(status);
                showNotification(getNextNotification(status));
                notifyJumpedToNextSong();
            }
        }.execute(PlayerControlTask.Command.NEXT);
    }

    public void previous() {
        new PlayerControlTask(context) {
            @Override
            protected void onPostExecute(Status status) {
                super.onPostExecute(status);
                showNotification(getPreviousNotification(status));
                notifyJumpedToPreviousSong();
            }
        }.execute(PlayerControlTask.Command.PREVIOUS);
    }

    public void stop() {
        new PlayerControlTask(context) {
            @Override
            protected void onPostExecute(Status status) {
                super.onPostExecute(status);
                showNotification(getStopNotification(status));
                notifyPlayerStopped();
            }
        }.execute(PlayerControlTask.Command.STOP);
    }

    public void addPlayerListener(PlayerListener playerListener) {
        playerListeners.add(playerListener);
    }

    private void notifyPlayerStarted() {
        for (PlayerListener playerListener : playerListeners) {
            playerListener.onPlayerStarted();
        }
    }

    private void notifyPlayerPaused() {
        for (PlayerListener playerListener : playerListeners) {
            playerListener.onPlayerPaused();
        }
    }

    private void notifyJumpedToPreviousSong() {
        for (PlayerListener playerListener : playerListeners) {
            playerListener.onJumpedToPreviousSong();
        }
    }

    private void notifyJumpedToNextSong() {
        for (PlayerListener playerListener : playerListeners) {
            playerListener.onJumpedToNextSong();
        }
    }

    private void notifyPlayerStopped() {
        for (PlayerListener playerListener : playerListeners) {
            playerListener.onPlayerStopped();
        }
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

    private String getPauseNotification(PlayerControlTask.Status status) {
        if (status == PlayerControlTask.Status.OK) {
            return context.getString(R.string.player_notification_pause_successful);
        } else {
            return context.getString(R.string.player_notification_pause_failed);
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
