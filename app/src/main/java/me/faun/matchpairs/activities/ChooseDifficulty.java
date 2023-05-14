package me.faun.matchpairs.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import me.faun.matchpairs.R;
import me.faun.matchpairs.utils.MediaPlayerUtils;
import me.faun.matchpairs.utils.ViewUtils;

public class ChooseDifficulty extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_difficulty);
    }

    public void chooseDifficulty(View view) {
        if (!(view instanceof ImageView level)) {
            return;
        }

        MediaPlayer.create(this, R.raw.menu_click).start();
        ViewUtils.animateBounce(view);

        Intent intent = new Intent(this, GameTime.class);

        intent.putExtra("row",
                switch (level.getId()) {
                    case R.id.easy -> 2;
                    case R.id.medium -> 4;
                    case R.id.hard -> 6;
                    default -> 3;
                });

        intent.putExtra("column",
                switch (level.getId()) {
                    case R.id.easy -> 2;
                    case R.id.medium -> 4;
                    case R.id.hard -> 6;
                    default -> 3;
                });

        startActivity(intent);
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

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (MediaPlayerUtils.getInstance().isPlaying()) {
                return;
            }

            MediaPlayerUtils.getInstance().playMusic(this, R.raw.home_page_music);
            MediaPlayerUtils.getInstance().setLooping(true);
        }, 500);
    }

    /*
     * onPause() is called when the activity is going to the background.
     *
     * We want to pause the music when the activity is going to the background.
     */
    @Override
    protected void onPause() {
        MediaPlayerUtils.getInstance().pauseMusic();
        super.onPause();
    }
}