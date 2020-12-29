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
 * Klasa ZahtjevLeta zapravo je dretva koja obrađuje zahtjeve koje šalje server aviona
 * @author Valentino Bencek
 */

public class ZahtjevLeta extends Thread{
     private Socket socket;
     
     public ZahtjevLeta(Socket socket) {
        this.socket = socket;
    }

     /**
      * Dretva prilikom izvršavanja obrađuje poruku koju prima od servera aviona
      */
    @Override
    public void run() {
        System.out.println("Spojen: " + socket);      
         try {
             provjeriSintaksuIObradiPoruku(primiPoruku(socket));
         } catch (Exception ex) {
             System.out.println("Greška u primanju poruke od servera aviona");
         }
        
    }
    
    /**
     * Metoda koja dodaje let u kolekciju letova 
     * @param avion oznaka aviona
     * @param vrijemeP vrijeme polijetanja aviona
     * @param VrijemeS  vrijeme slijetanja aviona
     */
    
    public void dodajLet(String avion, String vrijemeP, String VrijemeS){
        Let.spremiPodatke(avion, vrijemeP, VrijemeS);
    }
    
    /**
     * Metoda koja ispisuje status leta aviona koji prima od servera preko zahtjeva: "POZICIJA: avion24"
     * @param poruka ulazna poruka od servera
     * @return vraća status LETI, SLETIO ili ERROR ako nešto nije u redu
     */
    public String ispisLeta(String poruka){
        String str=poruka.split(" ")[1];
        String avion=str.substring(0, str.length() - 1);
        return Let.ispis(avion);
    }
    
    /**
     * Metoda koja dodaje let u kolekciju letova,a informacije dobiva od servera aviona preko komande "LET"
     * @param poruka ulazna poruka od servera
     * @return vraća OK ako je sve uredu inače vraća ERROR
     */
    public String dodajLet(String poruka){   
        String[] str=poruka.split(";");  
        String avion=str[0].substring(4);
        String vrijemeP=str[1].substring(13);
        String vrijemeS=str[2].substring(12);
        if(!Let.provjeriAkoAvionPostoji(avion)) {
            Let.spremiPodatke(avion, vrijemeP, vrijemeS);
            return "OK;";
        }
        if(Let.provjeriAkoAvionPostoji(avion)){
            if(Let.provjeriAkoAvionSletio(avion)){
                Let.spremiPodatke(avion, vrijemeP, vrijemeS);
                return "OK;";
            }else{
                return"ERROR 21; Nije moguće simulirati let, avion: "+ avion +" još leti;";
            }
        }
        return "ERROR 22; Greška u simuliranju leta;";
    }
    
    /**
     * Metoda koja provjerava sintaksu te ovisno o sintaksi odlučuje koju će metodu pozvati
     * Metoda također šalje poruku serveru ako je sintaksa u redu, ako nije šalje se ERROR
     * @param sintaksa 
     */
    public void provjeriSintaksuIObradiPoruku(String sintaksa){
        String avion="^LET [A-z0-9]{1,30}; ";
        String polijetanje="POLIJETANJE (19|20)[0-9][0-9][/.](0[1-9]|1[012])[/.](0[1-9]|[12][0-9]|3[01]) ([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]; ";
        String slijetanje="SLIJETANJE (19|20)[0-9][0-9][/.](0[1-9]|1[012])[/.](0[1-9]|[12][0-9]|3[01]) ([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9];$";
        String pozicija="^POZICIJA: [A-z0-9]{1,30};$";
        Pattern patternLet = Pattern.compile(avion+polijetanje+slijetanje);
        Pattern patternPoz = Pattern.compile(pozicija);
        Matcher prvi = patternLet.matcher(sintaksa);
        Matcher drugi = patternPoz.matcher(sintaksa);
        try{
        if (!prvi.matches() && !drugi.matches()) 
            posaljiPoruku(socket, "ERROR 20; Pogrešna sintaksa;");
        if(prvi.matches())
            posaljiPoruku(socket, dodajLet(sintaksa));
        if(drugi.matches())
            posaljiPoruku(socket, ispisLeta(sintaksa));
        }catch(Exception e){
            System.out.println("Pogreška prilikom slanja poruke serveru aviona");
        }
       
    }
    
     /**
     * Metoda kojom dretva šalje poruku serveru aviona
     * @param socket soket na koji se šalje poruka
     * @param poruka tekst poruke
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
     * Metoda kojoj dretva prima zahtjev od servera aviona
     * @param socket soket preko kojeg dobiva poruku
     * @return vraća se poruka serveru aviona
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public String primiPoruku(Socket socket) throws IOException, ClassNotFoundException {
        InputStream is = socket.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        
        String poruka = br.readLine();
        socket.shutdownInput();
        System.out.println("Primljena poruka: "+poruka);
        return poruka;
        
    }
}
