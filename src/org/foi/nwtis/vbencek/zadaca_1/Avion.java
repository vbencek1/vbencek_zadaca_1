
package org.foi.nwtis.vbencek.zadaca_1;

import java.io.Serializable;


/**
 * Klasa Avion služi za pohranu informacija o avionu i može se serijalizirati.
 * @author Valentino Bencek
 */
public class Avion implements Serializable{
    
    public String oznakaAviona;
    public String oznakaPol;
    public String oznakaOdr;
    public String vrijemePolaska;
    public String vrijemeDolaska;
    
    
    
    /**
     * Metoda koja dodaje avion u listu aviona
     * @param oznakaA oznaka aviona
     * @param oznakaP oznaka aerodroma polazišta
     * @param oznakaO oznaka aerodroma odredišta
     * @param vp vrijeme polijetanja
     * @param vd vrijeme slijetanja
     */
    public static synchronized void dodajAvion(String oznakaA, String oznakaP, String oznakaO, String vp, String vd){
        Avion avion=new Avion();
        avion.oznakaAviona=oznakaA;
        avion.oznakaPol=oznakaP;
        avion.oznakaOdr=oznakaO;
        avion.vrijemePolaska=vp;
        avion.vrijemeDolaska=vd;
        ServerAviona.avioni.add(avion);
    }
    
    /**
     * Metoda koja provjerava postojanje aviona u listi
     * @param oznaka oznaka aviona
     * @return vraća true ako postoji i false ako ne postoji
     */
    public static synchronized boolean provjeriAkoAvionPostoji(String oznaka){
        for(Avion avion: ServerAviona.avioni){
            if(avion.oznakaAviona.equals(oznaka)){
                return true;
            }
        }return false;
    }
    
    /**
     * Metoda koja provjerava postojanje aviona u listi i provjerava ako su aerodromi isti kao oni učitani iz datoteke ili liste
     * @param oznaka oznava aviona
     * @param oznakaP oznaka aerodroma polazište
     * @param oznakaO oznaka aerodroma odredišta
     * @return vraća true ako postoji avion te ako su aerodromi polazišta i odredišta jednaki inače vraća false
     */
    public static synchronized boolean provjeriAkoAvionPostoji(String oznaka,String oznakaP, String oznakaO){
        for(Avion avion: ServerAviona.avioni){
            if(avion.oznakaAviona.equals(oznaka) && avion.oznakaPol.equals(oznakaP) && avion.oznakaOdr.equals(oznakaO)){
                return true;
            }
        }return false;
    }
    
    
    
}
