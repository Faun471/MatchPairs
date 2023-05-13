package me.faun.matchpairs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
}