package org.foi.nwtis.vbencek.zadaca_1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Klasa KorisnikAviona predstavlja klasu klijenta koji šalje zahtjeve serveru putem parametara
 * @author Valentino Bencek
 * Program kao parametre prima ime korisnika, lozinku, adresu servera na koji se spaja te port preko kojeg se spaja.
 * Zatim prima jedan od četiri parametra: kraj, dodajDretve, uzletio i ispis.
 */

public class KorisnikAviona {
    private static String korisnik;
    private static String lozinka;
    private static String adresa;
    private static int port;
    private static int brojDretvi=0;
    private static int brojSek;
    private static String oznakaAvion;
    private static String oznakaO;
    private static String oznakaP;
    private static String parametar="";

/**
 * Glavna metoda klase KorsinikAviona.
 * Na početku od ulaznih argumenata gradi jedan string, zatim provjerava ulaznu sintaksu, broj argumenata te šalje poruku serveru i čeka na odgovor.
 * Argumenti moraju biti uneseni redom npr: -k admin -l foi -s localhost -p 9001 --kraj
 * @param args ulazni argumenti potrebni za pokretanje programa
 */    

    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        String parametri = sb.toString().trim();
        if(args.length<9 || args.length>10) {
            System.out.println("Neprimjeren broj parametara!");
            return;
        }    
        if(provjeraOpcija(parametri)){
            if(args.length==10)dodjeliParametreVarijablama(args[1],args[3],args[5],Integer.parseInt(args[7]),args[8],args[9]);
            if(args.length==9)dodjeliParametreVarijablama(args[1],args[3],args[5],Integer.parseInt(args[7]),args[8]);
        KorisnikAviona korisnikAviona= new KorisnikAviona();
        try (var socket = new Socket(adresa, port)) {
            korisnikAviona.posaljiPoruku(socket,korisnikAviona.formirajPoruku(korisnik,lozinka,parametar));
            korisnikAviona.primiPoruku(socket);
        } catch (IOException ex) {
            System.out.println("Spajanje na server neuspješno!");
        }
        } else {
            System.out.println("ERROR02; Neispravna sintaksa;");
            System.out.println("*Mora biti oblika: -k imeKorisnika -l lozinka -s adresa -p brojPorta...*");
        }
    }
    
    /**
     * Metoda koja provjerava ispravnost ulaznih parametara te vraća false ako sintaksa nije ispravna
     * @param ulaz string argumenata
     */
    
  
    private static boolean provjeraOpcija(String ulaz){
        String s1 = "^-k [A-Z,a-z0-9_-]{3,10} -l [A-Z,a-z0-9-#!]{3,10} -s [A-Z,a-z0-9-#!.]{1,50} -p ([9][0-9][0-9][0-9]) ";
        String s2 = "(--kraj|--dodajDretve ([1-9]|[1][0-9]|20)|--uzletio AerodromPolazište: [A-Z,a-z0-9]{1,20}, AerodromOdredište: ";
        String s3 = "[A-Z,a-z0-9]{1,20}, Avion: [A-Z,a-z0-9-]{1,20}, trajanjeLeta: [0-9]{1,100}|--ispis [A-Z,a-z0-9]{1,20})$";
        String sintaksa=s1+s2+s3;
 
        Pattern pattern = Pattern.compile(sintaksa);
        Matcher m = pattern.matcher(ulaz);
        boolean status = m.matches();
        if (!status) {
            return false;
        }return true;
    }
    
    /**
     * Metoda koja dodijeljuje osnovne parametre varijablama. Ti parametri se upisuju prilikom svakog pozivanja programa
     * @param k korisničko ime  
     * @param l lozinka
     * @param s adresa servera
     * @param p broj porta
     */
    private static void dodijeliOsnovno(String k, String l, String s, int p) {
        korisnik = k;
        lozinka = l;
        adresa = s;
        port = p;
    }
    
    /**
     * Metoda koja dodijeljuje ulazne parametre varijablama klase. Metoda se poziva kad ima veći broj ulaznih parametra
     * odnosno kad zadnji parametar nije "kraj"
     * @param k korisničko ime
     * @param l lozinka
     * @param s adresa
     * @param p broj porta 
     * @param par ključni parametar, može biti dodajDretve, uzletio ili ispis
     * @param vrij  vrijednost koja je pridružena ključnom parametru. npr dodajDretve 9.
     */
    
    private static void dodjeliParametreVarijablama(String k, String l,String s, int p, String par, String vrij){
        dodijeliOsnovno(k,l,s,p);
        parametar=par;
        if(parametar.equals("--dodajDretve" )) brojDretvi=Integer.parseInt(vrij);
        if(parametar.equals("--uzletio" )) dodjeliParametreUzletio(vrij);
        if(parametar.equals("--ispis" )) oznakaAvion=vrij;
        
        
    }
    
    /**Metoda koja dodijeljuje ulazne parametre varijablama klase. Metoda se poziva kad ima manji broj ulaznih parametra, odnosno zadnji parametar je "kraj"
     * 
     * @param k korisničko ime
     * @param l lozinka
     * @param s adresa
     * @param p broj porta 
     * @param par ključni parametar, može biti samo kraj
     */
    private static void dodjeliParametreVarijablama(String k, String l,String s, int p, String par){
        dodijeliOsnovno(k,l,s,p);
        parametar=par;
        
    }
    
    /**
     * Metoda koja razdvaja uzlazni string na manje dijelove koje dodijeljuje varijablama klase
     * @param vrijednost parametar kojeg je potrebno razdvojiti
     */
    
    private static void dodjeliParametreUzletio(String vrijednost){
        String polje[]=vrijednost.split(" ");
        oznakaP=polje[1].substring(0, polje[1].length() - 1);;
        oznakaO=polje[3].substring(0, polje[3].length() - 1);;
        oznakaAvion=polje[5].substring(0, polje[5].length() - 1);;
        brojSek=Integer.parseInt(polje[7]);
    }
     
   /**
    * Metoda pomoću koje se šalju poruke serveru
    * @param socket socket na koji se treba spojiti i poslati poruku
    * @param poruka poruka koju treba poslati serveru
    * @throws IOException 
    */

    private void posaljiPoruku(Socket socket, String poruka) throws IOException {
        OutputStream os = socket.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(osw);
        bw.write(poruka);
        bw.flush();
        socket.shutdownOutput();

    }
    
    /**
     * Metoda koja služi za primanje poruke od servera
     * @param socket socket preko koje se prima poruka
     * @throws IOException 
     */
    
    private void primiPoruku(Socket socket) throws IOException{
        InputStream is = socket.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String poruka = br.readLine();  
        ispis(poruka);
        socket.shutdownInput();
    }
    
    /**
     * Metoda koja formira poruku koju će server primiti u oblik čitljiv serveru
     * @param korisnik korisničko ime
     * @param lozinka lozika
     * @param parametar parametar preko koje se prepoznaje o kakvoj se naredbi radi
     * @return 
     */
    
    private String formirajPoruku(String korisnik, String lozinka, String parametar){
        String glavni="KORISNIK "+ korisnik+"; LOZINKA "+lozinka+"; ";
        switch(parametar){
            case "--kraj":
                return glavni+"KRAJ;";
            case "--dodajDretve":
                return glavni+"DODAJ "+brojDretvi + ";";
            case "--uzletio": 
                return glavni+"UZLETIO "+ oznakaAvion+ "; POLAZIŠTE "+oznakaP+"; ODREDIŠTE "+oznakaO+"; TRAJANJE "+brojSek+";";
            case "--ispis":
                return glavni+"ISPIS "+oznakaAvion+";";
        }return "";
       
    }
    /**
     * Metoda ispisa teksta na ekran
     * @param tekst ulazni tekst
     */
    public synchronized void ispis(String tekst) {
            System.out.println(tekst);
        }

}
