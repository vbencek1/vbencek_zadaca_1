
package org.foi.nwtis.vbencek.zadaca_1;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.vbencek.konfiguracije.Konfiguracija;
import org.foi.nwtis.vbencek.konfiguracije.KonfiguracijaApstraktna;

/**
 * Klasa koja se ponaša kao server zadužen za simulaciju leta, a komunicira samo na serverom aviona
 * @author Valentino Bencek
 */

public class ServerSimulatoraLeta {

    private static int portSimulator;
    private static int brojCekaca;
    
    /**
     * Glavna metoda koja provjerava broj parametra, dostupno konfiguracije ,porta te čeka zahtjeve od servera aviona
     * @param args datoteka konfiguracije je jedeini parametar
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
    if(args.length!=1){
        System.out.println("Neprimjeren broj parametra!");
        return;
    }    
    
    ServerSimulatoraLeta server= new ServerSimulatoraLeta();
    if(!server.provjeriParametre(args[0])){     
            return;
        }
    if(!server.provjeriAkoPostojiKonf(args[0])){
            return;
        }
    server.ucitajDatotekuKonfiguracije(args[0]);
    if(!server.provjeriDostupnostPorta(portSimulator)){      
            return;
        }
    server.izvrsi(portSimulator,brojCekaca);
    }
    
     /**
     * Metoda koja provjerava postojanje konfiguracijske datoteke
     * @param datoteka ulazna datoteka
     * @return  vraća true ako je uredu inače vraća false
     */
    
    public boolean provjeriAkoPostojiKonf(String datoteka){
        File file = new File(datoteka);
        boolean status=file.exists();
        if(!status) System.out.println("Datoteka '"+ datoteka+"' ne postoji!");
        
        return status;
    }
    
    /**
     * Metoda koja provjerava ulazni parametar odnosno da li datoteka ima pravilnu ekstenziju
     * @param datoteka ulazna datoteka
     * @return vraća true ako je uredu inače vraća false
     */
    
    public boolean provjeriParametre(String datoteka){
        String sintaksa = "^[a-z0-9A-Z-_]{1,50}\\.((?i)txt|xml|json|bin)$";   
        Pattern pattern = Pattern.compile(sintaksa);
        Matcher m = pattern.matcher(datoteka);
        boolean status = m.matches();
        if(!status){
            System.out.println("Sintaksa mora biti oblika: {nazivDatoteke}.{xml|txt|json|bin}");
        }
        return status;
    }
    
    /**
     * Metoda koja učitava datoteku konfiguracija te pridružuje podatke iz datoteke varijablama klase
     * @param naziv ulazna datoteka
     */
    public void ucitajDatotekuKonfiguracije(String naziv) {
        try {
            Konfiguracija konfiguracija = null;
            konfiguracija = KonfiguracijaApstraktna.preuzmiKonfiguraciju(naziv);
            portSimulator = Integer.parseInt(konfiguracija.dajPostavku("port.simulator"));
            brojCekaca = Integer.parseInt(konfiguracija.dajPostavku("maks.cekaca"));
        } catch (Exception e) {
            System.out.println("Pogreška u čitanju konfiguracije!");
            System.exit(0);
        }
    }
    
    /**
     * Metoda koja provjerava da li je dostupan port na koji se želi pokrenuti server
     * @param port broj porta
     * @return vraća status true ili fasle
     */
    public boolean provjeriDostupnostPorta(int port) {
        boolean slobodan;
        try (ServerSocket isprobajPort= new ServerSocket(port)) {
            slobodan = true;
        } catch (IOException e) {
            System.out.println("Port je zauzet!");
            slobodan = false;
        }
        return slobodan;
    }
    
    
    /**
    * Metoda koja vrti beskonačnu petlju u kojoj se čeka zahtjev servera, zahtjev se zatim prosljeđuje dretvi koja ga obrađuje
    * @param port broj porta
    * @param maksCekaca maksimalni broj čekača
    * @throws IOException 
    */
    public void izvrsi(int port,int maksCekaca) throws IOException{
        try (var server = new ServerSocket(port,maksCekaca)) {
            System.out.println("Server se pokrece...");
            
            while (true) {
                Thread dretva= new ZahtjevLeta(server.accept());
                dretva.start();
                try {
                    dretva.join();
                } catch (InterruptedException ex) {
                    System.out.println("Greška prilikom obrade zahtjeva");
                }
            }
            
        }

    }
}
