package org.foi.nwtis.vbencek.zadaca_1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.vbencek.konfiguracije.Konfiguracija;
import org.foi.nwtis.vbencek.konfiguracije.KonfiguracijaApstraktna;

/**
 * Klasa ServerAviona ponaša se kao server koji prima zahtjeve od korisnika. Klasa preko komandne linije prima dva parametra: datoteku konfiguracije iz koje čita podatke i broj dretvi
 * @author Valentino Bencek
 */

public class ServerAviona {

    public static String datotekaKorisnika;
    public static String datotekaAvioni;
    public static String datotekaAerodromi;
    public static int portAvioni;
    public static int portSimulator;
    public static int maksCekaca;
    public static int brojDretvi;
    public static int brojAktivnih=0;
    public static int intervalPohrane;
    public static boolean prekid=false;
    public static ServerSocket server;
    
    /**
     * lista aviona koji se učitaju iz binarne datoteke
     */
    public static List<Avion> avioni = new ArrayList<>();

    
    /**
     * Glavna metoda koja na početku provjerava broj parametara, zatim sintaksu unesenih parametara, 
     * provjerava postojanje konfiguracijske datoteke i ostalih datoteka te priprema serverske dretve i dretve za prijam korisnika.
     * @param args ulazni parametar: datoteka konfiguracije i broj dretvi
     */
    public static void main(String[] args) {       
        ServerAviona serverAviona = new ServerAviona();
        if(!serverAviona.provjeriBrojParametra(args.length)){
            return;
        }      
        if(!serverAviona.provjeriParametre(args[0], args[1], args[2])){     
            return;
        }
        if(!serverAviona.provjeriAkoPostojiKonf(args[0])){
            return;
        }
        serverAviona.ucitajDatotekuKonfiguracije(args[0]);
        if(!serverAviona.provjeriAkoPostojeDat(datotekaAerodromi,datotekaKorisnika)){
            return;
        }
        if(!provjeriDostupnostPorta(portAvioni)){      
            return;
        }
        zavrsetakRada();
        Aerodrom.spremiPodatke(datotekaAerodromi);
        Korisnik.spremiPodatke(datotekaKorisnika); 
        serverAviona.ucitajDatotekuAviona(datotekaAvioni);
        brojDretvi=Integer.parseInt(args[2]);
        serverAviona.kreirajGrupuDretvi(intervalPohrane,datotekaAvioni);
        serverAviona.izvrsi(portAvioni,maksCekaca);
    }
    
    /**
     * Metoda koja provjeraava broj ulaznih parametara
     * @param duljina broj parametara
     * @return vraća true ako je duljina u redu inače vraća false
     */
    public boolean provjeriBrojParametra(int duljina){
        if(duljina !=3){
            System.out.println("Nedovoljan broj parametra!");
            System.out.println("Ulaz mora biti oblika: {nazivDatoteke}.{xml|txt|json|bin} --brojDretvi {broj}");
            return false;
        }else{
            return true;
        }
    }
    
    /**
     * Metoda koja provjerava sintaksu unesenih parametara pomoću regularnih izraza
     * @param datoteka prvi parametar: konfiguracijska datoteka
     * @param par2 drugi parametar je --brojDretvi
     * @param vrij2 treci parametar je vrijednost brojaDretvi
     * @return vraća status odnosno true ili false
     */
    public boolean provjeriParametre(String datoteka, String par2, String vrij2){
        String sintaksa = "^[a-z0-9A-Z-_]{1,50}\\.((?i)txt|xml|json|bin) --brojDretvi [0-9]{1,5}$";    
        String p = datoteka+" "+par2+" "+vrij2.trim();
        Pattern pattern = Pattern.compile(sintaksa);
        Matcher m = pattern.matcher(p);
        boolean status = m.matches();
        if(!status){
            System.out.println("Sintaksa mora biti oblika: {nazivDatoteke}.{xml|txt|json|bin} --brojDretvi {broj}");
        }
        return status;
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
     * Metoda koja provjerava postojanje datoteka sa korisnicima i aerodromima
     * @param aerodromi ulazna datoteka sa aerodromima
     * @param korisnici ulazna datoteka sa korisnicima
     * @return vraća true ako postoje obje datoteke inače vraća false
     */
    public boolean provjeriAkoPostojeDat(String aerodromi, String korisnici){
        try{
        File aer = new File(aerodromi);
        File kor = new File(korisnici);
        boolean statusAer=aer.exists();
        boolean statusKor=kor.exists();
        if(!statusAer) System.out.println("Datoteka '"+ aer+"' ne postoji!");
        if(!statusKor) System.out.println("Datoteka '"+ kor+"' ne postoji!");
        return statusAer && statusKor;
        }catch(Exception e){
            System.out.println("Pogreška u čitanju konfiguracije!");
            return false;
        }
    }
    
    /**
     * Metoda koja učitava datoteku sa serijaliziranim avionima
     * @param datoteka ulazna datoteka
     */
    public void ucitajDatotekuAviona(String datoteka){
        try{
        File file = new File(datoteka);
        boolean status=file.exists();
        if(!status) System.out.println("Datoteka '"+ datoteka+"' ne postoji!");
        else{
            ucitajAvione(datoteka);
        }
        }catch(Exception e){
            System.out.println("Pogreška u čitanju konfiguracije!");
            
        }
    }

    /**
     * Metoda koja učitava datoteku konfiguracija te pridružuje podatke iz datoteke varijablama klase
     * @param naziv ulazna datoteka
     */
    public void ucitajDatotekuKonfiguracije(String naziv) {
        try {
            Konfiguracija konfiguracija = null;
            konfiguracija = KonfiguracijaApstraktna.preuzmiKonfiguraciju(naziv);

            datotekaAvioni = konfiguracija.dajPostavku("datoteka.avioni");
            datotekaAerodromi = konfiguracija.dajPostavku("datoteka.aerodromi");
            datotekaKorisnika = konfiguracija.dajPostavku("datoteka.korisnika");
            portAvioni = Integer.parseInt(konfiguracija.dajPostavku("port.avioni"));
            portSimulator = Integer.parseInt(konfiguracija.dajPostavku("port.simulator"));
            maksCekaca = Integer.parseInt(konfiguracija.dajPostavku("maks.cekaca"));
            intervalPohrane= Integer.parseInt(konfiguracija.dajPostavku("interval.pohrane.aviona"));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Pogreška u čitanju konfiguracije!");
            System.exit(0);
        }
    }
    
    /**
     * Metoda koja učitava avione iz datoteke i ispisuje ih na ekran, također učitane avione dodaje u listu kako bi se sa njima moglo raditi
     * @param datoteka ulazna datoteka
     */
    public void ucitajAvione(String datoteka){
        Avion avion = null;
        try {
            System.out.println("Učitavam datoteku aviona: " + datoteka + " ...");
            FileInputStream dat = new FileInputStream(datoteka);
            ObjectInputStream in = new ObjectInputStream(dat);
            while ((avion = (Avion) in.readObject()) != null) {
                System.out.println("-------------------------------------------");
                System.out.println("OznakaAviona: " + avion.oznakaAviona);
                System.out.println("Oznaka aerodroma polazišta: " + avion.oznakaPol);
                System.out.println("Oznaka aerodroma odredišta: " + avion.oznakaOdr);
                System.out.println("Vrijeme polaska: " + avion.vrijemePolaska);
                System.out.println("Vrijeme dolaska: " + avion.vrijemeDolaska);
                System.out.println("-------------------------------------------");
                
                Avion.dodajAvion(avion.oznakaAviona, avion.oznakaPol, avion.oznakaOdr, avion.vrijemePolaska, avion.vrijemeDolaska);
            }
            in.close();
            dat.close();
        } catch (IOException i) {
            System.out.println("Datoteka: "+ datoteka+" pročitana!");    
        } catch (ClassNotFoundException c) {
            System.out.println("Klasa: Avion ne postoji");
        }
    }
    

    /**
     * Metoda koja provjerava da li je dostupan port na koji se želi pokrenuti server
     * @param port broj porta
     * @return vraća status true ili fasle
     */
    public static boolean provjeriDostupnostPorta(int port) {
        boolean slobodan;
        try (ServerSocket isprobajPort= new ServerSocket(port)) {
            isprobajPort.close();
            slobodan = true;
        } catch (IOException e) {
            System.out.println("Port je zauzet!");
            slobodan = false;
        }
        
        return slobodan;
    }
    
    /**
     * Metoda koja kreira grupu dretvi i servisnu dretvu za serializaciju aviona
     * @param interval interval izvršavanja zadataka dretve
     * @param datAvioni datoteka sa avionima
     */
    public void kreirajGrupuDretvi(int interval, String datAvioni){
        ThreadGroup tg = new ThreadGroup("vbencek_SD");   
        Timer timer= new Timer();
        ServisAviona t1 = new ServisAviona(tg,"vbencek_SD_1", datAvioni); 
        timer.schedule(t1, 0, interval*1000);
        
    }
   
   /**
    * Metoda koja vrti beskonačnu petlju u kojoj se čeka da se spoje korisnici, nakon spajanja pridružuje im se dretva koja obrađuje njihove zahtjeve
    * U petlji se također čeka signal dretve koja šalje zahtjev za prekid odnosno gašenje servera i zatvara vezu sa korisnikom
    * @param port broj porta
    * @param brCekaca maksimalni broj čekača
    */
    
    public void izvrsi(int port, int brCekaca) {
        try {
            server = new ServerSocket(port, brCekaca);
            while (!prekid) {
                try {
                    Socket socket = server.accept();
                    Thread dretva = new ZahtjevAviona(socket);
                    dretva.start();
                } catch (SocketException e) {
                    break;
                }
            }
            server.close();
            System.exit(0);
        } catch (IOException e) {
            System.out.println("Socket je zatvoren!");
        }
    } 
  
    
    /**
     * Metoda koja vrši poslove servera prilikom njegova gašenja
     */
    final public static void zavrsetakRada() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {            
                try {    
                    Timer timer= new Timer();
                    ServisAviona t1 = new ServisAviona(datotekaAvioni);
                    timer.schedule(t1, 0);
                    sleep(1000);
                } catch (Exception ex) {
                    System.out.println("Greška u serializaciji prilikom gašenja servera");
                }
                
                System.out.println("Server završava rad.");
            }
        });
    }
    
    
}
