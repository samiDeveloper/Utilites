package java8impatient;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Java SE 8 For the Really Impatient - exercises
 * 
 * Ch 01. Lambda Expressions
 */
public class ChapterOneLambda {
    public static void main(String[] args) {
        // randomStuff();

        // exerciseTwoListDirs();
        // exerciseThreeListFiles();
        // exerciseFourSortFilesUsingComparator();
        // exerciseFourSortFilesLambaOnly();
        // exerciseSixUncheckRunnable();
        // exerciseSevenAndThenRunnable();
        // exerciseSevenAndThenUsingChainableRunnable();
        // exerciseEightForEach();
        // exerciseNineCollectionForEachIf();
        // exerciseElevenInterfacesMethodClashes();
        // exerciseElevenSuperclassAndInterfaceMethodClashes();
        exerciseTwelveBackwardCompatibilityFails();

    }

    private static void exerciseTwelveBackwardCompatibilityFails() {
        class ArrayList2 extends ArrayList {
            
            // This is valid Java 7 code, but it does not compile in Java 8
            // public void stream() {}
        }
    }

    private static class S {
        public void f() {
            System.out.println("S");
        }
    }

    private static interface IAbstract {
        void f();
    }

    private static interface JAbstract {
        void f();
    }

    private static interface IDefault {
        default void f() {
            System.out.println("IDefault");
        }
    }

    private static interface JDefault {
        default void f() {
            System.out.println("JDefault");
        }
    }

    private static interface JStatic {
        static void f() {
            System.out.println(JStatic.class.getName());
        }
    }

    private static void exerciseElevenInterfacesMethodClashes() {
        class Defaults implements IDefault, JDefault {
            // clash, must override and resolve conflict.
            @Override
            public void f() {
                // Resolve by for example pick the one from I
                IDefault.super.f();
            }
        }

        class Abstracts implements IAbstract, JAbstract {
            // We just have to provide an implementation because none exists
            @Override
            public void f() {}
        }

        // Even if only one interface provides an implementation, the compiler considers this an ambiguity.
        // Subclass must resolve.
        class DefaultAbstract implements IDefault, JAbstract {
            @Override
            public void f() {}
        }

        // Nothing to see here
        class DefaultStatic implements IDefault, JStatic {}

        System.out.println("See comments in exerciseElevenInterfacesMethodClashes()");
    }

    private static void exerciseElevenSuperclassAndInterfaceMethodClashes() {
        class Defaults extends S implements IDefault {}
        new Defaults().f(); // Prints "S", superclass wins

        // Nothing new here, S provides implementation
        class Abstracts extends S implements IAbstract {}
    }

    private static interface Collection2<E> extends Collection<E> {
        public default void forEachIf(Consumer<E> action, Predicate<E> filter) {
            for (E element : this) {
                if (filter.test(element)) {
                    action.accept(element);
                }
            }
        }
    }

    // We need a named class to implement the interface
    private static class ArrayList2<E> extends ArrayList<E> implements Collection2<E> {}

    private static void exerciseNineCollectionForEachIf() {
        ArrayList2<String> al2 = new ArrayList2();
        al2.addAll(Arrays.asList("Joe", "Edouard Bracame", "Guido Brasletti", "Jean-Raoul Ducable",
                "Jean Manchzeck", "Paul Posichon", "Pierre Leghnome", "Jérémie Lapurée"));
        al2.forEachIf(System.out::println, (s) -> s.startsWith("J"));
    }

    private static void exerciseEightForEach() {
        String[] names = { "Peter", "Paul", "Mary" };
        List<Runnable> runners = new ArrayList<>();
        for (String name : names) {

            // legal, lambda captures the free 'name' variable
            runners.add(() -> System.out.println(name));
        }

        for (Runnable r : runners) {
            r.run();
        }
    }

    private static void exerciseSevenAndThenUsingChainableRunnable() {
        chainable(() -> System.out.println("one")).andThen(() -> System.out.println("two"))
                .andThen(() -> System.out.println("three")).run();

    }

    private static ChainableRunnable chainable(Runnable r) {
        return () -> {
            r.run();
        };
    }

    private interface ChainableRunnable extends Runnable {
        default ChainableRunnable andThen(Runnable r) {
            Objects.requireNonNull(r);
            return () -> {
                this.run();
                r.run();
            };
        }
    }

    private static void exerciseSevenAndThenRunnable() {
        andThen(() -> System.out.println("one"), () -> System.out.println("two")).run();
    }

    private static Runnable andThen(Runnable r1, Runnable r2) {
        return () -> {
            r1.run();
            r2.run();
        };
    }

    /** Runnable semantics throwing any exception */
    private static interface RunnableEx {
        void run() throws Exception;

        /** Converts the exception-throwing RunnableEx into a Runnable, wrapping the checked in an unchecked. */
        static Runnable uncheck(RunnableEx r) {
            return () -> {
                try {
                    r.run();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            };
        }
    };

    private static void exerciseSixUncheckRunnable() {

        new Thread(RunnableEx.uncheck(() -> {
            System.out.println("wait...");
            Thread.sleep(1000);
            System.out.println("done.");
        })).start();

    }

    private static void exerciseFourSortFilesLambaOnly() {
        File[] files = new File("/tmp").listFiles();

        Arrays.sort(files, (l, r) -> {
            if (l.isDirectory() && !r.isDirectory()) {
                return -1;
            } else if (!l.isDirectory() && r.isDirectory()) {
                return 1;
            } else {
                return l.getName().compareTo(r.getName());
            }
        });

        printArray(files, (File f) -> {
            return (f.isDirectory() ? "d" : "f") + " " + f.getName();
        });
    }

    private static void exerciseFourSortFilesUsingComparator() {
        File[] files = new File("/tmp").listFiles();
        Comparator<File> bytype = (l, r) -> {
            if (l.isDirectory() && !r.isDirectory()) {
                return -1;
            } else if (!l.isDirectory() && r.isDirectory()) {
                return 1;
            } else {
                return 0;
            }
        };
        Comparator<File> bytypeThenByname = bytype
                .thenComparing((l, r) -> l.getName().compareTo(r.getName()));

        Arrays.sort(files, bytypeThenByname);

        printArray(files, (File f) -> {
            return (f.isDirectory() ? "d" : "f") + " " + f.getName();
        });
    }

    private static void exerciseThreeListFiles() {
        File dir = new File("/home/bs/Pictures");
        String[] list = dir.list((d, name) -> name.endsWith("png"));
        printArray(list);
    }

    private static void exerciseTwoListDirs() {
        File dir = new File("/tmp");

        // lambda
        // File[] listFiles = dir.listFiles(f -> f.isDirectory());

        // method expr
        File[] listFiles = dir.listFiles(File::isDirectory);

        printArray(listFiles, File::getName);
    }

    private static void randomStuff() {
        new Thread(new Runnable() {
            public void run() {
                System.out.println("Hi");
            }
        }).start();

        new Thread(() -> {
            System.out.println("Ho");
        }).start();

        {
            String[] sa = { "b", "a", "c" };
            Arrays.sort(sa, (l, r) -> {
                return l.compareTo(r);
            });
            System.out.println(Arrays.asList(sa));
        }

        {
            String[] sb = { "f", "d", "e" };
            Arrays.sort(sb, String::compareTo);
            System.out.println(Arrays.asList(sb));
        }

        {
            String[] sa = { "i", "g", "h" };
            Arrays.sort(sa, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareTo(o2);
                }
            });
            System.out.println(Arrays.asList(sa));
        }
    }

    private static void printArray(Object[] objects) {
        printList(Arrays.asList(objects));
    }

    private static void printList(List<Object> objects) {
        objects.stream().forEachOrdered(System.out::println);
    }

    private static <T> void printArray(T[] objects, Function<T, ?> transformAction) {
        printList(Arrays.asList(objects), transformAction);
    }

    private static <T> void printList(List<T> objects, Function<T, ?> f) {
        objects.stream().map(f).forEachOrdered(System.out::println);
    }

}
