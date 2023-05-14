package me.faun.matchpairs.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import me.faun.matchpairs.R;
import me.faun.matchpairs.SettingsFragment;
import me.faun.matchpairs.utils.MediaPlayerUtils;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
    }

    public void onPlay(View view) {
        Intent intent = new Intent(this, ChooseDifficulty.class);
        startActivity(intent);
    }

    public void onQuit(View view) {
        finish();
    }

    public void onSettings(View view) {
        SettingsFragment menuFragment = new SettingsFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frameLayout, menuFragment);
        transaction.addToBackStack(null);
        transaction.commit();
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