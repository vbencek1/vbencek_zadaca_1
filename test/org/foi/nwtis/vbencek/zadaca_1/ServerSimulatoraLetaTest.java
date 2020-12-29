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
public class ServerSimulatoraLetaTest {
    
    public String datotekaKonf="NWTiS_vbencek_zadaca_1.txt";
    
    public ServerSimulatoraLetaTest() {
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
     * Test of main method, of class ServerSimulatoraLeta.
     */
    @Test 
    @Ignore
    public void testMain() throws Exception {
        System.out.println("main");
        String[] args = null;
        ServerSimulatoraLeta.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of provjeriAkoPostojiKonf method, of class ServerSimulatoraLeta.
     */
    @Test
    public void testProvjeriAkoPostojiKonf() {
        System.out.println("provjeriAkoPostojiKonf");
        ServerSimulatoraLeta instance = new ServerSimulatoraLeta();
        boolean expResult = true;
        boolean result = instance.provjeriAkoPostojiKonf(datotekaKonf);
        assertEquals(expResult, result);
    }

    /**
     * Test of provjeriParametre method, of class ServerSimulatoraLeta.
     */
    @Test
    public void testProvjeriParametre() {
        System.out.println("provjeriParametre");
        ServerSimulatoraLeta instance = new ServerSimulatoraLeta();
        boolean expResult = true;
        boolean result = instance.provjeriParametre(datotekaKonf);
        assertEquals(expResult, result);
    }

    /**
     * Test of ucitajDatotekuKonfiguracije method, of class ServerSimulatoraLeta.
     */
    @Test
    public void testUcitajDatotekuKonfiguracije() {
        System.out.println("ucitajDatotekuKonfiguracije");
        ServerSimulatoraLeta instance = new ServerSimulatoraLeta();
        try{
        instance.ucitajDatotekuKonfiguracije(datotekaKonf);
        }catch(Exception e){
            fail();
        }
    }

    /**
     * Test of provjeriDostupnostPorta method, of class ServerSimulatoraLeta.
     */
    @Test 
    public void testProvjeriDostupnostPorta() {
        System.out.println("provjeriDostupnostPorta");
        int port = 9001;
        ServerSimulatoraLeta instance = new ServerSimulatoraLeta();
        boolean expResult = true;
        boolean result = instance.provjeriDostupnostPorta(port);
        assertEquals(expResult, result);
    }

    /**
     * Test of izvrsi method, of class ServerSimulatoraLeta.
     */
    @Test
    @Ignore
    public void testIzvrsi() throws Exception {
        System.out.println("izvrsi");
        int port = 9001;
        int maksCekaca = 5;
        ServerSimulatoraLeta instance = new ServerSimulatoraLeta();
        instance.izvrsi(port, maksCekaca);
    }
    
}
