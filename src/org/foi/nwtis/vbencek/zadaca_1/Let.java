/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.vbencek.zadaca_1;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Klasa Let djeluje kao kolekcija koju koristi SimulatorLeta prilikom svojeg izvršavanja i tu pohranjuje svoje letove
 * @author Valentino Bencek
 */

public class Let {
    
    private String avion;
    private String vrijemePolaska;
    private String vrijemeSlijetanja;
    
    /**
     *lista koja sprema letove koje koristi SimulatorLetova
     */
    
    public static List<Let> letovi = new ArrayList<>();
    
    /**
     * Metoda koja sprema informacije o letu u listu
     * @param avion oznaka aviona 
     * @param vrijemeP vrijeme polijetanja
     * @param vrijemeS vrijeme slijetanja
     */
    public static void spremiPodatke(String avion, String vrijemeP, String vrijemeS) {
        Let let= new Let();
        let.avion = avion;
        let.vrijemePolaska = vrijemeP;
        let.vrijemeSlijetanja = vrijemeS;
        letovi.add(let);
        
    }
    /**
     * Metoda koja serveru vraća odgovor na poslanu komandu: POZICIJA: avion; 
     * Provjerava da li je vrijeme slijetanja aviona prije današnjeg datuma te vraća primjerenu poruku
     * @param avion oznaka aviona
     * @return vraća OK;SLETIO; ako je datum slijetanja već prošao inače vraća OK; LETI;
     */
    
    public static String ispis(String avion) {
        DateFormat form = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	Date danasnjiDatum = new Date();
        for (Let let : letovi) {
            if(let.avion.equals(avion)){
                String datum=let.vrijemeSlijetanja;
                try {
                    if(form.parse(datum).before(danasnjiDatum))
                        return "OK; SLETIO;";
                    else
                        return "OK; LETI;";
                } catch (Exception ex) {
                    return "ERROR 23; Greška u uspoređivanju datuma;";
                }
            }
            
        }return "ERROR 23; Avion '"+avion+"' ne postoji u kolekciji letova;";
                
    }
    
    /**
     * Metoda koja provjerava ako postoji avion koji leti ili koji je sletio
     * @param oznaka oznaka aviona
     * @return vraća true ako postoji inače vraća false
     */
    public static boolean provjeriAkoAvionPostoji(String oznaka){
        for(Let let:letovi){
            if(let.avion.equals(oznaka)){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Metoda koja provjerava da li je avion sletio
     * Provjerava da li je vrijeme slijetanja aviona prije današnjeg datuma
     * Ako je avion sletio tada se obrišu podaci kako bi se mogao dodati novi let
     * @param oznaka oznaka aviona
     * @return vraća true ako je sletio inače vraća false
     */
    public static boolean provjeriAkoAvionSletio(String oznaka){
        DateFormat form = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	Date danasnjiDatum = new Date();
        for(Let let:letovi){
            if(let.avion.equals(oznaka)){
            String datum=let.vrijemeSlijetanja;
            try {
                if(form.parse(datum).before(danasnjiDatum)){
                    obrisiLet(oznaka);
                    return true;
                }else{
                    return false;
                }
            } catch (ParseException ex) {
                return false;
            }
        }}
        return false;
    }
    
    /**
     * Metoda koja koja uklanja let iz liste letova
     * @param oznaka oznaka aviona
     */
    public static void obrisiLet(String oznaka){
        for (Iterator<Let> iter = letovi.listIterator(); iter.hasNext();) {
            Let a = iter.next();
            if (a.avion.equals(oznaka)) {
                iter.remove();
            }
        }
    }
    
    
       /**
        * Metoda koja ispisuje sve letove iz liste letova
        */
       public static void ispisA(){
           for(Let l: letovi){
               System.out.println(l.avion+" "+l.vrijemePolaska+" "+l.vrijemeSlijetanja);
           }
       }

}
