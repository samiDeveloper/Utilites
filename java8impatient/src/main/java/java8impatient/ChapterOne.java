package java8impatient;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 * Java SE 8 For the Really Impatient - exercises
 * 
 * Ch 01. Lambda Expressions
 */
public class ChapterOne {
    public static void main(String[] args) {
        // randomStuff();

        // exerciseTwoListDirs();
        // exerciseThreeListFiles();
        // exerciseFourSortFilesUsingComparator();
        // exerciseFourSortFilesLambaOnly();
        // exerciseSixUncheckRunnable();
        exerciseSevenAndThenRunnable();

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
