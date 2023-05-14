package me.faun.matchpairs.activities;

import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import me.faun.matchpairs.R;
import me.faun.matchpairs.utils.MediaPlayerUtils;
import me.faun.matchpairs.utils.ViewUtils;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
    }

    public void onPlay(View view) {
        MediaPlayer.create(this, R.raw.menu_click).start();
        ViewUtils.animateBounce(view, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                Intent intent = new Intent(HomePage.this, ChooseDifficulty.class);
                startActivity(intent);
            }
        });
    }

    public void onQuit(View view) {
        MediaPlayer.create(this, R.raw.menu_click).start();
        ViewUtils.animateBounce(view, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                finish();
            }
        });
    }

    public void onSettings(View view) {
        MediaPlayer.create(this, R.raw.menu_click).start();
        ViewUtils.animateBounce(view);
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

        MediaPlayerUtils.getInstance().resumeMusic();

        if (MediaPlayerUtils.getInstance().isPlaying()) {
            return;
        }

        MediaPlayerUtils.getInstance().playMusic(this, R.raw.home_page_music);
        MediaPlayerUtils.getInstance().setLooping(true);
    }

    /*
     * onPause() is called when the activity is going to the background.
     *
     * We want to pause the music when the activity is going to the background.
     */
    @Override
    protected void onPause() {
        super.onPause();
        MediaPlayerUtils.getInstance().pauseMusic();
    }
}