package org.foi.nwtis.vbencek.zadaca_1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Klasa ZahtjevAviona zapravo je dretva koja je zadužena za obradu korisničkih zahtjeva
 * @Valentino Bencek
 */
public class ZahtjevAviona extends Thread {

    private Socket socket;

    public ZahtjevAviona(Socket socket) {
        this.socket = socket;
    }

    /**
     * Metoda koju dretva vrši svaki put kad korisnik pošalje neki zahtjev.
     * Prvo provjerava da li ima još razpoloživih dretvi a zatim obrađuje zahtjev koji je primila
     */
    @Override
    public void run() {
        if(!ServerAviona.prekid){
        System.out.println("Spojen: " + socket);
        if (ServerAviona.brojAktivnih >= ServerAviona.brojDretvi) {
            try {
                posaljiPoruku(socket, "ERROR 01; nedovoljan broj raspoloživih dretvi;");
                return;
            } catch (IOException e) {
                System.out.println("Greška u komunikaciji");
            }
        }
        ServerAviona.brojAktivnih++;
        try {
            obradiPoruku(primiPoruku(socket));

        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Greška u komunikaciji");
        }
        ServerAviona.brojAktivnih--;
        }else{
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Metoda kojoj dretva prima zahtjev od korisnika ili prima poruku od simulatora leta
     * @param socket soket preko kojeg dobiva poruku
     * @return vraća se poruka od korisnika ili simulatora leta
     * @throws IOException
     * @throws ClassNotFoundException 
     */

    public String primiPoruku(Socket socket) throws IOException, ClassNotFoundException {
        InputStream is = socket.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        
        String poruka = br.readLine();
        socket.shutdownInput();
        System.out.println("Primljna poruka: "+poruka);
        return poruka;
        
    }

    /**
     * Metoda kojom dretva šalje poruku korisniku ili simulatoru leta
     * @param socket soket na koji se šalje poruka
     * @param poruka tekst poruke
     * @throws IOException 
     */
    public void posaljiPoruku(Socket socket, String poruka) throws IOException {
        OutputStream os = socket.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(osw);
        bw.write(poruka);
        bw.flush();
        socket.shutdownOutput();

    }

    
    /**
     * Metoda preko koje se korisnik autenticira te ako autentikacija prolazi poruka se šalje drugoj metodi u obradu, ako ne prolazi korisniku se šalje poruka
     * @param poruka ulazni tekst poruke koji se prima od korisnika
     */
    public void obradiPoruku(String poruka)  {
        String polje[] = poruka.split(";");
        String korisnik = polje[0].split(" ")[1];
        String lozinka = polje[1].split(" ")[2];
        if (autenticiraj(korisnik, lozinka)) {
            odrediKomandu(poruka);
            

        } else {
            try{
            posaljiPoruku(socket, "ERROR 03; Pogrešno ime ili lozinka;");
            }catch(Exception e){
                System.out.println("Greška u slanju poruke korisniku");
            }
        }
    }

    /**
     * Metoda autentikacije korisnika
     * @param korisnik korisničko ime
     * @param lozinka lozinka
     * @return vraća true ili false ovisno o točnim podacima
     */
    public boolean autenticiraj(String korisnik, String lozinka) {
        return Korisnik.provjeriImeiLozinku(korisnik, lozinka);

    }
    
    /**
     * Metoda koja obrađuje pojedinačne zahtjeve korisnika ovisno o poslanom zahtjevu. 
     * Metoda također odmah nakon obrade prosljeđuje poruku koju je dobio od simulatora leta korisniku.
     * Ako poruka nije bila namijenjena simulatoru ova metoda odmah šalje poruku natrag korisniku.
     * @param poruka  ulazna poruka
     */
    public void odrediKomandu(String poruka) {
        String polje[] = poruka.split(";");
        String komanda= polje[2].split(" ")[1];
        try {
        switch(komanda){
            case "KRAJ":
                posaljiPoruku(socket, obradiKraj());
                break;
            case "DODAJ":
                posaljiPoruku(socket, obradiDodaj(poruka));
                break;
            case "UZLETIO":
                posaljiPoruku(socket, obradiUzletio(socket.getInetAddress().getHostName(), ServerAviona.portSimulator,poruka));
                break;
            case "ISPIS":
                posaljiPoruku(socket, obradiIspis(socket.getInetAddress().getHostName(), ServerAviona.portSimulator,poruka));
                break;
            }
        } catch (Exception e) {

            System.out.println("Greska u prosljeđivanju poruke od simulatora leta prema korisniku");
        }
    }
    
    /**
     * Metoda koja se aktivira kada korisnik pošalje zahtjev koji sadrži kljunu riječ "ISPIS".
     * Poruka se preformulira i prosljeđuje se simulatoru leta od kojeg se zatim prima poruka
     * @param adresa adresa servera
     * @param port port servera
     * @param porukaIspis ulazna poruka od korisnika
     * @return vraća se poruka koju je poslao simulator leta
     */
    public String obradiIspis(String adresa, int port, String porukaIspis){
        String polje[] = porukaIspis.split(" ");
        String porukaSimulatoru= "POZICIJA: "+polje[5];
        try (var simulator = new Socket(adresa, port)) {
            posaljiPoruku(simulator,porukaSimulatoru);
            return primiPoruku(simulator);
        } catch (IOException | ClassNotFoundException ex) {
            return "ERROR16; spajanje na server simulatora leta neuspješno;";
        }
    }
    
    /**
     * Metoda koja se aktivira kada korisnik pošalje zahtjev koji sadrži kljunu riječ "UZLETIO".
     * Poruka se rastavlja na dijelove i izračunavaju se vremea polijetanja i slijetanja
     * Provjerava se korektnost podataka, te se dodaje novi avion u listu aviona ako on ne postoji
     * Poruka se  prosljeđuje se simulatoru leta.
     * @param adresa adresa servera
     * @param port port servera
     * @param porukaLeta poruka od korisnika
     * @return ako nešto nije u redu sa porukom (ne postoji aerodrom isli.) vraća se error korisniku, inače se prosljeđuje poruka simulatoru leta
     */
    public String obradiUzletio(String adresa, int port,String porukaLeta){
        String polje[] = porukaLeta.split(";");
        String oznakaAviona = polje[2].split(" ")[2];
        String oznakaPol = polje[3].split(" ")[2];
        String oznakaOdr = polje[4].split(" ")[2];
        int trajanje = Integer.parseInt(polje[5].split(" ")[2]);
        String[] vremena = izračunajTrajnjeLeta(trajanje);
        String vrijemePol = vremena[0];
        String vrijemeDol = vremena[1];
        if(!Aerodrom.provjeriAkoPostojiAeordrom(oznakaOdr) || !Aerodrom.provjeriAkoPostojiAeordrom(oznakaPol))
            return "ERROR13; Aerodrom sa navedenom oznakom ne postoji;";
        if(oznakaOdr.equals(oznakaPol))
            return "ERROR13; Aerodromi polazišta i odredišta dani parametrom su isti;";
        if(!Avion.provjeriAkoAvionPostoji(oznakaAviona)){          
            Avion.dodajAvion(oznakaAviona, oznakaPol, oznakaOdr, vrijemePol, vrijemeDol);
            return posaljiUzletioSimulatoru(adresa, port, oznakaAviona, trajanje, oznakaPol, oznakaOdr);
        }
        if(!Avion.provjeriAkoAvionPostoji(oznakaAviona,oznakaPol,oznakaOdr)){
            return "ERROR14; Podaci aerodroma polazišta i odredišta nisu isti kao u datoteci;";
        }else{
            return posaljiUzletioSimulatoru(adresa, port, oznakaAviona, trajanje, oznakaPol, oznakaOdr);
        }
    }
    
    /**
     * Metoda koja pomoću današnjeg datuma i trajanja leta izračunava vrijeme slijetanja aviona
     * @param trajanje trajanje leta
     * @return vraća trenutan datum i vrijeme te vrijeme slijetanja
     */
    public String[] izračunajTrajnjeLeta(int trajanje){
        DateFormat form = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	Date danasnjiDatum = new Date();
        Date kasnije = new Date( System.currentTimeMillis() + (trajanje*1000L));
        String vrijemePol= form.format(danasnjiDatum);
        String vrijemeDol= form.format(kasnije);
        String[] vremena={vrijemePol,vrijemeDol};
        return vremena;
    }
    
    /**
     * Metoda koja poziva metodu izračunavanja udaljenosti između aerodroma te se poruka formulira za prosljeđivanje korisniku
     * @param oznakaPol aerodrom polazišta
     * @param oznakaOdr aerodrom odredišta
     * @return formulirana poruka koja sadrži udaljenost
     */
    public String izračunajUdaljenost(String oznakaPol, String oznakaOdr){
        String udaljenost=Aerodrom.izračunajUdaljenost(oznakaPol, oznakaOdr);
        return "UDALJENOST: "+udaljenost+";";
    }
    
    /**
     * Metoda koja se koristi uz metodu obradiUzletio. Glavna svrha joj je preformulirati poruku u poruku čitljivu simulatoru leta.
     * Metoda šalje poruku te čeka na odgovor simulatora.
     * @param adresa adresa servera
     * @param port port servera
     * @param oznakaAviona oznaka aviona
     * @param trajanje trajanje leta
     * @param oznakaPol oznaka aerodroma polazišta
     * @param oznakaOdr oznaka aerodroma odredišta
     * @return vraća poruku primljenu od simulatora leta ako je sve u redu, inače vraća ERROR koji se prosljeđuje korisniku
     */
    public String posaljiUzletioSimulatoru(String adresa, int port, String oznakaAviona, int trajanje, String oznakaPol, String oznakaOdr){
        String[] vremena = izračunajTrajnjeLeta(trajanje);
        String vrijemePol = vremena[0];
        String vrijemeDol = vremena[1];
        String porukaSimulatoru= "LET "+oznakaAviona+"; POLIJETANJE "+vrijemePol+"; SLIJETANJE "+vrijemeDol+";";
            try (var simulator = new Socket(adresa, port)) {
                posaljiPoruku(simulator, porukaSimulatoru);
                String porukaOdSimulatora= primiPoruku(simulator);
                if("ERROR".equals(porukaOdSimulatora.split(" ")[0])){
                    return "ERROR 15; "+ porukaOdSimulatora.split(";")[1].trim()+";";
                }
                else{
                    return porukaOdSimulatora+izračunajUdaljenost(oznakaPol, oznakaOdr);
                }
            } catch (IOException | ClassNotFoundException ex) {
                return "ERROR16; spajanje na server simulatora leta neuspješno;";
            }
    }
    
    /**
     * Metoda koja se poziva kada korisnik pošalje zahtjev sa ključnom riječi "KRAJ".
     * Dretva šalje zahtjev za prekida rad servera te zatvara socket servera koji čeka zahtjeve korisnika
     * @return vraća OK korisniku ako je sve uredu inače vraća ERROR
     */
    public String obradiKraj(){
        ServerAviona.prekid=true;
        try {
            ServerAviona.server.close();
        } catch (IOException ex) {
            return "ERROR 11; Greška u prekidu rada;";
        }
        return "OK;";
    }
    
    /**
     * Metoda koja se poziva kada korisnik pošalje zahtjev sa ključnom riječi "DODAJ".
     * Metoda povećava maksimalni broj dretvi koje postoje u sustavu, a služe za obradu zahtjeva korisnika
     * @param poruka vraća OK korisniku ako je sve uredu inače vraća ERROR
     * @return 
     */
    public String obradiDodaj(String poruka){
        try {
            String polje[] = poruka.split(" ");
            int brojDretvi = Integer.parseInt(polje[5].substring(0,polje[5].length() - 1));
            ServerAviona.brojDretvi = ServerAviona.brojDretvi + brojDretvi;
            System.out.println("Korisnik je dodao "+brojDretvi+" novih dretvi. Trenutni broj: "+ServerAviona.brojDretvi);
            return "OK;";
        } catch (Exception e) {
            return "ERROR 12; Greška prilikom dodavanja dretvi;";
        }
    }
  

}
