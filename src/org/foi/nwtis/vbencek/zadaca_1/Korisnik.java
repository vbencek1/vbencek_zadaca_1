package org.foi.nwtis.vbencek.zadaca_1;

import java.util.ArrayList;
import java.util.List;
import org.foi.nwtis.vbencek.konfiguracije.Konfiguracija;
import org.foi.nwtis.vbencek.konfiguracije.KonfiguracijaApstraktna;

/**
 * Klasa Korisnik kolekcija je podataka o korisnicima koje pribavlja iz datoteke. trenutno sadrži samo korisničko ime i lozinku
 * @author Valentino Bencek
 */


public class Korisnik {
    public String ime;
    public String lozinka;
    
    /**
     * lista koja sadrži popis korisnika
     */
    public static List<Korisnik> korisnici = new ArrayList<>();

    /**
     * Metoda koja pohranjuje zapise iz datoteke u listu korisnika
     * @param datoteka datoteka sa korisnicima
     */
    public static void spremiPodatke(String datoteka) {
        try {
            Konfiguracija konfiguracija = null;
            konfiguracija = KonfiguracijaApstraktna.preuzmiKonfiguraciju(datoteka);
            for (Object o : konfiguracija.dajSvePostavke().keySet()) {
                Korisnik korisnik= new Korisnik();
                korisnik.ime = (String) o;
                korisnik.lozinka = konfiguracija.dajPostavku(korisnik.ime);
                korisnici.add(korisnik);
            }

        } catch (Exception e) {
            System.out.println("Greška u spremanju podataka!");
        }
    }
    
    /**
     * Metoda koja ispisuje sve korisnike iz datoteke
     */
    public static void ispis() {
        for (Korisnik kor : korisnici) {
            System.out.println(kor.ime+" "+ kor.lozinka);
        }
    }
    
    /**
     * Metoda koja provjerava da li lozinka i korisničko ime dano od korisnika odgovara lozinki i korisničkom imenu u datoteci
     * @param korisnik korisničko ime
     * @param lozinka dana lozinka
     * @return vraća true ako odgovara inače vraća false
     */

    public static boolean provjeriImeiLozinku(String korisnik, String lozinka) {
        for (Korisnik kor : korisnici) {
            if (kor.ime.equals(korisnik) && kor.lozinka.equals(lozinka)) {
                return true;
            }
        }
        return false;
    }

}
