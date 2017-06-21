/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.slusaci;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.foi.nwtis.mdomladov.konfiguracije.APP_Konfiguracija;
import org.foi.nwtis.mdomladov.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.mdomladov.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.mdomladov.web.dretve.PreuzmiMeteoPodatke;
import org.foi.nwtis.mdomladov.web.dretve.UpravljackaDretva;

/**
 * Slušač aplikacije starta pozadinsku dretvu i stavlja konfiguraciju u
 * aplikacijski kontekst
 *
 * @author Zeus
 */
@WebListener
public class SlusacAplikacije implements ServletContextListener {

    public static final String APP_KONFIG = "APP_Konfiguracija";

    public static final String UPRAVLJAC_IME = "UPRAVLJACKA_DRETVA";
    
     public static final String METEO_IME = "METEO_DRETVA";

    private static HashMap<String, Thread> dretve;

    public static ServletContext context;

    private UpravljackaDretva upravljackaDretva;

    private PreuzmiMeteoPodatke preuzmiMeteoPodatke;

    /**
     *
     * @param sce
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            context = sce.getServletContext();
            String filePath = context.getRealPath("WEB-INF")
                    + File.separator + context.getInitParameter("konfiguracija");

            APP_Konfiguracija bpKonfig = new APP_Konfiguracija(filePath);
            context.setAttribute(APP_KONFIG, bpKonfig);

            dretve = new HashMap<>();
            upravljackaDretva = new UpravljackaDretva();
            preuzmiMeteoPodatke = new PreuzmiMeteoPodatke();
            preuzmiMeteoPodatke.setInterval(bpKonfig.getTimeSecThread() * 1000);
            preuzmiMeteoPodatke.setApiKey(bpKonfig.getOWApiKey());
            upravljackaDretva.setKonfiguracija(bpKonfig);
            dretve.put(UPRAVLJAC_IME, upravljackaDretva);
            dretve.put(METEO_IME, preuzmiMeteoPodatke);

            upravljackaDretva.start();
            preuzmiMeteoPodatke.start();
        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            Logger.getLogger(SlusacAplikacije.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     *
     * @param sce
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if(upravljackaDretva != null){
            upravljackaDretva.interrupt();
        }
        
        if(preuzmiMeteoPodatke != null){
            preuzmiMeteoPodatke.interrupt();
        }
    }

    public static HashMap<String, Thread> getDretve() {
        return dretve;
    }
}
