package edu.ktu.ds.lab3.pilinkus;

import edu.ktu.ds.lab3.utils.EvaluableMap;
import edu.ktu.ds.lab3.utils.HashType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class HashMapOA<K, V> implements EvaluableMap<K, V> {

    public static final int DEFAULT_INITIAL_CAPACITY = 16;
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;
    public static final HashType DEFAULT_HASH_TYPE = HashType.DIVISION;

    // Maišos lentelė
    protected Node<K, V>[] table;
    // Lentelėje esančių raktas-reikšmė porų kiekis
    protected int size = 0;
    // Apkrovimo faktorius
    protected float loadFactor;
    // Maišos metodas
    protected HashType ht;
    //--------------------------------------------------------------------------
    //  Maišos lentelės įvertinimo parametrai
    //--------------------------------------------------------------------------
    // Maksimalus suformuotos maišos lentelės grandinėlės ilgis
    protected int maxChainSize = 1;
    // Permaišymų kiekis
    protected int rehashesCounter = 0;
    // Paskutinės patalpintos poros grandinėlės indeksas maišos lentelėje
    protected int lastUpdatedChain = 0;
    // Lentelės grandinėlių skaičius
    protected int chainsCounter = 0;
    // Einamas poros indeksas maišos lentelėje
    protected int index = 0;

    /* Klasėje sukurti 4 perkloti konstruktoriai, nustatantys atskirus maišos
     * lentelės parametrus. Jei kuris nors parametras nėra nustatomas -
     * priskiriama standartinė reikšmė.
     */
    public HashMapOA() {
        this(DEFAULT_HASH_TYPE);
    }

    public HashMapOA(HashType ht) {
        this(DEFAULT_INITIAL_CAPACITY, ht);
    }

    public HashMapOA(int initialCapacity, HashType ht) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR, ht);
    }

    public HashMapOA(float loadFactor, HashType ht) {
        this(DEFAULT_INITIAL_CAPACITY, loadFactor, ht);
    }

    public HashMapOA(int initialCapacity, float loadFactor, HashType ht) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }

        if ((loadFactor <= 0.0) || (loadFactor > 1.0)) {
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        }

        this.table = new Node[initialCapacity];
        this.loadFactor = loadFactor;
        this.ht = ht;
    }

    public boolean containsValue(Object value) {
        if (value == null)
            return false;
        for (Node<K, V> n : table) {
                if (n != null && n.value.equals(value)){
                    return true;
                }
        }
        return false;
    }

    public Set<K> keySet() {
        Set<K> keySet = new HashSet<K>();
        for (Node<K, V> n : table) {
            for(Node<K, V> nn = n; nn != null; nn = nn.next) {
                keySet.add(nn.key);
            }
        }
        return keySet;
    }

    public V putIfAbsent(K key, V value) {
        V val = get(key);
        if(val == null) {
            put(key, value);
        }
        return val;
    }

    public int averageChainSize() {
        if (chainsCounter == 0)
            return 0;
        return size / chainsCounter;
    }

    public int numberOfEmpties() {
        return table.length - chainsCounter;
    }

    /**
     * Patikrinama ar atvaizdis yra tuščias.
     *
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Grąžinamas atvaizdyje esančių porų kiekis.
     *
     * @return Grąžinamas atvaizdyje esančių porų kiekis.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Išvalomas atvaizdis.
     */
    @Override
    public void clear() {
        Arrays.fill(table, null);
        size = 0;
        index = 0;
        lastUpdatedChain = 0;
        maxChainSize = 0;
        rehashesCounter = 0;
        chainsCounter = 0;
    }

    /**
     * Patikrinama ar pora egzistuoja atvaizdyje.
     *
     * @param key raktas.
     * @return Patikrinama ar pora egzistuoja atvaizdyje.
     */
    @Override
    public boolean contains(K key) {
        return get(key) != null;
    }

    /**
     * Atvaizdis papildomas nauja pora.
     *
     * @param key raktas,
     * @param value reikšmė.
     * @return Atvaizdis papildomas nauja pora.
     */
    @Override
    public V put(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Key or value is null in put(Key key, Value value)");
        }
        int temp = hash(key);
        int i = temp;
        do{
            if(table[i] == null){
                table[i] = new Node<>(key, value, table[i]);;
                size++;
                chainsCounter++;
                index = i;

                if (size > table.length * loadFactor) {
                    rehash(table[index]);
                } else {
                    lastUpdatedChain = index;
                }
                return value;
            }
            if (table[i].key.equals(key))
            {
                table[i].value = value;
                return value;
            }
            i = (i + 1) % table.length;
        } while (i != temp);

        return value;
    }

    /**
     * Grąžinama atvaizdžio poros reikšmė.
     *
     * @return Grąžinama atvaizdžio poros reikšmė.
     *
     * @param key raktas.
     */
    @Override
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key is null in get(Key key)");
        }
        index = hash(key);
        while(table[index] != null) {
            if(table[index].key.equals(key))
                return table[index].value;
            index = (index + 1) % table.length;
        }
        return null;
    }

    /**
     * Pora pašalinama iš atvaizdžio.
     *
     * @param key Pora pašalinama iš atvaizdžio.
     * @return key raktas.
     */
    @Override
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Key is null in remove(Key key)");
        }
        /** find position key and delete **/
        index = hash(key);
        while (!key.equals(table[index].key))
            index = (index + 1) % table.length;
        V removed = table[index].value;
        table[index].key = null;
        table[index].value = null;

        /** rehash all keys **/
        for (index = (index + 1) % table.length; table[index].key != null; index = (index + 1) % table.length)
        {
            K tmp1 = table[index].key;
            V tmp2 = table[index].value;
            table[index].key = null;
            table[index].value = null;
            size--;
            put(tmp1, tmp2);
        }
        size--;
        return removed;
    }

    /**
     * Permaišymas
     *
     * @param node
     */
    private void rehash(Node<K, V> node) {
        HashMapOA<K, V> newMap = new HashMapOA<>(table.length * 2, loadFactor, ht);
        for (int i = 0; i < table.length; i++) {
            while (table[i] != null) {
                if (table[i].equals(node)) {
                    lastUpdatedChain = i;
                }
                newMap.put(table[i].key, table[i].value);
                table[i] = table[i].next;
            }
        }
        table = newMap.table;
        maxChainSize = newMap.maxChainSize;
        chainsCounter = newMap.chainsCounter;
        rehashesCounter++;
    }


    private int hash(K key) {
        int h = key.hashCode();
        return Math.abs(h) % table.length;
    }

    /**
     * Paieška vienoje grandinėlėje
     *
     * @param key
     * @param node
     * @return
     */
    private Node<K, V> getInChain(K key, Node<K, V> node) {
        if (key == null) {
            throw new IllegalArgumentException("Key is null in getInChain(Key key, Node node)");
        }
        int chainSize = 0;
        for (Node<K, V> n = node; n != null; n = n.next) {
            chainSize++;
            if ((n.key).equals(key)) {
                return n;
            }
        }
        maxChainSize = Math.max(maxChainSize, chainSize + 1);
        return null;
    }

    @Override
    public String[][] toArray() {
        String[][] result = new String[table.length][];
        int count = 0;
        for (Node<K, V> n : table) {
            String[] list = new String[getMaxChainSize()];
            int countLocal = 0;
            while (n != null) {
                list[countLocal++] = n.toString();
                n = n.next;
            }
            result[count] = list;
            count++;
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Node<K, V> node : table) {
            if (node != null) {
                for (Node<K, V> n = node; n != null; n = n.next) {
                    result.append(n.toString()).append(System.lineSeparator());
                }
            }
        }
        return result.toString();
    }

    /**
     * Grąžina maksimalų grandinėlės ilgį.
     *
     * @return Maksimalus grandinėlės ilgis.
     */
    @Override
    public int getMaxChainSize() {
        return maxChainSize;
    }

    /**
     * Grąžina formuojant maišos lentelę įvykusių permaišymų kiekį.
     *
     * @return Permaišymų kiekis.
     */
    @Override
    public int getRehashesCounter() {
        return rehashesCounter;
    }

    /**
     * Grąžina maišos lentelės talpą.
     *
     * @return Maišos lentelės talpa.
     */
    @Override
    public int getTableCapacity() {
        return table.length;
    }

    /**
     * Grąžina paskutinės papildytos grandinėlės indeksą.
     *
     * @return Paskutinės papildytos grandinėlės indeksas.
     */
    @Override
    public int getLastUpdatedChain() {
        return lastUpdatedChain;
    }

    /**
     * Grąžina grandinėlių kiekį.
     *
     * @return Grandinėlių kiekis.
     */
    @Override
    public int getChainsCounter() {
        return chainsCounter;
    }

    protected static class Node<K, V> {

        // Raktas
        protected K key;
        // Reikšmė
        protected V value;
        // Rodyklė į sekantį grandinėlės mazgą
        protected Node<K, V> next;

        public Node<K, V> getNext() {
            return next;
        }

        protected Node() {
        }

        protected Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }
}
