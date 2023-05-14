package me.faun.matchpairs.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

public class MediaPlayerUtils {

    private static MediaPlayerUtils instance;
    private final MediaPlayer mediaPlayer;

    private MediaPlayerUtils() {
        mediaPlayer = new MediaPlayer();
    }

    public static synchronized MediaPlayerUtils getInstance() {
        if (instance == null) {
            instance = new MediaPlayerUtils();
        }
        return instance;
    }

    public void playMusic(Context context, int resourceId) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(context, Uri.parse("android.resource://" + context.getPackageName() + "/" + resourceId));
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pauseMusic() {
        mediaPlayer.pause();
    }

    public void resumeMusic() {
        mediaPlayer.start();
    }

    public void stopMusic() {
        mediaPlayer.stop();
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void release() {
        mediaPlayer.release();
    }

    public void seekTo(int position) {
        mediaPlayer.seekTo(position);
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    //loop music
    public void setLooping(boolean looping) {
        mediaPlayer.setLooping(looping);
    }
}
