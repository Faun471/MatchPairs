package me.faun.matchpairs;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import me.faun.matchpairs.customviews.IgasPlayingCard;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final ArrayList<IgasPlayingCard> clickedCards = new ArrayList<>();
    private boolean isFlipping = false;
    private GridLayout gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridLayout = findViewById(R.id.gridLayout);

        addCardsToGrid(3, 3);

        gridLayout.post(this::showCards);
    }

    /*
     * This method will show all cards for 1.5 seconds.
     * During this time, the user can memorize the cards.
     * Additionally, the user will not be able to click on any card.
     * After that time, all cards will be flipped back.
     */
    public void showCards() {
        // get all cards from the grid layout
        ArrayList<IgasPlayingCard> cards = new ArrayList<>();
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View view = gridLayout.getChildAt(i);
            if (view instanceof IgasPlayingCard card) {
                cards.add(card);
            }
        }

        // flip all cards
        cards.forEach(card -> {
            card.flip();
            isFlipping = true;

            card.postDelayed(() -> {
                card.flip();
                isFlipping = false;
            }, 1500);
        });
    }

    /*
     * This method will add cards to a grid layout.
     * The number of cards added will be the number of columns times the number of rows.
     */
    public void addCardsToGrid(int columnCount, int rowCount) {
        // Set the number of columns and rows
        gridLayout.setColumnCount(columnCount);
        gridLayout.setRowCount(rowCount);

        // Calculate the total number of cards to be added
        int totalCards = columnCount * rowCount;

        // If total number of cards is odd, subtract one
        if (totalCards % 2 != 0) {
            totalCards -= 1;
        }

        // make sure that the code runs post layout
        int finalTotalCards = totalCards;

        gridLayout.post(() -> {
            int width = gridLayout.getWidth() - gridLayout.getPaddingLeft() - gridLayout.getPaddingRight();
            int height = gridLayout.getHeight() - gridLayout.getPaddingTop() - gridLayout.getPaddingBottom();
            int cellSize = Math.min(width / columnCount, height / rowCount);
            int margin = 8;

            // Calculate the actual cell size including margin
            int actualCellSize = cellSize - 2 * margin;

            // Add the cards to the grid
            for (int i = 0; i < finalTotalCards; i++) {
                int row = i / columnCount;
                int column = i % columnCount;
                IgasPlayingCard card = new IgasPlayingCard(this);

                // Set the card's name, gravity and background color
                card.setName((row + 1) + ", " + (column + 1));
                card.setGravity(Gravity.CENTER);
                card.setBackgroundColor(Color.WHITE);

                // Set the card's layout parameters with margins
                GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                        new ViewGroup.LayoutParams(actualCellSize, actualCellSize));
                params.setMargins(margin, margin, margin, margin);
                card.setLayoutParams(params);

                // Add a click listener to the card
                card.setOnClickListener(this::onClick);

                // Add the card to the grid layout
                gridLayout.addView(card);
            }
        });
    }

    public void onClick(View view) {
        //Check if view is being delayed
        if (isFlipping) {
            return;
        }

        // Check if the view is a IgasPlayingCard
        if (!(view instanceof IgasPlayingCard card)) {
            return;
        }

        Toast.makeText(this, card.getName(), Toast.LENGTH_SHORT).show();

        // Add the clicked card to the list if it's not already in it
        if (clickedCards.size() <= 1) {
            clickedCards.add(card);
            card.flip();
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

        // You have now clicked two cards that aren't the same at this point.
        // Check if the two clicked cards don't match
        if (!clickedCards.get(0).getName().equals(clickedCards.get(1).getName())) {
            Toast.makeText(this, "No match!", Toast.LENGTH_SHORT).show();
            isFlipping = true;

            // Delay the cards from flipping back
            view.postDelayed(() -> {
                clickedCards.forEach(IgasPlayingCard::flip);
                clickedCards.clear();
                isFlipping = false;
            }, 1500);

            return;
        }

        Toast.makeText(this, "Match!", Toast.LENGTH_SHORT).show();

        clickedCards.forEach(playingCard -> playingCard.setClickable(false));
        clickedCards.clear();
    }
}