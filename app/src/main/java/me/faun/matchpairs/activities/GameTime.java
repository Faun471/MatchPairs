package me.faun.matchpairs.activities;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.GridLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import me.faun.matchpairs.R;
import me.faun.matchpairs.customviews.IgasPlayingCard;
import me.faun.matchpairs.managers.LeaderboardManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GameTime extends AppCompatActivity {
    private final ArrayList<IgasPlayingCard> clickedCards = new ArrayList<>();
    private boolean isRestarting = false, isFlipping = false;
    private GridLayout gridLayout;
    private final String[] names = {"abigail", "harvey", "elliott"};
    private LeaderboardManager leaderboardManager;
    private long elapsedTime = 0;
    private int clicks = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gridLayout = findViewById(R.id.gridLayout);
        leaderboardManager = new LeaderboardManager(this);

        addCardsToGrid(getIntent().getIntExtra("column", 3), getIntent().getIntExtra("row", 3));

        gridLayout.post(() -> {
            giveNamesToCards();
            shuffleGridLayout();
            showCards();
            //start timer
            new CountDownTimer(Long.MAX_VALUE, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    elapsedTime += 1000;
                }

                @Override
                public void onFinish() {
                    // Do nothing
                }

            }.start();
        });
    }

    private ArrayList<IgasPlayingCard> getCards() {
        ArrayList<IgasPlayingCard> cards = new ArrayList<>();
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View view = gridLayout.getChildAt(i);
            if (view instanceof IgasPlayingCard card) {
                cards.add(card);
            }
        }

        return cards;
    }

    /*
     * This will store all the cards in an ArrayList and shuffle it to change
     * the cards' order.
     */
    public void shuffleGridLayout() {
        // get all cards from the grid layout
        ArrayList<IgasPlayingCard> cards = getCards();
        gridLayout.removeAllViews();

        Collections.shuffle(cards);

        cards.forEach(card -> gridLayout.addView(card));
    }

    /*
     * This method will show all cards for 1.5 seconds.
     * During this time, the user can memorize the cards.
     * Additionally, the user will not be able to click on any card.
     * After that time, all cards will be flipped back.
     */
    public void showCards() {
        // get all cards from the grid layout
        ArrayList<IgasPlayingCard> cards = getCards();

        // flip all cards
        for (int i = 0; i < cards.size(); i++) {
            IgasPlayingCard card = cards.get(i);
            card.postDelayed(card::flip, i * 25L);
            isFlipping = true;
        }

        // wait 1.5 seconds
        gridLayout.postDelayed(() -> {
            // flip all cards back
            for (int i = cards.size() - 1; i >= 0; i--) {
                IgasPlayingCard card = cards.get(i);
                card.postDelayed(card::flip, (cards.size() - i) * 25L);
            }

            isFlipping = false;
        }, 2000L);
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
            int margin = 8;
            int cellSize = (Math.min(width / columnCount, height / rowCount) - 2 * margin);

            // Add the cards to the grid
            for (int i = 0; i < finalTotalCards; i++) {
                IgasPlayingCard card = new IgasPlayingCard(this);

                // Set the card's gravity, and background color
                card.setGravity(Gravity.CENTER);
                card.setBackgroundColor(Color.WHITE);

                // Set the card's layout parameters with margins
                GridLayout.LayoutParams params = new GridLayout.LayoutParams(new ViewGroup.LayoutParams(cellSize, cellSize));
                params.setMargins(margin,  margin, margin, margin);
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
        if (isFlipping || isRestarting) {
            return;
        }

        // Check if the view is a IgasPlayingCard
        if (!(view instanceof IgasPlayingCard card)) {
            return;
        }

        clicks++;

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
            isFlipping = true;

            // Delay the cards from flipping back
            view.postDelayed(() -> {
                clickedCards.forEach(IgasPlayingCard::flip);
                clickedCards.clear();
                isFlipping = false;
            }, 1250);

            return;
        }

        clickedCards.forEach(playingCard -> playingCard.setClickable(false));
        clickedCards.clear();

        // Check if all cards have been matched
        if (getCards().stream().noneMatch(View::isClickable)) {
            onGameComplete(calculateScore(elapsedTime, clicks, getCards().size()));
        }
    }

    // Method to randomly select a name from the cardNames array
    private void giveNamesToCards() {
        ArrayList<IgasPlayingCard> cards = getCards();

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

    public void onClickRestart(View view) {
        if (isRestarting || isFlipping) {
            return;
        }

        isRestarting = true;

        // get all cards from the grid layout and flip them to the back
        ArrayList<IgasPlayingCard> cards = getCards();
        cards.forEach(card -> {
            if (!card.isFlipped()) {
                return;
            }

            clickedCards.clear();
            card.setClickable(true);
            card.flip();
        });

        // calculate the center coordinates of the screen
        int centerX = gridLayout.getWidth() / 2;
        int centerY = gridLayout.getHeight() / 2;

        view.postDelayed(() -> {
            // animate each card to the center of the screen and back to its original position
            for (int i = 0; i < cards.size(); i++) {
                IgasPlayingCard card = cards.get(i);
                card.animate()
                        .translationX(centerX - card.getLeft() - card.getWidth() / 2)
                        .translationY(centerY - card.getTop() - card.getHeight() / 2)
                        .setStartDelay(i * 10L)
                        .setDuration(500)
                        .withEndAction(() -> {
                            card.animate()
                                    .translationX(0)
                                    .translationY(0)
                                    .setStartDelay(1000)
                                    .setDuration(500)
                                    .setInterpolator(new AccelerateDecelerateInterpolator())
                                    .start();
                        }).start();
            }
        }, 200);

        view.postDelayed(() -> {
            isFlipping = false;
            isRestarting = false;
            clicks = 0;
            shuffleGridLayout();
            showCards();
        }, 2500);
    }

    public void onGameComplete(long score) {
        Toast.makeText(this, "Game Complete!", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        leaderboardManager.saveScore(score);

        builder.setTitle("Game Complete!");
        builder.setMessage("Your score is " + score + " points!" + "\n" + toHumanReadableList(leaderboardManager.getScores()));

        builder.setPositiveButton("Play again?", (dialog, which) -> {
            onClickRestart(findViewById(R.id.restart));
        });

        builder.setNegativeButton("Go back?", (dialog, which) -> {
            finish();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static String toHumanReadableList(List<Long> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            return "";
        }

        return numbers.stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));
    }

    public long calculateScore(long timeElapsed, int numClicks, int numCards) {
        double timeFactor = Math.max(0, 1 - Math.log10(timeElapsed + 1) / Math.log10(180000)); // normalize timeElapsed to a value between 0 and 1
        double clickFactor = Math.max(0, 1 - Math.log10(numClicks + 1) / Math.log10(100)); // normalize numClicks to a value between 0 and 1
        double cardFactor = Math.max(0, 1 - Math.log10(numCards + 1) / Math.log10(50)); // normalize numCards to a value between 0 and 1
        return Math.round((100 * timeFactor) + (100 * clickFactor) + ((100 * numCards) * cardFactor));
    }

    public void onHome(View view) {
        finish();
    }
}