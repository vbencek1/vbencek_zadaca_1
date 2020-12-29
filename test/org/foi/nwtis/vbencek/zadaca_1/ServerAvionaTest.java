/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.vbencek.zadaca_1;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author NWTiS_2
 */
public class ServerAvionaTest {
    
    public String datotekaKonf="NWTiS_vbencek_zadaca_1.txt";
    public String datotekaAer="aerodromi.csv";
    public String datotekaKor="korisnici.xml";
    public String datotekaAvio="avioni.bin";
    public ServerAvionaTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of main method, of class ServerAviona.
     */
    @Test 
    @Ignore
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        ServerAviona.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of provjeriBrojParametra method, of class ServerAviona.
     */
    @Test
    public void testProvjeriBrojParametra() {
        System.out.println("provjeriBrojParametra");
        int duljina = 3;
        ServerAviona instance = new ServerAviona();
        boolean expResult = true;
        boolean result = instance.provjeriBrojParametra(duljina);
        assertEquals(expResult, result);
    }

    /**
     * Test of provjeriParametre method, of class ServerAviona.
     */
    @Test
    public void testProvjeriParametre() {
        System.out.println("provjeriParametre");
        String par2 = "--brojDretvi";
        String vrij2 = "9";
        ServerAviona instance = new ServerAviona();
        boolean expResult = true;
        boolean result = instance.provjeriParametre(datotekaKonf, par2, vrij2);
        assertEquals(expResult, result);
    }

    /**
     * Test of provjeriAkoPostojiKonf method, of class ServerAviona.
     */
    @Test 
    public void testProvjeriAkoPostojiKonf() {
        System.out.println("provjeriAkoPostojiKonf");
        ServerAviona instance = new ServerAviona();
        boolean expResult = true;
        boolean result = instance.provjeriAkoPostojiKonf(datotekaKonf);
        assertEquals(expResult, result);
    }

    /**
     * Test of provjeriAkoPostojeDat method, of class ServerAviona.
     */
    @Test
    public void testProvjeriAkoPostojeDat() {
        System.out.println("provjeriAkoPostojeDat");
        ServerAviona instance = new ServerAviona();
        boolean expResult = true;
        boolean result = instance.provjeriAkoPostojeDat(datotekaAer, datotekaKor);
        assertEquals(expResult, result);
    }

    /**
     * Test of ucitajDatotekuAviona method, of class ServerAviona.
     */
    @Test
    public void testUcitajDatotekuAviona() {
        System.out.println("ucitajDatotekuAviona");
        ServerAviona instance = new ServerAviona();
        try{
        instance.ucitajDatotekuAviona(datotekaAvio);
        }catch(Exception e){
            fail();
        }
        
    }

    /**
     * Test of ucitajDatotekuKonfiguracije method, of class ServerAviona.
     */
    @Test 
    public void testUcitajDatotekuKonfiguracije() {
        System.out.println("ucitajDatotekuKonfiguracije");
        ServerAviona instance = new ServerAviona();
        try{
        instance.ucitajDatotekuKonfiguracije(datotekaKonf);
        }catch(Exception e){
            fail();
        }
    }

    /**
     * Test of ucitajAvione method, of class ServerAviona.
     */
    @Test
    public void testUcitajAvione() {
        System.out.println("ucitajAvione");
        ServerAviona instance = new ServerAviona();
        try{
        instance.ucitajAvione(datotekaAvio);
        }catch(Exception e){
            fail();
        }
    }

    /**
     * Test of provjeriDostupnostPorta method, of class ServerAviona.
     */
    @Test
    public void testProvjeriDostupnostPorta() {
        System.out.println("provjeriDostupnostPorta");
        int port = 9001;
        boolean expResult = true;
        boolean result = ServerAviona.provjeriDostupnostPorta(port);
        assertEquals(expResult, result);
    }

    /**
     * Test of kreirajGrupuDretvi method, of class ServerAviona.
     */
    @Test 
    public void testKreirajGrupuDretvi() {
        System.out.println("kreirajGrupuDretvi");
        ServerAviona instance = new ServerAviona();
        int interval=10;
        try{
        instance.kreirajGrupuDretvi(interval,datotekaAvio);
        }catch(Exception e){
            fail();
        }
    }

    /**
     * Test of izvrsi method, of class ServerAviona.
     */
    @Test @Ignore
    public void testIzvrsi() {
        System.out.println("izvrsi");
        int port = 0;
        int brCekaca = 0;
        ServerAviona instance = new ServerAviona();
        instance.izvrsi(port, brCekaca);
    }

    /**
     * Test of zavrsetakRada method, of class ServerAviona.
     */
    @Test
    public void testZavrsetakRada() {
        System.out.println("zavrsetakRada");
        try{
        ServerAviona.zavrsetakRada();
        }catch(Exception e){
            fail();
        }
    }
    
}
