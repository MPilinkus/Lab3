package edu.ktu.ds.lab3.pilinkus;

import edu.ktu.ds.lab3.utils.*;

import java.util.Locale;

public class HouseTest {

    static House[] houses;

    private static void run() {
        ParsableHashMap<String, House> houseMap
                = new ParsableHashMap<>(String::new, House::new, HashType.DIVISION);
        House h1  = new House(
                "Kaunas",
                "Berzu g. 1",
                2014,
                250000.00,
                150.75);
        House h2  = new House(
                "Kaunas",
                "Rutu g. 1",
                2012,
                150000.00,
                200.50);
        House h3  = new House(
                "Kaunas",
                "Plytu g. 1",
                2018,
                550000.00,
                300.00);
        House h4  = new House(
                "Vilnius",
                "Rytu g. 1",
                2000,
                50000.00,
                125.20);
        House h5  = new House(
                "Vilnius",
                "Metu g. 1",
                2015,
                700000.00,
                512.00);
        House h6  = new House(
                "Kaunas",
                "Giliu g. 1",
                2015,
                600000.00,
                412.00);
        Ks.oun(houseMap.numberOfEmpties());
        House[] housesArray = {h3, h1, h2, h4, h5};
        String[] housesIds = {"H156", "H102", "H178", "H171", "H105", "H106", "H107", "H108"};
        int id = 0;
        for (House h : housesArray) {
            houseMap.put(housesIds[id++], h);
            Ks.oun(houseMap.numberOfEmpties());
        }
        Ks.ounn(houseMap);
        //houseMap.println("Porų išsidėstymas atvaizdyje pagal raktus");
        Ks.oun("Ar egzistuoja pora atvaizdyje?");
//        Ks.oun(houseMap.numberOfEmpties());
//        Ks.oun(houseMap.contains(housesIds[4]));
//        Ks.oun("Pašalinamos poros iš atvaizdžio:");
//        Ks.oun(houseMap.remove(housesIds[1]));
//        Ks.oun(houseMap.remove(housesIds[4]));
//       // houseMap.println("Porų išsidėstymas atvaizdyje pagal raktus");
//        Ks.oun("Atliekame porų paiešką atvaizdyje:");
//        Ks.oun(houseMap.get(housesIds[0]));
//        Ks.oun(houseMap.get(housesIds[2]));
//        Ks.oun("Išspausdiname atvaizdžio poras String eilute:");
        Ks.ounn(houseMap);


    }

    public static void main(String... args) {
        Locale.setDefault(new Locale("LT"));
        run();
        Ks.oun("House test done");
    }
}
