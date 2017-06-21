/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.mdomladov.web.zrna;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Named;
import org.foi.nwtis.mdomladov.konfiguracije.APP_Konfiguracija;
import org.foi.nwtis.mdomladov.web.slusaci.SlusacAplikacije;

/**
 * Pomoćno zrno sa metodama 
 * za pomoć pri prikazu
 * 
 * @author Marko Domladovac
 */
@ManagedBean
@Named(value = "pomocnoZrno")
@ApplicationScoped
public class PomocnoZrno {
    
    public static String FORMAT_DATUMA = "dd.MM.yyyy";
    
    public static String FORMAT_VREMENA = "HH:mm:ss";
    
    private String locale = "hr";
    
    private final int velicinaStranica;

    /**
     * Konstruktor PomocnoZrno
     */
    public PomocnoZrno() {
        APP_Konfiguracija konfiguracija = (APP_Konfiguracija) SlusacAplikacije.context
                .getAttribute(SlusacAplikacije.APP_KONFIG);
        velicinaStranica = konfiguracija.velicinaStranice();
    }    

    public String formatDate(Date datum) {
        if (datum != null) {
            SimpleDateFormat dt1 = new SimpleDateFormat(
                    String.format("%s %s", FORMAT_DATUMA, FORMAT_VREMENA));
            return dt1.format(datum);
        }
        return null;
    }

    public String getFormatDatuma() {
        return FORMAT_DATUMA;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public int getVelicinaStranica() {
        return velicinaStranica;
    }
    
    
}
