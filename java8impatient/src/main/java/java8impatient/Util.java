package java8impatient;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public final class Util {
    public static void printArray(Object[] objects) {
        printList(Arrays.asList(objects));
    }

    public static void printList(List<? extends Object> objects) {
        objects.stream().forEachOrdered(System.out::println);
    }

    public static <T> void printArray(T[] objects, Function<T, ?> transformAction) {
        printList(Arrays.asList(objects), transformAction);
    }

    public static <T> void printList(List<T> objects, Function<T, ?> f) {
        objects.stream().map(f).forEachOrdered(System.out::println);
    }

}
