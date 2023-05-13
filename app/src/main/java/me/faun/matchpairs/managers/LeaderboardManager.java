package me.faun.matchpairs.managers;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.*;

public class LeaderboardManager {

    private static final String PREF_KEY_SCORES = "scores";
    private final SharedPreferences prefs;

    public LeaderboardManager(Context context) {
        prefs = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
    }

    public void saveScore(long newScore) {
        Set<String> scoresSet = prefs.getStringSet(PREF_KEY_SCORES, new HashSet<>());
        List<Long> scoresList = new ArrayList<>();
        for (String score : scoresSet) {
            scoresList.add(Long.parseLong(score));
        }
        scoresList.add(newScore);
        scoresSet = new HashSet<>();
        for (Long score : scoresList) {
            scoresSet.add(score.toString());
        }
        prefs.edit().putStringSet(PREF_KEY_SCORES, scoresSet).apply();
    }

    public List<Long> getScores() {
        Set<String> scoresSet = prefs.getStringSet(PREF_KEY_SCORES, new HashSet<>());
        List<Long> scoresList = new ArrayList<>();

        for (String score : scoresSet) {
            scoresList.add(Long.parseLong(score));
        }
        scoresList.sort(Collections.reverseOrder());
        return scoresList;
    }

}
