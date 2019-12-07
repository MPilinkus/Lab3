package edu.ktu.ds.lab3.pilinkus;

import edu.ktu.ds.lab3.demo.Timekeeper;
import edu.ktu.ds.lab3.gui.ValidationException;
import edu.ktu.ds.lab3.utils.Ks;

import java.io.File;
import java.io.FileNotFoundException;
import java.util .*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

    public class Benchmark {

        public static final String FINISH_COMMAND = "                               ";
        private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("edu.ktu.ds.lab3.gui.messages");

        private final Timekeeper timekeeper;

        private final String[] BENCHMARK_NAMES = {"input", "javaCon", "cstmCon", "javaRmv", "cstmRmv"};
        private final int[] COUNTS = {5000, 10000, 20000, 40000, 80000};

        private final HashMap<String, String> javaHashMap = new HashMap<String, String>();
        private final edu.ktu.ds.lab3.utils.HashMap<String, String> customHashMap = new edu.ktu.ds.lab3.utils.HashMap<String, String>();
        private String[] keys = new String[]{};
        private int currentCount;

        /**
         * For console benchmark
         */
        public Benchmark() {
            timekeeper = new Timekeeper(COUNTS);
        }

        /**
         * For Gui benchmark
         *
         * @param resultsLogger
         * @param semaphore
         */
        public Benchmark(BlockingQueue<String> resultsLogger, Semaphore semaphore) {
            semaphore.release();
            timekeeper = new Timekeeper(COUNTS, resultsLogger, semaphore);
        }

        public static void main(String[] args) {
            executeTest();
        }

        public static void executeTest() {
            // suvienodiname skaičių formatus pagal LT lokalę (10-ainis kablelis)
            Locale.setDefault(new Locale("LT"));
            Ks.out("Greitaveikos tyrimas:\n");
            new Benchmark().startBenchmark();
        }

        public void startBenchmark() {
            try {
                benchmark();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
            }
        }

        public void benchmark() throws InterruptedException {
            try {
                for(int k : COUNTS){
                    currentCount = 0;
                    File file = new File("D:\\OneDrive - Kaunas University of Technology\\Duomenu_strukturos\\Lab3_MaisosLenteles_Intellij\\data\\zodynas.txt");
                    Scanner sc = new Scanner(file);
                    javaHashMap.clear();
                    customHashMap.clear();
                    timekeeper.startAfterPause();
                    timekeeper.start();
                    List<String> allWords = new ArrayList<>();
                    while(sc.hasNextLine()){
                        String line = sc.nextLine();
                        allWords.add(line);

                    }
                    Collections.shuffle(allWords);
                    System.gc();
                    System.gc();
                    System.gc();
                    long memTotal = Runtime.getRuntime().totalMemory();
                    long memFree = Runtime.getRuntime().freeMemory();
                    for(int i = 0; i < k; i++)
                    {
                        javaHashMap.put(allWords.get(i), allWords.get(i));
                    }
                    long memFreeJav = Runtime.getRuntime().freeMemory();
                    for(int i = 0; i < k; i++)
                    {
                        customHashMap.put(allWords.get(i), allWords.get(i));
                    }
                    long memFreeCst = Runtime.getRuntime().freeMemory();
                    Collections.shuffle(allWords);
                    List<String> keysList = new ArrayList<>();
                    for(int i = 0; i < k; i++)
                    {
                        keysList.add(allWords.get(i));
                    }
                    Collections.shuffle(keysList);
                    keys = keysList.toArray(new String[0]);
                    Arrays.stream(keys).forEach(customHashMap::contains);
                    timekeeper.finish(BENCHMARK_NAMES[0]);
                    Arrays.stream(keys).forEach(javaHashMap::containsKey);
                    timekeeper.finish(BENCHMARK_NAMES[1]);
                    Arrays.stream(keys).forEach(customHashMap::contains);
                    timekeeper.finish(BENCHMARK_NAMES[2]);
                    Arrays.stream(keys).forEach(javaHashMap::remove);
                    timekeeper.finish(BENCHMARK_NAMES[3]);
                    Arrays.stream(keys).forEach(customHashMap::remove);
                    timekeeper.finish(BENCHMARK_NAMES[4]);
                    Ks.oun(k);
                    Ks.ounn(memTotal - memFreeJav - (memTotal - memFree));
                    Ks.ounn(memTotal - memFreeCst - (memTotal - memFreeJav - (memTotal - memFree)));
                    timekeeper.seriesFinish();
                }


                StringBuilder sb = new StringBuilder();
                timekeeper.logResult(sb.toString());
                timekeeper.logResult(FINISH_COMMAND);
            } catch (ValidationException | FileNotFoundException e) {
                timekeeper.logResult(e.getMessage());
            }
        }
    }

