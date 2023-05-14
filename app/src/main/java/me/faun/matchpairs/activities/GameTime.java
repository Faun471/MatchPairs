package me.faun.matchpairs.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.GridLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import me.faun.matchpairs.R;
import me.faun.matchpairs.customviews.IgasPlayingCard;
import me.faun.matchpairs.managers.LeaderboardManager;
import me.faun.matchpairs.utils.CardUtils;
import me.faun.matchpairs.utils.MediaPlayerUtils;
import me.faun.matchpairs.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collections;

public class GameTime extends AppCompatActivity {
    private final ArrayList<IgasPlayingCard> clickedCards = new ArrayList<>();
    private boolean isRestarting = true, isFlipping = true;
    private GridLayout gridLayout;
    private final String[] names = {"abigail", "harvey", "elliott", "linus", "wizard"};
    private LeaderboardManager leaderboardManager;
    private long elapsedTime = 0;
    private int clicks = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        MediaPlayerUtils.getInstance().playMusic(this, R.raw.game_music);

        gridLayout = findViewById(R.id.gridLayout);
        leaderboardManager = new LeaderboardManager(this);

        CardUtils.addCardsToGrid(this, gridLayout, this::onClick);

        gridLayout.post(() -> {
            CardUtils.giveNamesToCards(getCards(), names);
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
     * This method will show all cards for a short period of time.
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
        }

        gridLayout.postDelayed(() -> {
            // flip all cards back
            for (int i = cards.size() - 1; i >= 0; i--) {
                IgasPlayingCard card = cards.get(i);
                card.postDelayed(card::flip, (cards.size() - i) * 25L);
            }
        }, 2000L + (cards.size() * 25L));

        gridLayout.postDelayed(() -> {
            // allow user to click on cards
            isFlipping = false;
            isRestarting = false;
        }, 2000L + (cards.size() * 25L) + 500L);
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

    public void onClickRestart(View view) {
        if (isRestarting || isFlipping) {
            return;
        }

        isFlipping = true;
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
            isFlipping = true;
            isRestarting = true;
            clicks = 0;
            elapsedTime = 0;
            CardUtils.giveNamesToCards(getCards(), names);
            shuffleGridLayout();
            showCards();
        }, 2500);
    }

    public void onGameComplete(long score) {
        Toast.makeText(this, "Game Complete!", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        leaderboardManager.saveScore(score);

        builder.setTitle("Game Complete!");
        builder.setMessage("Your score is " + score + " points!" + "\n" + StringUtils.toHumanReadableList(leaderboardManager.getScores()));

        builder.setPositiveButton("Play again?", (dialog, which) -> {
            onClickRestart(findViewById(R.id.restart));
        });

        builder.setNegativeButton("Go back?", (dialog, which) -> {
            finish();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
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

    /*
     * onDestroy() is called when the activity is being destroyed.
     * This can happen when the activity is being finished or when the system is running low on memory.
     *
     * We want to stop the music when the activity is being destroyed.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaPlayerUtils.getInstance().stopMusic();
    }

    /*
     * onResume() is called when the activity is coming back from the background.
     *
     * We want to resume the music when the activity is coming back from the background.
     */
    @Override
    protected void onResume() {
        super.onResume();

        MediaPlayerUtils.getInstance().resumeMusic();

        if (MediaPlayerUtils.getInstance().isPlaying()) {
            return;
        }

        MediaPlayerUtils.getInstance().playMusic(this, R.raw.game_music);
        MediaPlayerUtils.getInstance().setLooping(true);
    }
}