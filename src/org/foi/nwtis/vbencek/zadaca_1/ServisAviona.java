package org.foi.nwtis.vbencek.zadaca_1;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.TimerTask;

/**
 * Klasa ServisAviona ponaša se kao dretva sa periodičim izvršavanjem, a služi za serijalizaciju podataka o avionima
 * Dretva u određenom intervala sprema podatke o avionima u datoteku
 * @author Valentino Bencek
 */
public class ServisAviona extends TimerTask {

        
    private String datoteka;
    
    ServisAviona(String dat) {
        datoteka=dat;
    }
    ServisAviona(ThreadGroup tg, String imeD, String dat) {
        ThreadGroup group=tg;
        String ime=imeD;
        datoteka=dat;
    }
    
    /**
     * Dretva u izvršavanju poziva metodu serijalizacije aviona
     */
    @Override
    public void run() {
        serijalizirajAvione(datoteka);   
        System.out.println("Podaci serijalizirani u: "+new Date());
    }
    
    /**
     * Metoda koja služi za pohranu informacija o avionu u binarnu datoteku.
     * Ako datoteka ne postoji ona se kreira te iz aktivne liste aviona zapisuje podakte u datoteku
     * @param datoteka ulazna datoteka
     */
    private synchronized void serijalizirajAvione(String datoteka) {     
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(datoteka);
        } catch (FileNotFoundException ex) {
            System.out.println("Datoteka: " + datoteka + " ne postoji!");}
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(fileOut);
        } catch (IOException ex) {
            System.out.println("Došlo je do pogreške prilikom serijalizacije");}
        for (Avion avion : ServerAviona.avioni) {
            try {
               if(!ServerAviona.avioni.isEmpty()){
                   out.writeObject(avion);
               }                
            } catch (IOException ex) {
                System.out.println("Došlo je do pogreške prilikom serijalizacije");
            }}
        try {
            out.close();
            fileOut.close();
        } catch (IOException ex) {
            System.out.println("Došlo je do pogreške prilikom serijalizacije");
        }}  
    
   
}
