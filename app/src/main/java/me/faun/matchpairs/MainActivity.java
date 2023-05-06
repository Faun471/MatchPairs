package me.faun.matchpairs;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import me.faun.matchpairs.customviews.IgasPlayingCard;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private final ArrayList<IgasPlayingCard> clickedCards = new ArrayList<>();
    private boolean isDelay = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] cards = new String[] {
                "card_1", "card_1",
                "card_2", "card_2",
                "card_3", "card_3",
                "card_4", "card_4",
                "card_5", "card_5",
        };

        // randomize order of cards
        for (int i = 0; i < cards.length; i++) {
            int randomIndex = (new Random().nextInt(cards.length));
            String temp = cards[i];
            cards[i] = cards[randomIndex];
            cards[randomIndex] = temp;
        }

        // get the parent view
        if (!(findViewById(R.id.parent) instanceof ViewGroup parent)) {
            return;
        }

        // set the name of each card and add a click listener
        for (int i = 0; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            if (view instanceof IgasPlayingCard card) {
                card.setName(cards[i]);
                card.setOnClickListener(this::onClick);
            }
        }
    }

    public void onClick(View view) {
        //Check if view is being delayed
        if (isDelay) {
            return;
        }

        // Check if the view is a IgasPlayingCard
        if (!(view instanceof IgasPlayingCard card)) {
            return;
        }

        Toast.makeText(this, card.getName(), Toast.LENGTH_SHORT).show();

        // Add the clicked card to the list
        if (clickedCards.size() <= 1) {
            clickedCards.add(card);
            card.toggle();
        }

        // Check if two cards have been clicked
        if (clickedCards.size() != 2) {
            return;
        }

        // Check if the same card has been clicked twice
        if (card.equals(clickedCards.get(0))) {
            clickedCards.clear();
            return;
        }

        // Check if the two clicked cards match
        if (!clickedCards.get(0).getName().equals(clickedCards.get(1).getName())) {
            Toast.makeText(this, "No match!", Toast.LENGTH_SHORT).show();
            isDelay = true;

            view.postDelayed(() -> {
                clickedCards.forEach(IgasPlayingCard::toggle);
                clickedCards.clear();
                isDelay = false;
            }, 1500);

            return;
        }

        Toast.makeText(this, "Match!", Toast.LENGTH_SHORT).show();
        clickedCards.forEach(playingCard -> playingCard.setClickable(false));
        clickedCards.clear();
    }
}