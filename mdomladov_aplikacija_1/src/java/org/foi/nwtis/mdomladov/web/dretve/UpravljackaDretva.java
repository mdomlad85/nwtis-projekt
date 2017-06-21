package org.foi.nwtis.mdomladov.web.dretve;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mdomladov.konfiguracije.APP_Konfiguracija;
import org.foi.nwtis.mdomladov.web.slusaci.SlusacAplikacije;

/**
 *
 *
 * @author Marko Domladovac
 */
public class UpravljackaDretva extends Thread {

    /**
     * stanje servera moze biti pauziran ili pokrenut
     */
    private StanjeServera stanjeServera;

    private APP_Konfiguracija konfiguracija;

    public UpravljackaDretva(APP_Konfiguracija konfiguracija) {
        this.konfiguracija = konfiguracija;
    }

    public UpravljackaDretva() {
        konfiguracija = (APP_Konfiguracija) SlusacAplikacije.context
                .getAttribute(SlusacAplikacije.APP_KONFIG);
    }

    @Override
    public void run() {
        super.run();
        stanjeServera = StanjeServera.STARTED;
        //Dva ista try catch-a su iz razloga što je prvi na razini 
        //kreiranja socketa a drugi pojedinog zahtjeva
        //Ne želimo da nam krivi zahtjev ub ije server
        try {
            ServerSocket ss = new ServerSocket(konfiguracija.getSocketPort());
            while (stanjeServera != StanjeServera.STOPPED) {
                try {
                    Socket socket = ss.accept();
                    RadnaDretva radnaDretva = new RadnaDretva(socket, konfiguracija);
                    radnaDretva.start();
                } catch (IOException ex) {
                    Logger.getLogger(UpravljackaDretva.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(UpravljackaDretva.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public synchronized void start() {
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

    public synchronized StanjeServera getStanjeServera() {
        return stanjeServera;
    }

    public synchronized void setStanjeServera(StanjeServera stanjeServera) {
        this.stanjeServera = stanjeServera;
    }

    public void setKonfiguracija(APP_Konfiguracija konfiguracija) {
        this.konfiguracija = konfiguracija;
    }    

    /**
     * Stanja servera
     */
    public enum StanjeServera {
        STARTED,
        PAUSED,
        STOPPED
    }
}
