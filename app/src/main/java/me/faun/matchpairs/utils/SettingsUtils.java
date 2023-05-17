package me.faun.matchpairs.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsUtils {
    private static final String PREFS_NAME = "AppSettings";
    private static final String MUSIC_VOLUME_KEY = "music_volume";
    private static final String SOUND_EFFECTS_VOLUME_KEY = "sound_effects_volume";

    private static SettingsUtils instance;
    private float musicVolume, soundEffectsVolume;
    private final SharedPreferences sharedPreferences;

    private SettingsUtils(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        musicVolume = sharedPreferences.getFloat(MUSIC_VOLUME_KEY, 1f);
        soundEffectsVolume = sharedPreferences.getFloat(SOUND_EFFECTS_VOLUME_KEY, 1f);
    }

    public static synchronized SettingsUtils getInstance(Context context) {
        if (instance == null) {
            instance = new SettingsUtils(context);
        }
        return instance;
    }

    public void setMusicVolume(Context context, float musicVolume) {
        this.musicVolume = musicVolume;
        MediaPlayerUtils.getInstance(context).setVolume(musicVolume);
        saveSettings(context);
    }

    public void setSoundEffectsVolume(Context context, float soundEffectsVolume) {
        this.soundEffectsVolume = soundEffectsVolume;
        saveSettings(context);
    }

    public void setMusicMute(Context context, boolean mute) {
        setMusicVolume(context, mute ? 0f : 1f);
    }

    public void setSoundEffectsMute(Context context, boolean mute) {
        this.soundEffectsVolume = mute ? 0f : 1f;
        saveSettings(context);
    }

    public float getMusicVolume() {
        return musicVolume;
    }

    public float getSoundEffectsVolume() {
        return soundEffectsVolume;
    }

    private void saveSettings(Context context) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(MUSIC_VOLUME_KEY, musicVolume);
        editor.putFloat(SOUND_EFFECTS_VOLUME_KEY, soundEffectsVolume);
        editor.apply();
    }
}
