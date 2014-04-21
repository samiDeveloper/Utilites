package java8impatient;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ChapterTwoStream {
    private static final List<String> NAMES = Arrays.asList("Joe", "Edouard Bracame", "Guido Brasletti",
            "Jean-Raoul Ducable", "Jean Manchzeck", "Paul Posichon", "Pierre Leghnome", "Jérémie Lapurée");

    public static void main(String[] args) {
        randomStuff();
    }

    private static void randomStuff() {
        System.out.println(NAMES.stream().collect(Collectors.joining(", ")));

        IntSummaryStatistics intSummaryStatistics = NAMES.stream().collect(
                Collectors.summarizingInt(String::length));
        System.out.println("cnt: " + intSummaryStatistics.getCount());
        System.out.println("avg: " + intSummaryStatistics.getAverage());
        System.out.println("min: " + intSummaryStatistics.getMin());
        System.out.println("tot: " + intSummaryStatistics.getSum());

        Map<Integer, String> mapByLengthFirstWins = NAMES.stream().collect(
                Collectors.toMap(String::length, Function.identity(), (existing, newValue) -> existing));
        System.out.println("first length 15 name: " + mapByLengthFirstWins.get(15));

        Map<Integer, String> mapByLengthLastWins = NAMES.stream().collect(
                Collectors.toMap(String::length, Function.identity(), (existing, newValue) -> newValue));
        System.out.println("last length 15 name: " + mapByLengthLastWins.get(15));

        // Map name length to set of names having that length...
        {
            // Naive implementation where we do not resolve dup keys
            Collector<String, ?, Map<Integer, Set<String>>> mapper = Collectors.toMap(String::length, (
                    String s) -> {
                Set<String> strSet = new HashSet<String>();
                strSet.add(s);
                return strSet;
            });
            try {
                NAMES.stream().collect(mapper);
            } catch (IllegalStateException e) {
                System.out.println("Caught '" + e + "'");
            }
        }

        {
            // Better naive implementation using Collections.singleton
            Collector<String, ?, Map<Integer, Set<String>>> mapper = Collectors.toMap(String::length, s->Collections.singleton(s));
            try {
                NAMES.stream().collect(mapper);
            } catch (IllegalStateException e) {
                System.out.println("Caught '" + e + "'");
            }
        }

        {
            // Resolve duplicates, merge them in a new set
            Collector<String, ?, Map<Integer, Set<String>>> mapper = Collectors.toMap(String::length, s->Collections.singleton(s)
            , (Set<String> existing, Set<String> newValue) -> {
                Set<String> set = new HashSet<String>(existing);
                set.addAll(newValue);
                return set;
            });

            Map<Integer, Set<String>> mapByLength = NAMES.stream().collect(mapper);
            System.out.println("# names length 15: " + mapByLength.get(15).size());
        }
        

    }

}
