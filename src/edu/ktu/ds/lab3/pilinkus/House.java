package edu.ktu.ds.lab3.pilinkus;

import edu.ktu.ds.lab3.utils.Ks;
import edu.ktu.ds.lab3.utils.Parsable;

import java.util.*;

public class House implements Parsable<House> {
    private String city;
    private String address;
    private int year;
    private double price;
    private double area;
    private static final String idCode = "HS";
    private static int serNr = 100;
    private final String houseRegNr;

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public int getYear() {
        return year;
    }

    public double getPrice() {
        return price;
    }

    public double getArea() {
        return area;
    }

    public String getHouseRegNr() { return houseRegNr; }

    public House() {
        this.houseRegNr = idCode + (serNr++);
    }

    public House(String city, String address, int year, double price, double area) {
        this.houseRegNr = idCode + (serNr++);
        this.city = city;
        this.address = address;
        this.year = year;
        this.price = price;
        this.area = area;
    }

    public House(String data) {
        this.houseRegNr = idCode + (serNr++);
        parse(data);
    }

    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.city);
        hash = 29 * hash + Objects.hashCode(this.address);
        hash = 29 * hash + this.year;
        hash = 29 * hash + (int) (Double.doubleToLongBits(this.price) ^ (Double.doubleToLongBits(this.price) >>> 32));
        hash = 29 * hash + (int) (Double.doubleToLongBits(this.area) ^ (Double.doubleToLongBits(this.area) >>> 32));
        return hash;
    }

    @Override
    public final void parse(String data) {
        try {
            Scanner ed = new Scanner(data);
            city = ed.next();
            address = ed.next();
            year = ed.nextInt();
            price = ed.nextDouble();
            area = ed.nextDouble();
        } catch (InputMismatchException e) {
            Ks.ern("Blogas duomenų formatas apie namą -> " + data);
        } catch (NoSuchElementException e) {
            Ks.ern("Trūksta duomenų apie namą -> " + data);
        }
    }

    @Override
    public String toString() {
        return String.format("%-8s %-8s %4d %9.1f %8.1f",
                city, address, year, price, area);
    }

    //@Override
    public int compareTo(House otherHouse) {
        return getHouseRegNr().compareTo(otherHouse.getHouseRegNr());
    }

    public static Comparator<House> byArea = (House h1, House h2) -> {
        // didėjanti tvarka, pradedant nuo mažiausios
        if (h1.area < h2.area) {
            return -1;
        }
        if (h1.area > h2.area) {
            return +1;
        }
        return 0;
    };

    public static Comparator<House> byPrice = (House h1, House h2) -> {
        // didėjanti tvarka, pradedant nuo mažiausios
        if (h1.price < h2.price) {
            return -1;
        }
        if (h1.price > h2.price) {
            return +1;
        }
        return 0;
    };

    public static Comparator<House> byYearPrice = (House h1, House h2) -> {
        // metai mažėjančia tvarka, esant vienodiems lyginama kaina
        if (h1.year > h2.year) {
            return +1;
        }
        if (h1.year < h2.year) {
            return -1;
        }
        if (h1.price > h2.price) {
            return +1;
        }
        if (h1.price < h2.price) {
            return -1;
        }
        return 0;
    };
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final House other = (House) obj;
        if (this.year != other.year) {
            return false;
        }
        if (Double.doubleToLongBits(this.area) != Double.doubleToLongBits(other.area)) {
            return false;
        }
        if (Double.doubleToLongBits(this.price) != Double.doubleToLongBits(other.price)) {
            return false;
        }
        if (!Objects.equals(this.city, other.city)) {
            return false;
        }
        if (!Objects.equals(this.address, other.address)) {
            return false;
        }
        return true;
    }

}
