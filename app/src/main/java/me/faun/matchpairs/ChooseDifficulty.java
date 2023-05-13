package me.faun.matchpairs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class ChooseDifficulty extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_difficulty);
    }

    public void chooseDifficulty(View view) {
      if (!(view instanceof ImageView level) {
        return;
      }
      
      Intent intent = new Intent(this, GameTime.class);
      
      intent.putExtra("row", 
        switch (level.getText().toLowerCase()) {
          case "easy" -> 2;
          case "medium" -> 4;
          case "hard" -> 5;
          default -> 3;
        });
      
      intent.putExtra("column", 
        switch (level.getText().toLowerCase()) {
          case "easy" -> 2;
          case "medium" -> 4;
          case "hard" -> 5;
          default -> 3;
        });
      
      startActivity(intent);
    }
}