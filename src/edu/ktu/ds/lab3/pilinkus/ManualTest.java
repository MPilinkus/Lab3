package edu.ktu.ds.lab3.pilinkus;

import edu.ktu.ds.lab3.utils.HashMap;
import edu.ktu.ds.lab3.utils.HashType;
import edu.ktu.ds.lab3.utils.Ks;
import edu.ktu.ds.lab3.utils.ParsableHashMap;

import java.util.Locale;

public class ManualTest {

    public static void main(String[] args) throws CloneNotSupportedException {
        Locale.setDefault(Locale.US);
        executeTest();
    }

    public static void executeTest() throws CloneNotSupportedException {
        HashMap<String, Integer> map = new HashMap<>(HashType.DIVISION);
        map.put("N01", 1);
        Ks.ounn(map.averageChainSize());
        map.put("N11", 10);
        Ks.ounn(map.averageChainSize());
        map.put("N23", 164);
        Ks.ounn(map.averageChainSize());
        map.put("N46", 24);
        Ks.ounn(map.averageChainSize());
        map.put("N02", 98);
        Ks.ounn(map.averageChainSize());
        map.put("N13", 33);
        Ks.ounn(map.averageChainSize());
        map.put("N16", 79);
        Ks.ounn(map.averageChainSize());
        map.put("N33", 64);
        Ks.ounn(map.averageChainSize());
        map.put("N32", 85);
        Ks.ounn(map.averageChainSize());
        map.put("N49", 19);
        Ks.ounn(map.averageChainSize());
        map.put("N05", 77);
        Ks.ounn(map.averageChainSize());
        map.put("N09", 34);
        Ks.oun("Data:");
        Ks.ounn(map);
        Ks.oun("KeySet:");
        Ks.ounn(map.keySet());
        Ks.oun("Average chain size:");
        Ks.ounn(map.averageChainSize());

    }
}
