package me.faun.matchpairs.activities;

import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import me.faun.matchpairs.R;
import me.faun.matchpairs.fragments.Settings;
import me.faun.matchpairs.utils.MediaPlayerUtils;
import me.faun.matchpairs.utils.SettingsUtils;
import me.faun.matchpairs.utils.ViewUtils;

public class HomePage extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
    }

    public void onPlay(View view) {
        MediaPlayerUtils.playSoundEffect(this, R.raw.menu_click, SettingsUtils.getInstance(this).getSoundEffectsVolume());
        ViewUtils.animateBounce(view, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                Intent intent = new Intent(HomePage.this, ChooseDifficulty.class);
                startActivity(intent);
            }
        });
    }

    public void onQuit(View view) {
        MediaPlayerUtils.playSoundEffect(this, R.raw.menu_click, SettingsUtils.getInstance(this).getSoundEffectsVolume());
        ViewUtils.animateBounce(view, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                finishAffinity();
            }
        });
    }

    public void onSettings(View view) {
        MediaPlayerUtils.playSoundEffect(this, R.raw.menu_click, SettingsUtils.getInstance(this).getSoundEffectsVolume());
        ViewUtils.animateBounce(view);

        new Settings().show(getSupportFragmentManager(), "Settings");
    }

    /*
     * onResume() is called when the activity is going to the foreground.
     *
     * We want to resume the music when the activity is going to the foreground.
     * We also want to play the music if it is not playing.
     */
    @Override
    protected void onResume() {
        super.onResume();

        MediaPlayerUtils.getInstance(this).resumeMusic();

        if (MediaPlayerUtils.getInstance(this).isPlaying()) {
            return;
        }

        MediaPlayerUtils.getInstance(this).playMusic(this, R.raw.home_page_music);
        MediaPlayerUtils.getInstance(this).setLooping(true);
    }

    /*
     * onPause() is called when the activity is going to the background.
     *
     * We want to pause the music when the activity is going to the background.
     */
    @Override
    protected void onPause() {
        super.onPause();
        MediaPlayerUtils.getInstance(this).pauseMusic();
    }
}