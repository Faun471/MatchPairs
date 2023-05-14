package me.faun.matchpairs.utils;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class StringUtils {
    /*
     * Converts a list of numbers to a human-readable list.
     * For example, [1, 2, 3] will be converted to "1, 2, 3".
     *
     * @param numbers the list of numbers to be converted
     * @return the human-readable list
     */
    public static String toHumanReadableList(List<Long> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            return "";
        }

        return numbers.stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));
    }

    /*
     * Shuffles the elements of the array.
     *
     * @param names the array to be shuffled
     * @return the shuffled array
     */
    public static String[] shuffleArray(@NotNull String[] names) {
        return new Random().ints(0, names.length)
                .distinct()
                .limit(names.length)
                .mapToObj(i -> names[i])
                .toArray(String[]::new);
    }
}
