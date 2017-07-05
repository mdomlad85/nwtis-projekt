/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.slusaci;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.foi.nwtis.mdomladov.ejb.sb.UpraviteljJmsZrno;
import org.foi.nwtis.mdomladov.konfiguracije.APP_Konfiguracija;
import org.foi.nwtis.mdomladov.konfiguracije.NeispravnaKonfiguracija;
import org.foi.nwtis.mdomladov.konfiguracije.NemaKonfiguracije;

/**
 * Slušač aplikacije starta pozadinsku dretvu i stavlja konfiguraciju u
 * aplikacijski kontekst
 *
 * @author Zeus
 */
@WebListener
public class SlusacAplikacije implements ServletContextListener {

    @EJB
    private UpraviteljJmsZrno jmsZrno;

    public static final String APP_KONFIG = "APP_Konfiguracija";

    public static ServletContext context;
    
    private static APP_Konfiguracija konfiguracija;

    /**
     *
     * @param sce
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            context = sce.getServletContext();
            String webInf = context.getRealPath("WEB-INF") + File.separator;
            String filePath = webInf + context.getInitParameter("konfiguracija");

            konfiguracija = new APP_Konfiguracija(filePath);
            context.setAttribute(APP_KONFIG, konfiguracija);
            
            String jmsSerPath = webInf + konfiguracija.getNazivDatoteke();
            jmsZrno.setPutanjaDatoteke(jmsSerPath);

        } catch (NemaKonfiguracije | NeispravnaKonfiguracija ex) {
            Logger.getLogger(SlusacAplikacije.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static APP_Konfiguracija getKonfiguracija() {
        return konfiguracija;
    }
    /**
     *
     * @param sce
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
