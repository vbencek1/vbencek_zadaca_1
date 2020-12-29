package org.foi.nwtis.vbencek.zadaca_1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa Aerodrom zapravo je kolekcija čiji je zadatak pohrana podataka o aerodromima iz csv datoteke u listu koju čita program. Također sadrži i  metode za rad s tim podacima
 * @author Valentino Bencek
 */


public class Aerodrom {
    
    private String icao;
    private String naziv;
    private String drzava;
    private String koordinataX;
    private String koordinataY;
    
    /**
     * lista u koju se pohranjuju podaci o Aerodromima
     */
    public static List<Aerodrom> aerodromi = new ArrayList<>();

    /**
     * metoda koja služi za pohranu podataka iz csv datoteke u listu
     * metoda otvara datoteku i zatim ovisno o separatoru pohranjuje podatke u različite varijable koje se preko objekta pohranjuju u listu
     * @param datoteka ulazna datoteka
     */
    public static void spremiPodatke(String datoteka) {
        String redak = "";
        BufferedReader br=null;
        try {
            br = new BufferedReader(new FileReader(datoteka));
            br.readLine();
            while ((redak = br.readLine()) != null) {
                Aerodrom a = new Aerodrom();
                String[] ispis = redak.split("\",\"");
                if(ispis.length==4){
                a.icao = ispis[0].substring(1);
                a.naziv = ispis[1];
                a.drzava = ispis[2];
                a.koordinataX = ispis[3].split(",")[0];
                a.koordinataY = ispis[3].split(",")[1].substring(1,ispis[3].split(",")[1].length()-1);}
                else{
                    String[] ispis1 = redak.split("\",");
                    a.icao = ispis1[0].substring(1);
                    a.naziv = ispis1[1].substring(1);                   
                    a.koordinataX = ispis1[2].split(",")[1].substring(1);
                    a.koordinataY = ispis1[2].split(",")[2].substring(1,ispis1[2].split(",")[2].length()-1);}
                aerodromi.add(a);}
        } catch (IOException e) {
            System.out.println("Pogreška u čitanju datoteke: " + datoteka);
            try { br.close();
            } catch (IOException ex) {
                System.out.println("Pogreška u čitanju datoteke: " + datoteka);}}
    }
    
    /**
     * Metoda koja provjerava postojanje aerodroma u listi
     * @param oznaka oznaka aerodroma
     * @return vraća true ako postoji i false ako ne postoji
     */

    public static boolean provjeriAkoPostojiAeordrom(String oznaka) {
        
        for (Aerodrom aer : aerodromi) {
            if(aer.icao.equals(oznaka)){
                return true;}
        }return false; 
    }
    
    /**
     * Metoda koja izračunava udaljenost između dva aerodroma
     * @param oznakaPol aerodrom polazišta
     * @param oznakaOdr aerodrom odredišta
     * @return  vraća izračunatu udaljenost kao string
     */
    
    public static String izračunajUdaljenost(String oznakaPol, String oznakaOdr) {
        double X1=0;
        double Y1=0;
        double X2=0;
        double Y2=0;
        for (Aerodrom aer : aerodromi) {
            if(aer.icao.equals(oznakaPol)){
                X1=Double.parseDouble(aer.koordinataX);
                Y1=Double.parseDouble(aer.koordinataY);
            }
            if(aer.icao.equals(oznakaOdr)){
                X2=Double.parseDouble(aer.koordinataX);
                Y2=Double.parseDouble(aer.koordinataY);
            }
        }
        int udaljenost=Koordinata.izracunajUdaljenost(new Koordinata(X1,Y1),new Koordinata(X2,Y2));
        return Integer.toString(udaljenost); 
    }
 
}
