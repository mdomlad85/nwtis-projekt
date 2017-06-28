/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.dretve;

import static java.lang.Thread.sleep;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.mdomladov.dal.MeteoDAL;
import org.foi.nwtis.mdomladov.dal.UredjajDAL;
import org.foi.nwtis.mdomladov.konfiguracije.APP_Konfiguracija;
import org.foi.nwtis.mdomladov.web.podaci.MeteoPodaci;
import org.foi.nwtis.mdomladov.rest.klijenti.OWMKlijent;
import org.foi.nwtis.mdomladov.web.podaci.Uredjaj;
import org.foi.nwtis.mdomladov.web.slusaci.SlusacAplikacije;

/**
 *
 * @author Zeus
 */
public class PreuzmiMeteoPodatke extends Thread {

    private int interval;

    private String apiKey;

    private long startTime;

    private final UredjajDAL uredjajDb;

    private final MeteoDAL meteoDb;

    public PreuzmiMeteoPodatke() {
        uredjajDb = new UredjajDAL();
        meteoDb = new MeteoDAL();
    }

    public PreuzmiMeteoPodatke(APP_Konfiguracija bpKonfig) {
        uredjajDb = new UredjajDAL(bpKonfig);
        meteoDb = new MeteoDAL(bpKonfig);
    }

    @Override
    public void run() {

        while (!this.isInterrupted()) {
            try {
                UpravljackaDretva uDretva = (UpravljackaDretva) SlusacAplikacije.getDretve()
                        .get(SlusacAplikacije.UPRAVLJAC_IME);

                synchronized (this) {
                    if (uDretva.getStanjeServera() == UpravljackaDretva.StanjeServera.PAUSED) {
                        this.wait();
                    }

                    startTime = System.currentTimeMillis();

                    List<Uredjaj> uredjaji = uredjajDb.getUredjaji();
                    if (uredjaji != null) {
                        OWMKlijent owm = new OWMKlijent(apiKey);
                        for (Uredjaj uredjaj : uredjaji) {
                            MeteoPodaci meteo = owm.getRealTimeWeather(uredjaj.getGeoloc());
                            meteoDb.addMeteo(meteo, uredjaj);
                        }
                    }
                }

            } catch (InterruptedException ex) {
                Logger.getLogger(PreuzmiMeteoPodatke.class.getName())
                        .log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(PreuzmiMeteoPodatke.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
            try {
                sleep(getTrajanjeSpavanja());
            } catch (InterruptedException ex) {
                Logger.getLogger(PreuzmiMeteoPodatke.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public synchronized void start() {
        Logger.getLogger(PreuzmiMeteoPodatke.class.getName()).log(Level.INFO,
                "ObradaPoruka je započela s radom", this);
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void interrupt() {
        Logger.getLogger(PreuzmiMeteoPodatke.class.getName()).log(Level.INFO,
                "ObradaPoruka je zavrsila s radom", this);
        super.interrupt();
    }

    /**
     *
     * @return
     */
    protected long getTrajanjeSpavanja() {
        int i = 1;
        long spavanje = 0;
        do {
            spavanje = i++ * interval - (System.currentTimeMillis() - startTime) / 1000;
        } while (spavanje < 0);

        return spavanje;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
