package java8impatient;

import static java8impatient.Util.printArray;
import static java8impatient.Util.printList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.sun.javafx.binding.SelectBinding.AsLong;

public class ChapterTwoStream {
    private static final List<String> NAMES = Arrays.asList("Joe", "Edouard Bracame", "Guido Brasletti",
            "Jean-Raoul Ducable", "Jean Manchzeck", "Paul Posichon", "Pierre Leghnome", "Jérémie Lapurée");

    private static final String TEXT = "Lorem ipsum dolor sit amet consectetur adipisicing elit sed do eiusmod tempor incididunt ut labore "
            + "et dolore magna aliqua Ut enim ad minim veniam quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo "
            + "consequat Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur";

    public static void main(String[] args) {
        // randomStuff();
        // exerciseOneParallelFor();
        // exerciseTwoFirstFiveLongWords();
        // exerciseThreeParallelPerformance();
        // exerciseFourIntStream();
        // exerciseFiveStreamIterate();
        // exerciseSixCharStream();
        // exerciseEightAlternate();
        // exerciseNineJoinArrayLists();
        exerciseTenAvgDoubleStream();

    }

    private static void exerciseTenAvgDoubleStream() {
        class Avg {
            public final long count;
            public final double avg;

            public Avg(long count, double avg) {
                super();
                this.count = count;
                this.avg = avg;
            }
        }

        Stream<Double> doubleStream = Arrays.asList(1.6d, 6.3d, 2.0d).stream();
        Avg reduce = doubleStream.reduce(new Avg(0, 0.0d),
                (avg, d) -> new Avg(avg.count + 1, (avg.avg * avg.count + d) / (avg.count + 1)), (l, r) -> new Avg(l.count
                        + r.count, (l.avg * l.count + r.avg * r.count) / (l.count + r.count)));
        System.out.println(reduce.avg);
    }

    private static void exerciseNineJoinArrayLists() {
        ArrayList<String> list1 = new ArrayList<String>(Arrays.asList("1", "2"));
        ArrayList<String> list2 = new ArrayList<String>(Arrays.asList("a", "b"));

        {
            Optional<ArrayList<String>> reduce = Arrays.asList(list1, list2).stream().reduce((l, r) -> {
                ArrayList<String> newList = new ArrayList<String>(l);
                newList.addAll(r);
                return newList;
            });
            if (reduce.isPresent()) {
                printList(reduce.get());
            }
        }

        {
            ArrayList<String> reduce = Arrays.asList(list1, list2).stream().reduce(new ArrayList<String>(), (l, r) -> {
                ArrayList<String> newList = new ArrayList<String>(l);
                newList.addAll(r);
                return newList;
            });
            printList(reduce);
        }

        {
            ArrayList<String> reduce = Arrays.asList(list1, list2).stream().reduce(new ArrayList<String>(), (l, r) -> {
                ArrayList<String> newList = new ArrayList<String>(l);
                newList.addAll(r);
                return newList;
            }, (l, r) -> {
                ArrayList<String> newList = new ArrayList<String>(l);
                newList.addAll(r);
                return newList;
            });
            printList(reduce);
        }

    }

    private static void exerciseEightAlternate() {
        Stream<String> zip = zip(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9").stream(), NAMES.stream());
        printArray(zip.toArray());
    }

    public static <T> Stream<T> zip(Stream<T> first, Stream<T> second) {
        Iterator<T> iterSecond = second.iterator();
        return first.flatMap(t -> {
            if (iterSecond.hasNext()) {
                return Arrays.asList(t, iterSecond.next()).stream();
            } else {
                first.close();
                return null;
            }
        });
    }

    private static void exerciseSixCharStream() {
        Stream<Character> charStream = IntStream.range(0, TEXT.length()).boxed().map(TEXT::charAt)
                .map(i -> new Character((char) i));
        charStream.peek(System.out::println).count();
    }

    private static void exerciseFiveStreamIterate() {
        long a = 25214903917l;
        byte c = 11;
        long m = 281474976700000l;
        Stream<Long> generated = Stream.iterate(1l, x -> (a * x + c) % m);
        List<Long> collect = generated.limit(6).collect(Collectors.toList());
        printList(collect);
    }

    private static void exerciseFourIntStream() {
        int[] values = { 1, 4, 9, 16 };
        Stream<int[]> arrayStream = Stream.of(values); // stream of single element
        IntStream intStream = Arrays.stream(values);
    }

    private static void exerciseThreeParallelPerformance() {
        final String warAndPeace = readWarAndPeace();
        List<String> words = Arrays.asList(warAndPeace.split("[\\P{L}]+"));
        {
            long start = System.currentTimeMillis();
            words.stream().filter(s -> s.length() > 12).count();
            long msec = System.currentTimeMillis() - start;
            System.out.println(String.format("Sequential count: %d millisec", msec));
        }
        {
            long start = System.currentTimeMillis();
            words.parallelStream().filter(s -> s.length() > 12).count();
            long msec = System.currentTimeMillis() - start;
            System.out.println(String.format("Parallel count: %d millisec", msec));
        }
    }

    private static String readWarAndPeace() {
        try {
            return Resources.toString(Resources.getResource("war-and-peace.txt"), Charsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void exerciseTwoFirstFiveLongWords() {
        List<String> words = Arrays.asList(TEXT.split("[\\P{L}]+"));
        List<String> collect = words.stream().filter(s -> {
            System.out.println("filter '" + s + "'");
            return s.length() > 8;
        }).limit(5).collect(Collectors.toList());
        printList(collect);
    }

    private static void exerciseOneParallelFor() {
        List<String> words = Arrays.asList(TEXT.split("[\\P{L}]+"));
        // The intention of the exercise is probably to program threads instead of this:
        System.out.println(words.parallelStream().filter(s -> s.length() > 10).count());
    }

    private static void randomStuff() {
        System.out.println(NAMES.stream().collect(Collectors.joining(", ")));

        IntSummaryStatistics intSummaryStatistics = NAMES.stream().collect(Collectors.summarizingInt(String::length));
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
            Collector<String, ?, Map<Integer, Set<String>>> mapper = Collectors.toMap(String::length, (String s) -> {
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
            Collector<String, ?, Map<Integer, Set<String>>> mapper = Collectors.toMap(String::length,
                    s -> Collections.singleton(s));
            try {
                NAMES.stream().collect(mapper);
            } catch (IllegalStateException e) {
                System.out.println("Caught '" + e + "'");
            }
        }

        {
            // Resolve duplicates, merge them in a new set
            Collector<String, ?, Map<Integer, Set<String>>> mapper = Collectors.toMap(String::length,
                    s -> Collections.singleton(s), (Set<String> existing, Set<String> newValue) -> {
                        Set<String> set = new HashSet<String>(existing);
                        set.addAll(newValue);
                        return set;
                    });

            Map<Integer, Set<String>> mapByLength = NAMES.stream().collect(mapper);
            System.out.println("# names length 15: " + mapByLength.get(15).size());
        }

    }

}
