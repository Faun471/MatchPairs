package me.faun.matchpairs.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

public class MediaPlayerUtils {
    private static MediaPlayerUtils instance;
    private final MediaPlayer mediaPlayer;

    private MediaPlayerUtils(Context context) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setVolume(SettingsUtils.getInstance(context).getMusicVolume(), SettingsUtils.getInstance(context).getMusicVolume());
    }

    /**
     * Singleton pattern
     * @return instance
     */
    public static synchronized MediaPlayerUtils getInstance(Context context) {
        if (instance == null) {
            instance = new MediaPlayerUtils(context);
        }

        return instance;
    }

    /**
     * Play a sound.
     * @param context what activity should the sound be played in.
     * @param resourceId the sound to be played.
     */
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

    /**
     * Pause the music.
     */
    public void pauseMusic() {
        mediaPlayer.pause();
    }

    /**
     * Resume the music.
     */
    public void resumeMusic() {
        mediaPlayer.start();
    }

    /**
     * Stop the music.
     */
    public void stopMusic() {
        mediaPlayer.stop();
    }

    /**
     * Check if the music is playing.
     * @return true if the music is playing, false otherwise.
     */
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    /**
     * Release the media player.
     */
    public void release() {
        mediaPlayer.release();
    }

    /**
     *  Seek to a specific position.
     *  @param position the position to seek to.
     */
    public void seekTo(int position) {
        mediaPlayer.seekTo(position);
    }

    /**
     * Get the current position of the music.
     * @return the current position of the music.
     */
    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    /**
     * Get the duration of the music.
     * @return the duration of the music.
     */
    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    /**
     * Set the volume of the music.
     * @param volume the volume of the music.
     */
    public void setVolume(float volume) {
        mediaPlayer.setVolume(volume, volume);
    }

    /**
     * Set the looping of the music.
     * @param looping the looping of the music.
     */
    public void setLooping(boolean looping) {
        mediaPlayer.setLooping(looping);
    }

    /**
     * Play a sound effect.
     * @param context what activity should the sound effect be played in.
     * @param resourceId the sound effect to be played.
     * @param volume the volume of the sound effect.
     */
    public static void playSoundEffect(Context context, int resourceId, float volume) {
        try {
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setVolume(volume, volume);
            mediaPlayer.setDataSource(context, Uri.parse("android.resource://" + context.getPackageName() + "/" + resourceId));
            mediaPlayer.setOnCompletionListener(MediaPlayer::release);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
