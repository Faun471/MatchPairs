package me.faun.matchpairs.activities;

import android.content.Intent;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import me.faun.matchpairs.R;

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

        Intent intent = new Intent(this, GameTime.class);

        intent.putExtra("row",
                switch (level.getId()) {
                    case R.id.easy -> 2;
                    case R.id.medium -> 4;
                    case R.id.hard -> 5;
                    default -> 3;
                });

        intent.putExtra("column",
                switch (level.getId()) {
                    case R.id.easy -> 2;
                    case R.id.medium -> 4;
                    case R.id.hard -> 5;
                    default -> 3;
                });

        startActivity(intent);
    }
}