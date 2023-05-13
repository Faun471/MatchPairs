package me.faun.matchpairs.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.FragmentTransaction;
import me.faun.matchpairs.R;
import me.faun.matchpairs.SettingsFragment;

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
}