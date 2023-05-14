package me.faun.matchpairs.utils;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import me.faun.matchpairs.activities.GameTime;
import me.faun.matchpairs.customviews.IgasPlayingCard;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CardUtils {

    /*
     * Assigns names to the cards.
     * The names are shuffled and then assigned to the cards in pairs.
     * The number of names must be equal to the number of cards.
     *
     * @param cards the cards to which the names will be assigned
     * @param names the names to be assigned to the cards
     */
    public static void giveNamesToCards(@NotNull ArrayList<IgasPlayingCard> cards, String[] names) {
        //shuffle the names
        names = StringUtils.shuffleArray(names);

        int nameIndex = 0;  // index of the current name in the sequence of pairs
        for (int i = 0; i < cards.size(); i += 2) {
            IgasPlayingCard card1 = cards.get(i);
            IgasPlayingCard card2 = cards.get(i + 1);
            String name = names[nameIndex];
            card1.setName(name);
            card1.setFrontCard(name);
            card2.setName(name);
            card2.setFrontCard(name);
            nameIndex = (nameIndex + 1) % names.length;  // move to the next name in the sequence
        }
    }

    /*
     * This method will add cards to a grid layout.
     * The number of cards added will be the number of columns times the number of rows.
     *
     * @param The GameTime class
     * @param gridLayout the grid layout to which the cards will be added
     * @param onClick the click listener to be added to the cards
     */
    public static void addCardsToGrid(@NotNull GameTime gameTime, @NotNull GridLayout gridLayout, View.OnClickListener onClick) {
        int rowCount = gameTime.getIntent().getIntExtra("row", 3);
        int columnCount = gameTime.getIntent().getIntExtra("column", 3);

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
            int margin = 8;
            int cellSize = (Math.min(width / columnCount, height / rowCount) - 2 * margin);

            // Add the cards to the grid
            for (int i = 0; i < finalTotalCards; i++) {
                IgasPlayingCard card = new IgasPlayingCard(gameTime);

                // Set the card's gravity, and background color
                card.setGravity(Gravity.CENTER);
                card.setBackgroundColor(Color.WHITE);

                // Set the card's layout parameters with margins
                GridLayout.LayoutParams params = new GridLayout.LayoutParams(new ViewGroup.LayoutParams(cellSize, cellSize));
                params.setMargins(margin,  margin, margin, margin);
                card.setLayoutParams(params);

                // Add a click listener to the card
                card.setOnClickListener(onClick);

                // Add the card to the grid layout
                gridLayout.addView(card);
            }
        });
    }
}
