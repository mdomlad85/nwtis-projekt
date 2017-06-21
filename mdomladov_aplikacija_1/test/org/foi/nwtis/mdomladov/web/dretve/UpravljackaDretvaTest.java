/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.dretve;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mdomladov.konfiguracije.APP_Konfiguracija;
import org.foi.nwtis.mdomladov.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.mdomladov.konfiguracije.NemaKonfiguracije;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Zeus
 */
public class UpravljackaDretvaTest {

    private APP_Konfiguracija konfiguracija;

    private UpravljackaDretva instance;

    public UpravljackaDretvaTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        try {
            konfiguracija = new APP_Konfiguracija("NWTiS.db.config_1.xml");
            /*
            instance = new UpravljackaDretva(konfiguracija);
            instance.start();
             */
        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            Logger.getLogger(UpravljackaDretvaTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of run method, of class UpravljackaDretva.
     */
    @Test
    public void testStart() {
        InputStream is = null;
        OutputStream os = null;
        Socket socket = null;
        StringBuffer sb = null;

        try {
            socket = new Socket("localhost", konfiguracija.getSocketPort());
            os = socket.getOutputStream();
            is = socket.getInputStream();
            os.write("USER mdomladov; PASSWD 123456; START;".getBytes());
            os.flush();
            socket.shutdownOutput();
            sb = new StringBuffer();
            int znak;
            do {
                znak = is.read();
                sb.append((char) znak);
            } while (znak != -1);

            System.out.println(sb.toString());

        } catch (IOException | NumberFormatException ex) {
            Logger.getLogger(UpravljackaDretvaTest.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Neispravna naredba");

        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }

            } catch (IOException ex) {
                Logger.getLogger(UpravljackaDretvaTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        assertNotNull(sb);
    }
}
